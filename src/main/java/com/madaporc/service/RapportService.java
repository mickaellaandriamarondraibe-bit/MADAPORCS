package com.madaporc.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.repository.VenteRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.PresenceRepository;
import com.madaporc.repository.CycleProductionRepository;

import com.madaporc.DTO.RapportDTO;
import com.madaporc.DTO.RapportFinancierDTO;
import com.madaporc.DTO.RapportSanitaireDTO;
import com.madaporc.DTO.RapportStockDTO;
import com.madaporc.DTO.RapportPresenceDTO;
import com.madaporc.DTO.RapportProductionDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class RapportService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SuiviSanitaireRepository suiviSanitaireRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private CycleProductionRepository cycleProductionRepository;

    public RapportDTO genererRapportGlobal(LocalDate debut, LocalDate fin) {
        RapportDTO rapportGlobal = new RapportDTO();
        rapportGlobal.setRapportFinancier(genererRapportFinancier(debut, fin));
        rapportGlobal.setRapportSanitaire(genererRapportSanitaire(debut, fin));
        rapportGlobal.setRapportStock(genererRapportStock());
        rapportGlobal.setRapportPresence(genererRapportPresence(debut, fin));
        rapportGlobal.setRapportProduction(genererRapportProduction(debut, fin));
        return rapportGlobal;
    }

    public RapportFinancierDTO genererRapportFinancier(LocalDate debut, LocalDate fin) {
        RapportFinancierDTO dto = new RapportFinancierDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        BigDecimal totalVentes = venteRepository.sumMontantByDateBetween(debutDateTime, finDateTime);
        BigDecimal totalDepenses = depenseRepository.sumMontantByDateBetween(debut, fin);

        dto.setTotalVentes(totalVentes);
        dto.setTotalDepenses(totalDepenses);
        dto.setSolde(totalVentes.subtract(totalDepenses));

        List<DetailFinancierDTO> detailsVentes = new ArrayList<>();
        List<Vente> ventes = venteRepository.findAllByDateVenteBetween(debutDateTime, finDateTime);
        for (Vente v : ventes) {
            detailsVentes.add(new DetailFinancierDTO(
                v.getDateVente().toLocalDate(),
                "Vente #" + v.getId(),
                v.getMontantTotal(),
                "Vente"
            ));
        }
        dto.setDetailsVentes(detailsVentes);

        List<DetailFinancierDTO> detailsDepenses = new ArrayList<>();
        List<Depense> depenses = depenseRepository.findAllByDateDepenseBetween(debut, fin);
        for (Depense d : depenses) {
            detailsDepenses.add(new DetailFinancierDTO(
                d.getDateDepense(),
                d.getLibelle(),
                d.getMontant(),
                d.getCategorieDepense() != null ? d.getCategorieDepense().getLibelle() : "N/A"
            ));
        }
        dto.setDetailsDepenses(detailsDepenses);

        return dto;
    }

    public RapportSanitaireDTO genererRapportSanitaire(LocalDate debut, LocalDate fin) {
        RapportSanitaireDTO dto = new RapportSanitaireDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        List<SuiviSanitaire> suivis = suiviSanitaireRepository.findAllByDateDiagnosticBetween(debut, fin);

        int total = suivis.size();
        int enCours = 0;
        int gueris = 0;

        List<DetailSanitaireDTO> details = new ArrayList<>();
        for (SuiviSanitaire s : suivis) {
            String statut = s.getStatutSuiviSanitaire() != null ? s.getStatutSuiviSanitaire().getLibelle() : "Inconnu";

            if ("Gueri".equalsIgnoreCase(statut) || "Guérison".equalsIgnoreCase(statut)) {
                gueris++;
            } else {
                enCours++;
            }

            String cible;
            if (s.getLotPorc() != null) {
                cible = "Lot " + s.getLotPorc().getCodeLot();
            } else if (s.getReproducteur() != null) {
                cible = "Reproducteur " + s.getReproducteur().getCodeReproducteur();
            } else {
                cible = "N/A";
            }

            details.add(new DetailSanitaireDTO(
                s.getDateDiagnostic(),
                cible,
                s.getMaladie() != null ? s.getMaladie().getLibelle() : "N/A",
                s.getNombrePorcsMalades(),
                statut
            ));
        }

        dto.setNombreCasTotal(total);
        dto.setNombreCasEnCours(enCours);
        dto.setNombreCasGueris(gueris);
        dto.setDetails(details);

        return dto;
    }

    public RapportStockDTO genererRapportStock() {
        RapportStockDTO dto = new RapportStockDTO();

        List<Ingredient> ingredients = ingredientRepository.findByActifTrue();
        List<DetailStockDTO> details = new ArrayList<>();
        int sousSeuil = 0;

        for (Ingredient ing : ingredients) {
            boolean estSousSeuil = ing.getStockActuelKg().compareTo(ing.getSeuilMinKg()) < 0;
            if (estSousSeuil) {
                sousSeuil++;
            }

            details.add(new DetailStockDTO(
                ing.getLibelle(),
                ing.getStockActuelKg(),
                ing.getSeuilMinKg(),
                ing.getPrixKg(),
                estSousSeuil
            ));
        }

        dto.setIngredients(details);
        dto.setNombreIngredientsSousSeuil(sousSeuil);

        return dto;
    }

    public RapportPresenceDTO genererRapportPresence(LocalDate debut, LocalDate fin) {
        RapportPresenceDTO dto = new RapportPresenceDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        List<Presence> presences = presenceRepository.findByDatePresenceBetween(debut, fin);

        int present = 0;
        int absent = 0;
        int retard = 0;

        List<DetailPresenceDTO> details = new ArrayList<>();
        for (Presence p : presences) {
            switch (p.getStatutPresence()) {
                case "Present":
                    present++;
                    break;
                case "Absent":
                    absent++;
                    break;
                case "Retard":
                    retard++;
                    break;
                default:
                    break;
            }

            String nomEmploye = "N/A";
            if (p.getEmploye() != null) {
                nomEmploye = p.getEmploye().getNom() +
                        (p.getEmploye().getPrenom() != null ? " " + p.getEmploye().getPrenom() : "");
            }

            details.add(new DetailPresenceDTO(
                nomEmploye,
                p.getDatePresence(),
                p.getStatutPresence(),
                p.getHeureArrivee(),
                p.getHeureDepart()
            ));
        }

        dto.setNombrePresences(present);
        dto.setNombreAbsences(absent);
        dto.setNombreRetards(retard);
        dto.setDetails(details);

        return dto;
    }

    public RapportProductionDTO genererRapportProduction(LocalDate debut, LocalDate fin) {
        RapportProductionDTO dto = new RapportProductionDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        List<CycleProduction> cycles = cycleProductionRepository.findByDateDebutBetween(debut, fin);

        int totalNaissances = 0;
        int totalPertes = 0;
        int totalVivants = 0;
        int totalVendables = 0;

        List<DetailProductionDTO> details = new ArrayList<>();
        for (CycleProduction c : cycles) {
            totalNaissances += c.getNombreNaissances() != null ? c.getNombreNaissances() : 0;
            totalPertes += c.getNombrePertes() != null ? c.getNombrePertes() : 0;
            totalVivants += c.getNombreVivants() != null ? c.getNombreVivants() : 0;
            totalVendables += c.getNombreVendables() != null ? c.getNombreVendables() : 0;

            details.add(new DetailProductionDTO(
                c.getCodeCycle(),
                c.getLotPorc() != null ? c.getLotPorc().getCodeLot() : "N/A",
                c.getDateDebut(),
                c.getDateFinReelle(),
                c.getNombreNaissances(),
                c.getNombrePertes(),
                c.getNombreVivants(),
                c.getNombreVendables(),
                c.getStatutCycle()
            ));
        }

        dto.setTotalNaissances(totalNaissances);
        dto.setTotalPertes(totalPertes);
        dto.setTotalVivants(totalVivants);
        dto.setTotalVendables(totalVendables);

        // Placeholder - à adapter selon ta logique de calcul (table pesees_lots)
        dto.setGainPoidsMoyen(BigDecimal.ZERO);

        dto.setCycles(details);

        return dto;
    }
}