package com.madaporc.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.model.AlerteReproduction;
import com.madaporc.model.AlerteReproduction.StatutAlerte;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.repository.AlerteReproductionRepository;
import com.madaporc.repository.GroupeReproductionRepository;

@Service
public class AlerteReproductionService {

    private static final String TYPE_MISE_BAS_PROCHE = "MISE_BAS_PROCHE";
    private static final List<StatutAlerte> STATUTS_ACTIFS = List.of(StatutAlerte.NON_LUE, StatutAlerte.LUE);

    private final AlerteReproductionRepository alerteRepository;
    private final GroupeReproductionRepository groupeRepository;
    private final NotificationService notificationService;
    private final EmailService emailService;
    private final String emailDestinataire;

    public AlerteReproductionService(AlerteReproductionRepository alerteRepository,
                                     GroupeReproductionRepository groupeRepository,
                                     NotificationService notificationService,
                                     EmailService emailService,
                                     @Value("${madaporc.alertes.email.destinataire:}") String emailDestinataire) {
        this.alerteRepository = alerteRepository;
        this.groupeRepository = groupeRepository;
        this.notificationService = notificationService;
        this.emailService = emailService;
        this.emailDestinataire = emailDestinataire;
    }

    public List<AlerteReproduction> listerAlertesActives() {
        return alerteRepository.findByStatutInOrderByDateAlerteDesc(STATUTS_ACTIFS);
    }

    public long countAlerteReproduction() {
        return alerteRepository.count();
    }

    @Transactional
    public int genererAlertesMiseBasProche() {
        LocalDate aujourdHui = LocalDate.now();
        LocalDate limite = aujourdHui.plusDays(5);
        List<GroupeReproduction> groupes = groupeRepository
                .findByDatePrevueMiseBasBetweenAndDateMiseBasReelleIsNullOrderByDatePrevueMiseBasAsc(aujourdHui, limite);

        int totalCree = 0;
        for (GroupeReproduction groupe : groupes) {
            boolean existe = alerteRepository.existsByGroupeReproductionIdAndTypeAlerteAndStatutIn(
                    groupe.getId(),
                    TYPE_MISE_BAS_PROCHE,
                    STATUTS_ACTIFS);

            if (!existe) {
                AlerteReproduction alerte = creerAlerteMiseBasProche(groupe);
                alerteRepository.save(alerte);
                notifier(alerte);
                totalCree++;
            }
        }
        return totalCree;
    }

    @Transactional
    public void marquerCommeLue(Long id) {
        AlerteReproduction alerte = trouverAlerte(id);
        if (alerte.getStatut() == StatutAlerte.NON_LUE) {
            alerte.setStatut(StatutAlerte.LUE);
        }
    }

    @Transactional
    public void traiter(Long id) {
        AlerteReproduction alerte = trouverAlerte(id);
        alerte.setStatut(StatutAlerte.TRAITEE);
    }

    private AlerteReproduction trouverAlerte(Long id) {
        return alerteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Alerte de reproduction introuvable."));
    }

    private AlerteReproduction creerAlerteMiseBasProche(GroupeReproduction groupe) {
        AlerteReproduction alerte = new AlerteReproduction();
        alerte.setGroupeReproduction(groupe);
        alerte.setLot(groupe.getLotFemelle());
        alerte.setTypeAlerte(TYPE_MISE_BAS_PROCHE);
        alerte.setDateAlerte(LocalDate.now());
        alerte.setStatut(StatutAlerte.NON_LUE);
        alerte.setMessage("Mise bas proche pour le groupe " + groupe.getCodeGroupe()
                + " prevue le " + groupe.getDatePrevueMiseBas() + ".");
        return alerte;
    }

    private void notifier(AlerteReproduction alerte) {
        notificationService.envoyerNotification(alerte.getMessage());

        if (emailDestinataire != null && !emailDestinataire.isBlank()) {
            emailService.envoyerTexte(emailDestinataire, "MADAPORC - Alerte reproduction", alerte.getMessage());
        }
    }
}
