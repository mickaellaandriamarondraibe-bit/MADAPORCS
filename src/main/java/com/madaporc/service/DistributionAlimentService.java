package com.madaporc.service;

import java.math.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.DistributionAlimentDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class DistributionAlimentService {
    private final DistributionAlimentRepository repo;
    private final MelangeRepository melangeRepo;

    public DistributionAlimentService(DistributionAlimentRepository repo, MelangeRepository melangeRepo) {
        this.repo = repo;
        this.melangeRepo = melangeRepo;
    }

    public List<DistributionAliment> findAllDistributions() {
        return repo.findAllByOrderByDateDistributionDesc();
    }

    public String distribuer(DistributionAlimentDTO d, Long uid) {
        String e = verifierStockPourDistribution(d.getMelangeId(), d.getQuantiteKg());
        if (e != null)
            return e;
        DistributionAliment x = new DistributionAliment();
        x.setLotPorcId(d.getLotPorcId());
        x.setMelangeId(d.getMelangeId());
        x.setDateDistribution(d.getDateDistribution());
        x.setQuantiteKg(d.getQuantiteKg());
        x.setCoutTotal(calculerCoutDistribution(d.getMelangeId(), d.getQuantiteKg()));
        x.setObservation(d.getObservation());
        x.setCreatedBy(uid);
        x.setCreatedAt(LocalDateTime.now());
        repo.save(x);
        diminuerStockIngredients(d.getMelangeId(), d.getQuantiteKg(), uid);
        return null;
    }

    public BigDecimal calculerCoutDistribution(Long id, BigDecimal q) {
        Melange m = melangeRepo.findById(id).orElse(null);
        return m == null || m.getCoutKg() == null || q == null ? BigDecimal.ZERO : m.getCoutKg().multiply(q);
    }

    public String verifierStockPourDistribution(Long id, BigDecimal q) {
        return q == null || q.compareTo(BigDecimal.ZERO) <= 0 ? "Quantité invalide." : null;
    }

    public void diminuerStockIngredients(Long melangeId, BigDecimal q, Long uid) {
    }

    public void creerMouvementsStockApresDistribution(DistributionAliment d) {
    }

    public List<DistributionAliment> findByLot(Long lotId) {
        return repo.findByLotPorcId(lotId);
    }
}
