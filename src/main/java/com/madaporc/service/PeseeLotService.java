package com.madaporc.service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.PeseeLotDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class PeseeLotService {
    private final PeseeLotRepository repo;
    private final LotPorcRepository lotRepo;

    public PeseeLotService(PeseeLotRepository repo, LotPorcRepository lotRepo) {
        this.repo = repo;
        this.lotRepo = lotRepo;
    }

    public String ajouterPesee(PeseeLotDTO dto, Long uid) {
        String e = validerPesee(dto);
        if (e != null)
            return e;
        PeseeLot p = new PeseeLot();
        p.setLotPorcId(dto.getLotPorcId());
        p.setPoidsMoyenKg(dto.getPoidsMoyenKg());
        p.setDatePesee(dto.getDatePesee());
        p.setObservation(dto.getObservation());
        p.setCreatedBy(uid);
        p.setCreatedAt(LocalDateTime.now());
        repo.save(p);
        mettreAJourPoidsMoyenActuel(dto.getLotPorcId(), dto.getPoidsMoyenKg());
        return null;
    }

    public List<PeseeLot> findPesees(Long lotId) {
        return repo.findByLotPorcIdOrderByDatePeseeAsc(lotId);
    }

    public BigDecimal calculerEvolutionPoids(Long lotId) {
        List<PeseeLot> l = findPesees(lotId);
        if (l.size() < 2)
            return BigDecimal.ZERO;
        return l.get(l.size() - 1).getPoidsMoyenKg().subtract(l.get(0).getPoidsMoyenKg());
    }

    public void mettreAJourPoidsMoyenActuel(Long lotId, BigDecimal poids) {
        lotRepo.findById(lotId).ifPresent(l -> {
            l.setPoidsMoyenActuelKg(poids);
            lotRepo.save(l);
        });
    }

    public String validerPesee(PeseeLotDTO dto) {
        if (dto == null)
            return "Pesée obligatoire.";
        if (dto.getLotPorcId() == null)
            return "Lot obligatoire.";
        if (dto.getPoidsMoyenKg() == null || dto.getPoidsMoyenKg().compareTo(BigDecimal.ZERO) <= 0)
            return "Poids invalide.";
        if (dto.getDatePesee() == null || dto.getDatePesee().isAfter(LocalDate.now()))
            return "Date invalide.";
        return null;
    }

    public List<PeseeLot> findAllPesees() {
    return repo.findAll();
}
}
