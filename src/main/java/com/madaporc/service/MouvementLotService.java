package com.madaporc.service;

import com.madaporc.dto.MouvementLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class MouvementLotService {

    private final MouvementLotPorcRepository mouvementLotRepository;
    private final LotPorcRepository lotPorcRepository;

    public MouvementLotService(
            MouvementLotPorcRepository mouvementLotRepository,
            LotPorcRepository lotPorcRepository
    ) {
        this.mouvementLotRepository = mouvementLotRepository;
        this.lotPorcRepository = lotPorcRepository;
    }

    public void saveMouvement(MouvementLotPorc mouvement) {
        mouvementLotRepository.save(mouvement);
    }

    @Transactional(readOnly = true)
    public LotPorc getLot(Long lotId) {
        return lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + lotId));
    }

    @Transactional(readOnly = true)
    public List<MouvementLotPorc> getMouvementsByLot(Long lotId) {
        return mouvementLotRepository.findByLotIdOrderByDateMouvementDesc(lotId);
    }

    public String enregistrerMouvement(MouvementLotDTO dto) {
        if (dto.getLotId() == null) {
            return "Le lot est obligatoire.";
        }

        if (dto.getTypeMouvement() == null || dto.getTypeMouvement().trim().isEmpty()) {
            return "Le type de mouvement est obligatoire.";
        }

        if (dto.getQuantite() == null || dto.getQuantite() <= 0) {
            return "La quantité doit être supérieure à 0.";
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + dto.getLotId()));

        if ("ARCHIVE".equalsIgnoreCase(lot.getStatut())) {
            return "Impossible d'ajouter un mouvement sur un lot archivé.";
        }

        String type = dto.getTypeMouvement().trim().toUpperCase();

        // Date du mouvement : ni dans le futur, ni avant la creation du lot.
        LocalDate dateMvt = dto.getDateMouvement() != null ? dto.getDateMouvement() : LocalDate.now();
        if (dateMvt.isAfter(LocalDate.now())) {
            return "La date du mouvement ne peut pas être dans le futur.";
        }
        if (lot.getDateCreation() != null && dateMvt.isBefore(lot.getDateCreation())) {
            return "La date du mouvement ne peut pas précéder la date de création du lot.";
        }

        if (estSortie(type) && dto.getQuantite() > lot.getEffectifActuel()) {
            return "La quantité est supérieure à l'effectif actuel.";
        }

        if (estEntree(type)) {
            lot.setEffectifActuel(lot.getEffectifActuel() + dto.getQuantite());
        } else if (estSortie(type)) {
            lot.setEffectifActuel(lot.getEffectifActuel() - dto.getQuantite());
        } else {
            return "Type de mouvement invalide.";
        }

        MouvementLotPorc mouvement = new MouvementLotPorc();

        // Très important : on lie le mouvement au lot.
        mouvement.setLot(lot);

        mouvement.setTypeMouvement(type);
        mouvement.setQuantite(dto.getQuantite());

        if (dto.getDateMouvement() == null) {
            mouvement.setDateMouvement(LocalDate.now());
        } else {
            mouvement.setDateMouvement(dto.getDateMouvement());
        }

        mouvement.setObservation(dto.getObservation());

        lotPorcRepository.save(lot);
        mouvementLotRepository.save(mouvement);

        return null;
    }

    @Transactional(readOnly = true)
    public Integer getEffectifTotal(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + lotId));

        return lot.getEffectifActuel();
    }

    public void augmenterEffectif(Long lotId, Integer quantite) {
        augmenterEffectif(lotId, quantite, "Augmentation de l'effectif du lot");
    }

    // Variante avec motif explicite (ex. réintégration après annulation de vente),
    // pour une traçabilité claire du mouvement.
    public void augmenterEffectif(Long lotId, Integer quantite, String observation) {
        MouvementLotDTO dto = new MouvementLotDTO();
        dto.setLotId(lotId);
        dto.setTypeMouvement("ENTREE");
        dto.setQuantite(quantite);
        dto.setDateMouvement(LocalDate.now());
        dto.setObservation(observation);

        String error = enregistrerMouvement(dto);

        if (error != null) {
            throw new IllegalArgumentException(error);
        }
    }

    public void diminuerEffectif(Long lotId, Integer quantite) {
        MouvementLotDTO dto = new MouvementLotDTO();
        dto.setLotId(lotId);
        dto.setTypeMouvement("VENTE");
        dto.setQuantite(quantite);
        dto.setDateMouvement(LocalDate.now());
        dto.setObservation("Diminution de l'effectif du lot");

        String error = enregistrerMouvement(dto);

        if (error != null) {
            throw new IllegalArgumentException(error);
        }
    }

    @Transactional(readOnly = true)
    public boolean verifierQuantiteDisponible(Long lotId, Integer quantite) {
        if (quantite == null || quantite <= 0) {
            return false;
        }

        LotPorc lot = lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + lotId));

        return lot.getEffectifActuel() >= quantite;
    }

    private boolean estEntree(String type) {
        return "ENTREE".equals(type)
                || "NAISSANCE".equals(type)
                || "TRANSFERT_ENTREE".equals(type);
    }

    private boolean estSortie(String type) {
        return "DECES".equals(type)
                || "VENTE".equals(type)
                || "TRANSFERT_SORTIE".equals(type);
    }
}