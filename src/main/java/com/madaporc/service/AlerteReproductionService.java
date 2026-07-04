package com.madaporc.service;

import com.madaporc.model.AlerteReproduction;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.Ingredient;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.AlerteReproductionRepository;
import com.madaporc.repository.GroupeReproductionRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AlerteReproductionService {

    private static final String STATUT_NON_LUE = "NON_LUE";
    private static final String STATUT_LUE = "LUE";
    private static final String STATUT_TRAITEE = "TRAITEE";
    private static final String TYPE_MISE_BAS_PROCHE = "MISE_BAS_PROCHE";
    private static final String TYPE_RETARD_MISE_BAS = "RETARD_MISE_BAS";
    private static final String TYPE_STOCK_FAIBLE = "STOCK_FAIBLE";
    private static final int JOURS_AVANT_ALERTE = 5;

    private final AlerteReproductionRepository alerteReproductionRepository;
    private final GroupeReproductionRepository groupeReproductionRepository;
    private final IngredientService ingredientService;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final Path journalEmailPath = Paths.get(System.getProperty("java.io.tmpdir"), "madaporc-alert-email-date.txt");

    public AlerteReproductionService(AlerteReproductionRepository alerteReproductionRepository,
                                     GroupeReproductionRepository groupeReproductionRepository,
                                     IngredientService ingredientService,
                                     NotificationService notificationService,
                                     EmailService emailService) {
        this.alerteReproductionRepository = alerteReproductionRepository;
        this.groupeReproductionRepository = groupeReproductionRepository;
        this.ingredientService = ingredientService;
        this.notificationService = notificationService;
        this.emailService = emailService;
    }

    public long countAlerteReproduction() {
        return alerteReproductionRepository.count();
    }

    public List<AlerteReproduction> listerAlertesActives() {
        return alerteReproductionRepository.findByStatut(STATUT_NON_LUE);
    }

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void genererAlertesMiseBasProche() {
        List<String> messages = new ArrayList<>();

        LocalDate aujourdHui = LocalDate.now();
        LocalDate limiteAlerte = aujourdHui.plusDays(JOURS_AVANT_ALERTE);

        for (GroupeReproduction groupe : groupeReproductionRepository.findAll()) {
            LocalDate datePrevue = groupe.getDatePrevueMiseBas();

            if (datePrevue == null || groupe.getDateMiseBasReelle() != null) {
                continue;
            }

            String typeAlerte;
            String message;

            if (datePrevue.isBefore(aujourdHui)) {
                typeAlerte = TYPE_RETARD_MISE_BAS;
                message = "Le groupe " + groupe.getCodeGroupe() + " a une mise bas en retard.";
            } else if (!datePrevue.isAfter(limiteAlerte)) {
                typeAlerte = TYPE_MISE_BAS_PROCHE;
                message = "Le groupe " + groupe.getCodeGroupe() + " a une mise bas proche.";
            } else {
                continue;
            }

            if (!alerteExiste(typeAlerte, message)) {
                enregistrerAlerte(groupe, groupe.getLotFemelle(), typeAlerte, message);
                messages.add(message);
            }
        }

        // genererAlerteStockFaible(messages);

        for (String message : messages) {
            notificationService.envoyerNotification(message);
        }

        envoyerEmailJournalier(messages);
    }

    @Transactional
    public String marquerCommeLue(Long id) {
        AlerteReproduction alerte = alerteReproductionRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Alerte introuvable."));

        alerte.setStatut(STATUT_LUE);
        alerteReproductionRepository.save(alerte);
        return "Alerte marquee comme lue.";
    }

    @Transactional
    public String traiter(Long id) {
        AlerteReproduction alerte = alerteReproductionRepository.findById(Math.toIntExact(id))
                .orElseThrow(() -> new IllegalArgumentException("Alerte introuvable."));

        alerte.setStatut(STATUT_TRAITEE);
        alerteReproductionRepository.save(alerte);
        return "Alerte traitee.";
    }

    // private void genererAlerteStockFaible(List<String> messages) {
    //     List<Ingredient> ingredients = ingredientService.listerStocksFaibles();

    //     for (Ingredient ingredient : ingredients) {
    //         String message = "Stock faible pour l'ingrédient " + ingredient.getNom()
    //                 + " (" + ingredient.getStockActuel() + " " + ingredient.getUnite() + ")";

    //         if (!alerteExiste(TYPE_STOCK_FAIBLE, message)) {
    //             enregistrerAlerte(null, null, TYPE_STOCK_FAIBLE, message);
    //             messages.add(message);
    //         }
    //     }
    // }

    private boolean alerteExiste(String typeAlerte, String message) {
        return alerteReproductionRepository.findByTypeAlerteAndStatut(typeAlerte, STATUT_NON_LUE)
                .stream()
                .anyMatch(alerte -> message.equals(alerte.getMessage()));
    }

    private void envoyerEmailJournalier(List<String> messages) {
        if (messages.isEmpty() || emailDejaEnvoyeAujourdHui() || !emailService.isConfigured()) {
            return;
        }

        StringBuilder corps = new StringBuilder();

        corps.append("<div style=\"font-family:'Segoe UI',Arial,sans-serif; padding:20px; color:#333333; max-width:600px; margin:0 auto; border:1px solid #e0e0e0; border-radius:8px;\">")
            // En-tête / Salutations
            .append("<p style=\"font-size:16px; margin-bottom:15px;\">Bonjour,</p>")
            
            // Objectif du mail
            .append("<p style=\"font-size:14px; line-height:1.6; margin-bottom:20px;\">")
            .append("Dans le cadre du suivi opérationnel de nos systèmes, vous trouverez ci-dessous le <strong>rapport consolidé des alertes</strong> générées ce jour. ")
            .append("L'objectif de ce récapitulatif est de vous permettre d'identifier rapidement les anomalies et de planifier les actions correctives nécessaires.")
            .append("</p>")
            
            // Titre de la section
            .append("<h2 style=\"color:#1b4332; font-size:18px; border-bottom:2px solid #1b4332; padding-bottom:5px; margin-top:25px;\">Synthèse des alertes</h2>")
            
            // Structure de la liste (ou tableau)
            .append("<ul style=\"padding-left:20px; line-height:1.8; font-size:14px;\">");

        // Déduplication et insertion des messages
        Set<String> uniques = new LinkedHashSet<>(messages);
        for (String message : uniques) {
            corps.append("<li style=\"margin-bottom:8px;\">").append(message).append("</li>");
        }

        corps.append("</ul>")
            .append("<hr style=\"border:0; border-top:1px solid #e0e0e0; margin:25px 0;\">")
            
            // Pied de page / Clôture
            .append("<p style=\"font-size:14px; margin-bottom:5px;\">Nos équipes restent à votre disposition pour tout complément d'information ou pour vous accompagner dans la résolution de ces incidents.</p>")
            .append("<p style=\"font-size:14px; font-weight:bold; margin-top:15px;\">Cordialement,<br>")
            .append("<span style=\"color:#666666; font-weight:normal; font-size:13px;\">Le service Support & Supervision</span></p>")
            .append("</div>");

        if (emailService.envoyerHTML(null, "MADAPORC - Alertes du jour", corps.toString())) {
            marquerEmailCommeEnvoyeAujourdHui();
        }
    }

    private boolean emailDejaEnvoyeAujourdHui() {
        try {
            if (!Files.exists(journalEmailPath)) {
                return false;
            }

            String contenu = Files.readString(journalEmailPath, StandardCharsets.UTF_8).trim();
            return LocalDate.now().toString().equals(contenu);
        } catch (IOException e) {
            return false;
        }
    }

    private void marquerEmailCommeEnvoyeAujourdHui() {
        try {
            Files.writeString(journalEmailPath, LocalDate.now().toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            // Ne bloque pas l'enregistrement des alertes si le marquage échoue.
        }
    }

    private void enregistrerAlerte(GroupeReproduction groupe, LotPorc lot, String typeAlerte, String message) {
        AlerteReproduction alerte = new AlerteReproduction();
        alerte.setGroupeReproduction(groupe);
        alerte.setLot(lot);
        alerte.setTypeAlerte(typeAlerte);
        alerte.setMessage(message);
        alerte.setDate_alerte(LocalDateTime.now());
        alerte.setStatut(STATUT_NON_LUE);
        alerteReproductionRepository.save(alerte);
    }
}