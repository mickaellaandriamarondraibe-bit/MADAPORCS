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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SuiviSanitaireService {

    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final LotPorcRepository lotPorcRepository;
    private final MaladieRepository maladieRepository;
    private final TraitementRepository traitementRepository;

    public List<SuiviSanitaire> getAll() {
        return suiviSanitaireRepository.findAll();
    }

    public SuiviSanitaireDTO getDtoById(Long id) {
        SuiviSanitaire ent = suiviSanitaireRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Suivi sanitaire introuvable"));

        SuiviSanitaireDTO dto = new SuiviSanitaireDTO();
        dto.setId(ent.getId());
        dto.setLotId(ent.getLotId());
        dto.setMaladieId(null);
        dto.setTraitementId(null);
        dto.setDateDiagnostic(ent.getDateDiagnostic());
        dto.setDateTraitement(ent.getDateTraitement());
        dto.setDateGuerison(ent.getDateGuerison());
        dto.setObservation(ent.getObservation());
        return dto;
    }

    public String enregistrer(SuiviSanitaireDTO dto) {
        if (dto == null) return "Données invalides.";
        if (dto.getLotId() == null) return "Lot obligatoire.";
        if (dto.getDateDiagnostic() == null) return "Date diagnostic obligatoire.";

        if (dto.getDateTraitement() != null && dto.getDateTraitement().isBefore(dto.getDateDiagnostic())) {
            return "Date traitement doit être >= date diagnostic.";
        }
        if (dto.getDateGuerison() != null && dto.getDateGuerison().isBefore(dto.getDateDiagnostic())) {
            return "Date guérison doit être >= date diagnostic.";
        }
        if (dto.getDateDiagnostic().isAfter(LocalDate.now())) {
            return "Date diagnostic ne peut pas être dans le futur.";
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId()).orElse(null);
        if (lot == null) return "Lot introuvable.";

        Maladie maladie = dto.getMaladieId() != null ? maladieRepository.findById(dto.getMaladieId()).orElse(null) : null;
        if (dto.getMaladieId() != null && maladie == null) return "Maladie introuvable.";

        Traitement traitement = dto.getTraitementId() != null ? traitementRepository.findById(dto.getTraitementId()).orElse(null) : null;
        if (dto.getTraitementId() != null && traitement == null) return "Traitement introuvable.";

        // Note: la règle "suivi sanitaire lié a un lot" est couverte par lot obligatoire.

        SuiviSanitaire ent = (dto.getId() == null)
                ? new SuiviSanitaire()
                : suiviSanitaireRepository.findById(dto.getId()).orElse(new SuiviSanitaire());

        ent.setLot(lot);
        ent.setMaladie(maladie);
        ent.setTraitement(traitement);
        ent.setDateDiagnostic(dto.getDateDiagnostic());
        ent.setDateTraitement(dto.getDateTraitement());
        ent.setDateGuerison(dto.getDateGuerison());
        ent.setObservation(dto.getObservation());

        suiviSanitaireRepository.save(ent);
        return null;
    }
}

