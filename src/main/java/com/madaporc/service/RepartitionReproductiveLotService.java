package com.madaporc.service;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.madaporc.repository.RepartitionReproductiveLotRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import com.madaporc.model.RepartitionReproductiveLot;

@Service
public class RepartitionReproductiveLotService {
    @Autowired
    private RepartitionReproductiveLotRepository repartitionReproductiveLotRepository;

    public RepartitionReproductiveLotService(RepartitionReproductiveLotRepository repartitionReproductiveLotRepository) {
        this.repartitionReproductiveLotRepository = repartitionReproductiveLotRepository;
    }

    public RepartitionReproductiveLotRepository getRepartitionReproductiveLotRepository() {
        return repartitionReproductiveLotRepository;
    }

    @Transactional
    public void initialiserRepartitionLotFemelle(Long lotId) {
    }

    @Transactional(readOnly = true)
    public List<RepartitionReproductiveLot> getRepartitionByLot(Long lotId) {
        return repartitionReproductiveLotRepository.findByLotId(lotId);
    }

    public Integer calculerFemellesDisponibles(Long lotId) {
        return 0;
    }

    public Integer calculerFemellesEnCycle(Long lotId) {
        return 0;
    }

    @Transactional
    public void mettreAJourRepartition(Long lotId, String statut, Integer quantite) {
    }
}