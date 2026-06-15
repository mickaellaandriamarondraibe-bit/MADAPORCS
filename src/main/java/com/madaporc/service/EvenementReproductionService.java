package com.madaporc.service;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.EvenementReproductionDTO;
import com.madaporc.model.EvenementReproduction;
import com.madaporc.repository.*;

@Service
public class EvenementReproductionService {
    private final EvenementReproductionRepository repo;
    private final LotPorcRepository lotRepo;

    public EvenementReproductionService(EvenementReproductionRepository repo, LotPorcRepository lotRepo) {
        this.repo = repo;
        this.lotRepo = lotRepo;
    }

    public List<EvenementReproduction> rechercherEvenements(Long typeId) {
        return typeId == null ? repo.findAll() : repo.findByTypeEvenementReproductionId(typeId);
    }

    public String ajouter(EvenementReproductionDTO dto, Long uid) {
        EvenementReproduction e = ent(dto);
        e.setCreatedBy(uid);
        e.setCreatedAt(LocalDateTime.now());
        repo.save(e);
        return null;
    }

    public String modifier(Long id, EvenementReproductionDTO dto) {
        EvenementReproduction e = ent(dto);
        e.setId(id);
        repo.save(e);
        return null;
    }

    public int calculerPorceletsVivants(Integer nes, Integer morts) {
        return (nes == null ? 0 : nes) - (morts == null ? 0 : morts);
    }

    public String creerLotApresMiseBas(EvenementReproductionDTO dto) {
        return null;
    }

    public void mettreAJourCycleApresMiseBas(Long evenementId) {
    }

    private EvenementReproduction ent(EvenementReproductionDTO d) {
        EvenementReproduction e = new EvenementReproduction();
        e.setId(d.getId());
        e.setFemelleId(d.getFemelleId());
        e.setMaleId(d.getMaleId());
        e.setTypeEvenementReproductionId(d.getTypeEvenementReproductionId());
        e.setLotPorcId(d.getLotPorcId());
        e.setDateEvenement(d.getDateEvenement());
        e.setNombrePorceletsNes(d.getNombrePorceletsNes());
        e.setNombrePorceletsMorts(d.getNombrePorceletsMorts());
        e.setNombrePorceletsVivants(calculerPorceletsVivants(d.getNombrePorceletsNes(), d.getNombrePorceletsMorts()));
        e.setObservation(d.getObservation());
        return e;
    }
}
