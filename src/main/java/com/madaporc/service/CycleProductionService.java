package com.madaporc.service;

import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.CycleProductionDTO;
import com.madaporc.model.CycleProduction;
import com.madaporc.repository.*;

@Service
public class CycleProductionService {
    private final CycleProductionRepository repo;
    private final LotPorcRepository lotRepo;

    public CycleProductionService(CycleProductionRepository repo, LotPorcRepository lotRepo) {
        this.repo = repo;
        this.lotRepo = lotRepo;
    }

    public List<CycleProduction> findAllCycles() {
        return repo.findAllByOrderByDateDebutDesc();
    }

    public String creer(CycleProductionDTO dto) {
        if (repo.existsByCodeCycle(dto.getCodeCycle()))
            return "Code cycle déjà utilisé.";
        CycleProduction c = ent(dto);
        c.setNombreVivants(calculerNombreVivants(dto.getNombreNaissances(), dto.getNombrePertes()));
        c.setCreatedAt(LocalDateTime.now());
        repo.save(c);
        return null;
    }

    public String modifier(Long id, CycleProductionDTO dto) {
        CycleProduction c = ent(dto);
        c.setId(id);
        c.setNombreVivants(calculerNombreVivants(dto.getNombreNaissances(), dto.getNombrePertes()));
        repo.save(c);
        return null;
    }

    public int calculerNombreVivants(Integer n, Integer p) {
        return (n == null ? 0 : n) - (p == null ? 0 : p);
    }

    public int calculerNombreVendables(Long id) {
        return repo.findById(id).map(c -> c.getNombreVendables() == null ? 0 : c.getNombreVendables()).orElse(0);
    }

    public BigDecimal calculerTauxPerte(Long id) {
        CycleProduction c = repo.findById(id).orElse(null);
        if (c == null || c.getNombreNaissances() == null || c.getNombreNaissances() == 0)
            return BigDecimal.ZERO;
        int pertes = c.getNombrePertes() == null ? 0 : c.getNombrePertes();
        return BigDecimal.valueOf(pertes).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(c.getNombreNaissances()), 2, RoundingMode.HALF_UP);
    }

    public void cloturerCycle(Long id, LocalDate dateFinReelle) {
        repo.findById(id).ifPresent(c -> {
            c.setDateFinReelle(dateFinReelle);
            c.setStatutCycle("Clôturé");
            repo.save(c);
        });
    }

    private CycleProduction ent(CycleProductionDTO d) {
        CycleProduction c = new CycleProduction();
        c.setId(d.getId());
        c.setCodeCycle(d.getCodeCycle());
        c.setLotPorcId(d.getLotPorcId());
        c.setDateDebut(d.getDateDebut());
        c.setDateFinPrevue(d.getDateFinPrevue());
        c.setDateFinReelle(d.getDateFinReelle());
        c.setNombreNaissances(d.getNombreNaissances());
        c.setNombrePertes(d.getNombrePertes());
        c.setNombreVivants(d.getNombreVivants());
        c.setNombreVendables(d.getNombreVendables());
        c.setStatutCycle(d.getStatutCycle());
        c.setObservation(d.getObservation());
        return c;
    }
}
