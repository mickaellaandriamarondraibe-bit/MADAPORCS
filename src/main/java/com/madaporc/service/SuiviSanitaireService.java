package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.SuiviSanitaireDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.SuiviSanitaire;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.ReproducteurRepository;
import com.madaporc.repository.SuiviSanitaireRepository;

@Service
public class SuiviSanitaireService {

    private final SuiviSanitaireRepository repo;
    private final LotPorcRepository lotRepo;
    private final ReproducteurRepository reproducteurRepo;

    public SuiviSanitaireService(
            SuiviSanitaireRepository repo,
            LotPorcRepository lotRepo,
            ReproducteurRepository reproducteurRepo
    ) {
        this.repo = repo;
        this.lotRepo = lotRepo;
        this.reproducteurRepo = reproducteurRepo;
    }


    public List<SuiviSanitaire> rechercherSuivis(Long statutId, LocalDate debut, LocalDate fin) {
        if (statutId != null) {
            return repo.findByStatutSuiviSanitaireId(statutId);
        }

        if (debut != null && fin != null) {
            return repo.findByDateDiagnosticBetween(debut, fin);
        }

        return repo.findAll();
    }


    public String ajouter(SuiviSanitaireDTO dto, Long utilisateurId) {
        String erreur = validerSuivi(dto);

        if (erreur != null) {
            return erreur;
        }

        SuiviSanitaire suivi = ent(dto);

        suivi.setCreatedBy(utilisateurId);
        suivi.setCreatedAt(LocalDateTime.now());

        repo.save(suivi);

        return null;
    }


    public String modifier(Long id, SuiviSanitaireDTO dto) {
        if (id == null) {
            return "Identifiant invalide.";
        }

        if (!repo.existsById(id)) {
            return "Suivi sanitaire introuvable.";
        }

        String erreur = validerSuivi(dto);

        if (erreur != null) {
            return erreur;
        }

        SuiviSanitaire suivi = ent(dto);

        suivi.setId(id);

        repo.save(suivi);

        return null;
    }


    public String modifierStatut(Long suiviId, Long statutId) {
        if (suiviId == null || statutId == null) {
            return "Données invalides.";
        }

        SuiviSanitaire suivi = repo.findById(suiviId).orElse(null);

        if (suivi == null) {
            return "Suivi sanitaire introuvable.";
        }

        suivi.setStatutSuiviSanitaireId(statutId);

        repo.save(suivi);

        return null;
    }


    public BigDecimal calculerTauxGuerison(LocalDate debut, LocalDate fin) {
        List<SuiviSanitaire> suivis = rechercherSuivis(null, debut, fin);

        if (suivis.isEmpty()) {
            return BigDecimal.ZERO;
        }

        long gueris = suivis.stream()
                .filter(s -> s.getDateGuerisonReelle() != null)
                .count();

        return BigDecimal.valueOf(gueris)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(suivis.size()), 2, RoundingMode.HALF_UP);
    }


    public BigDecimal calculerTauxMortalite(LocalDate debut, LocalDate fin) {
        List<SuiviSanitaire> suivis = rechercherSuivis(null, debut, fin);

        int totalMalades = suivis.stream()
                .map(SuiviSanitaire::getNombrePorcsMalades)
                .filter(n -> n != null)
                .mapToInt(Integer::intValue)
                .sum();

        if (totalMalades == 0) {
            return BigDecimal.ZERO;
        }

        long casNonGueris = suivis.stream()
                .filter(s -> s.getDateGuerisonReelle() == null)
                .count();

        return BigDecimal.valueOf(casNonGueris)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalMalades), 2, RoundingMode.HALF_UP);
    }


    public List<SuiviSanitaire> listerAlertesSanitaires() {
        return repo.findAll()
                .stream()
                .filter(s -> s.getDateGuerisonReelle() == null)
                .toList();
    }


    public String verifierNombreMalades(Long lotId, Integer nombreMalades) {
        if (nombreMalades == null || nombreMalades < 0) {
            return "Nombre de porcs malades invalide.";
        }

        if (lotId == null) {
            return null;
        }

        LotPorc lot = lotRepo.findById(lotId).orElse(null);

        if (lot == null) {
            return "Lot introuvable.";
        }

        if (lot.getNombreActuel() != null && nombreMalades > lot.getNombreActuel()) {
            return "Le nombre de porcs malades ne peut pas dépasser l'effectif actuel du lot.";
        }

        return null;
    }


    public String validerSuivi(SuiviSanitaireDTO dto) {
        if (dto == null) {
            return "Suivi sanitaire obligatoire.";
        }

        boolean lotPresent = dto.getLotPorcId() != null;
        boolean reproducteurPresent = dto.getReproducteurId() != null;

        if (!lotPresent && !reproducteurPresent) {
            return "Le suivi doit concerner un lot ou un reproducteur.";
        }

        if (lotPresent && reproducteurPresent) {
            return "Le suivi ne peut pas concerner un lot et un reproducteur en même temps.";
        }

        if (lotPresent && !lotRepo.existsById(dto.getLotPorcId())) {
            return "Lot introuvable.";
        }

        if (reproducteurPresent && !reproducteurRepo.existsById(dto.getReproducteurId())) {
            return "Reproducteur introuvable.";
        }

        if (dto.getMaladieId() == null) {
            return "Maladie obligatoire.";
        }

        if (dto.getStatutSuiviSanitaireId() == null) {
            return "Statut du suivi obligatoire.";
        }

        if (dto.getDateDiagnostic() == null) {
            return "Date de diagnostic obligatoire.";
        }

        if (dto.getDateDiagnostic().isAfter(LocalDate.now())) {
            return "La date de diagnostic ne peut pas être dans le futur.";
        }

        String erreur = verifierNombreMalades(dto.getLotPorcId(), dto.getNombrePorcsMalades());

        if (erreur != null) {
            return erreur;
        }

        return null;
    }


    private SuiviSanitaire ent(SuiviSanitaireDTO dto) {
        SuiviSanitaire suivi = new SuiviSanitaire();

        suivi.setId(dto.getId());
        suivi.setLotPorcId(dto.getLotPorcId());
        suivi.setReproducteurId(dto.getReproducteurId());
        suivi.setNombrePorcsMalades(dto.getNombrePorcsMalades());
        suivi.setMaladieId(dto.getMaladieId());
        suivi.setTraitementId(dto.getTraitementId());
        suivi.setStatutSuiviSanitaireId(dto.getStatutSuiviSanitaireId());
        suivi.setDateDiagnostic(dto.getDateDiagnostic());
        suivi.setDateGuerisonPrevue(dto.getDateGuerisonPrevue());
        suivi.setDateGuerisonReelle(dto.getDateGuerisonReelle());
        suivi.setSymptomes(dto.getSymptomes());
        suivi.setDiagnostic(dto.getDiagnostic());
        suivi.setObservation(dto.getObservation());

        return suivi;
    }
}