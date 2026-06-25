package com.madaporc.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.madaporc.repository.RepartitionReproductiveLotRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import com.madaporc.model.RepartitionReproductiveLot;
import com.madaporc.model.StatutReproductif;
import com.madaporc.repository.StatutReproductifRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.model.LotPorc;
import java.time.LocalDateTime;

@Service
public class RepartitionReproductiveLotService {
    @Autowired
    private RepartitionReproductiveLotRepository repartitionReproductiveLotRepository;

    @Autowired
    private StatutReproductifRepository statutReproductifRepository;

    @Autowired
    private LotPorcRepository lotPorcRepository;

    public RepartitionReproductiveLotService(RepartitionReproductiveLotRepository repartitionReproductiveLotRepository) {
        this.repartitionReproductiveLotRepository = repartitionReproductiveLotRepository;
    }

    public RepartitionReproductiveLotRepository getRepartitionReproductiveLotRepository() {
        return repartitionReproductiveLotRepository;
    }

    @Transactional
    public void initialiserRepartitionLotFemelle(Long lotId) {
        List<StatutReproductif> statuts = statutReproductifRepository.findAll();
        LotPorc lotPorc = lotPorcRepository.findById(lotId).orElse(null);

        if (lotPorc != null) {
            for (StatutReproductif statut : statuts) {
                RepartitionReproductiveLot repartition = new RepartitionReproductiveLot();
                repartition.setLotPorc(lotPorc);
                repartition.setStatutReproductif(statut);

                if (statut.getCode().equals("PRETE_JAMAIS_SAILLIE")) {
                    repartition.setQuantite(lotPorc.getEffectifInitial());
                } else {
                    repartition.setQuantite(0);
                }

                repartition.setDateMiseAJour(LocalDateTime.now());
                repartitionReproductiveLotRepository.save(repartition);
            }
        }
    }

    @Transactional(readOnly = true)
    public List<RepartitionReproductiveLot> getRepartitionByLot(Long lotId) {
        return repartitionReproductiveLotRepository.findByLotId(lotId);
    }

    public Integer calculerFemellesDisponibles(Long lotId) {
        Integer quantitePreteJamaisSaillie = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "PRETE_JAMAIS_SAILLIE");
        Integer quantiteReproductriceApte = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "DEJA_REPRODUCTRICE_APTE");
        return (quantitePreteJamaisSaillie != null ? quantitePreteJamaisSaillie : 0) + (quantiteReproductriceApte != null ? quantiteReproductriceApte : 0);
    }

    public Integer calculerFemellesEnCycle(Long lotId) {
        return repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "EN_CYCLE");
    }

    @Transactional
    public void mettreAJourRepartition(Long lotId, String statut, Integer quantite) {
        RepartitionReproductiveLot repartition = repartitionReproductiveLotRepository.findByLotIdAndStatutReproductif(lotId, statut).orElse(null);
        if (repartition != null) {
            repartition.setQuantite(quantite);
            repartition.setDateMiseAJour(LocalDateTime.now());
            repartitionReproductiveLotRepository.save(repartition);
        }
    }
}