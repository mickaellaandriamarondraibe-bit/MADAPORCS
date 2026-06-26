package com.madaporc.service;

import com.madaporc.dto.SuiviSanitaireDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Maladie;
import com.madaporc.model.SuiviSanitaire;
import com.madaporc.model.Traitement;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MaladieRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.TraitementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SuiviSanitaireService {

    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final LotPorcRepository lotPorcRepository;
    private final MaladieRepository maladieRepository;
    private final TraitementRepository traitementRepository;

    @Transactional(readOnly = true)
    public List<SuiviSanitaire> getAll() {
        return suiviSanitaireRepository.findAllWithDetails();
    }
    @Transactional(readOnly = true)
    public SuiviSanitaireDTO getDtoById(Long id) {
        SuiviSanitaire suivi = suiviSanitaireRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suivi sanitaire introuvable avec l'id : " + id));

        SuiviSanitaireDTO dto = new SuiviSanitaireDTO();

        dto.setId(suivi.getId());

        if (suivi.getLot() != null) {
            dto.setLotId(suivi.getLot().getId());
        }

        if (suivi.getMaladie() != null) {
            dto.setMaladieId(suivi.getMaladie().getId());
        }

        if (suivi.getTraitement() != null) {
            dto.setTraitementId(suivi.getTraitement().getId());
        }

        dto.setDateDiagnostic(suivi.getDateDiagnostic());
        dto.setDateTraitement(suivi.getDateTraitement());
        dto.setDateGuerison(suivi.getDateGuerison());
        dto.setObservation(suivi.getObservation());

        return dto;
    }

    public String enregistrer(SuiviSanitaireDTO dto) {
        if (dto.getLotId() == null) {
            return "Le lot est obligatoire.";
        }

        if (dto.getDateDiagnostic() == null) {
            return "La date de diagnostic est obligatoire.";
        }

        if (dto.getDateTraitement() != null &&
                dto.getDateTraitement().isBefore(dto.getDateDiagnostic())) {
            return "La date de traitement doit être supérieure ou égale à la date de diagnostic.";
        }

        if (dto.getDateGuerison() != null &&
                dto.getDateGuerison().isBefore(dto.getDateDiagnostic())) {
            return "La date de guérison doit être supérieure ou égale à la date de diagnostic.";
        }

        SuiviSanitaire suivi;

        if (dto.getId() == null) {
            suivi = new SuiviSanitaire();
        } else {
            suivi = suiviSanitaireRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Suivi sanitaire introuvable avec l'id : " + dto.getId()));
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + dto.getLotId()));

        Maladie maladie = null;
        if (dto.getMaladieId() != null) {
            maladie = maladieRepository.findById(dto.getMaladieId())
                    .orElseThrow(() -> new IllegalArgumentException("Maladie introuvable avec l'id : " + dto.getMaladieId()));
        }

        Traitement traitement = null;
        if (dto.getTraitementId() != null) {
            traitement = traitementRepository.findById(dto.getTraitementId())
                    .orElseThrow(() -> new IllegalArgumentException("Traitement introuvable avec l'id : " + dto.getTraitementId()));
        }

        suivi.setLot(lot);
        suivi.setMaladie(maladie);
        suivi.setTraitement(traitement);
        suivi.setDateDiagnostic(dto.getDateDiagnostic());
        suivi.setDateTraitement(dto.getDateTraitement());
        suivi.setDateGuerison(dto.getDateGuerison());
        suivi.setObservation(dto.getObservation());

        suiviSanitaireRepository.save(suivi);

        return null;
    }
}