package com.madaporc.service;

import org.springframework.stereotype.Service;

import com.madaporc.dto.MouvementLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.repository.MouvementLotPorcRepository;
import java.util.List;
import java.util.ArrayList;

@Service
public class MouvementLotService {
    private final MouvementLotPorcRepository mouvementLotRepository;

    public MouvementLotService(MouvementLotPorcRepository mouvementLotRepository) {
        this.mouvementLotRepository = mouvementLotRepository;
    }

    public List<MouvementLotPorc> getMouvementsByLot(Long lotId) {
        List<MouvementLotPorc> mouvements = new ArrayList<>();
        mouvements = mouvementLotRepository.findByLotIdOrderByDateMouvementDesc(lotId);
        return mouvements;
    }

    public void saveMouvement(MouvementLotPorc mouvement) {
        mouvementLotRepository.save(mouvement);
    }

    public String enregistrerMouvement(MouvementLotDTO dto) {
        MouvementLotPorc mouvement = new MouvementLotPorc();
        mouvement.setId(dto.getLotId());
        mouvement.setTypeMouvement(dto.getTypeMouvement());
        mouvement.setQuantite(dto.getQuantite());
        mouvement.setDateMouvement(dto.getDateMouvement());
        mouvement.setObservation(dto.getObservation());
        mouvement.setCreatedAt(dto.getCreatedAt());

        mouvementLotRepository.save(mouvement);
        return "redirect:/lots/" + dto.getLotId();
    }

    public Integer getEffectifTotal(Long lotId) {
        List<MouvementLotPorc> mouvements = mouvementLotRepository.findByLotIdOrderByDateMouvementDesc(lotId);
        Integer effectifTotal = 0;

        for (MouvementLotPorc mouvement : mouvements) {
            if ("AUGMENTATION".equals(mouvement.getTypeMouvement())) {
                effectifTotal += mouvement.getQuantite();
            } else if ("DIMINUTION".equals(mouvement.getTypeMouvement())) {
                effectifTotal -= mouvement.getQuantite();
            }
        }
        return effectifTotal;
    }

    public void augmenterEffectif(Long lotId, Integer quantite) {
        MouvementLotPorc mouvement = new MouvementLotPorc();
        mouvement.setId(lotId);
        mouvement.setTypeMouvement("AUGMENTATION");
        mouvement.setQuantite(quantite);
        mouvement.setDateMouvement(java.time.LocalDate.now());
        mouvement.setObservation("Augmentation de l'effectif du lot");
        mouvement.setCreatedAt(java.time.LocalDateTime.now());

        mouvementLotRepository.save(mouvement);
    }

    public void diminuerEffectif(Long lotId, Integer quantite) {
        if(!verifierQuantiteDisponible(lotId, quantite)) {
            throw new IllegalArgumentException("Quantite insuffisante pour le lot");
        }
        MouvementLotPorc mouvement = new MouvementLotPorc();
        mouvement.setId(lotId);
        mouvement.setTypeMouvement("DIMINUTION");
        mouvement.setQuantite(quantite);
        mouvement.setDateMouvement(java.time.LocalDate.now());
        mouvement.setObservation("Diminution de l'effectif du lot");
        mouvement.setCreatedAt(java.time.LocalDateTime.now());

        mouvementLotRepository.save(mouvement);
    }

    public boolean verifierQuantiteDisponible(Long lotId, Integer quantite) {
        Integer effectifTotal = getEffectifTotal(lotId);
        return effectifTotal >= quantite;
    }
}