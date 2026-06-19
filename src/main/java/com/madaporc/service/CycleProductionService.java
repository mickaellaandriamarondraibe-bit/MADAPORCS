package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.CycleProductionDTO;
import com.madaporc.model.CycleProduction;
import com.madaporc.model.DetailVente;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.CycleProductionRepository;

@Service
public class CycleProductionService {
    private final CycleProductionRepository repo;

    public CycleProductionService(CycleProductionRepository repo) {
        this.repo = repo;
    }

    public List<CycleProduction> findAllCycles() {
        return repo.findAllByOrderByDateDebutDesc();
    }

    public CycleProductionDTO findById(Long id) {
        if (id == null)
            return new CycleProductionDTO();
        return repo.findById(id).map(this::convertToDTO).orElse(new CycleProductionDTO());
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
        if (id == null)
            return 0;
        return repo.findById(id).map(c -> c.getNombreVendables() == null ? 0 : c.getNombreVendables()).orElse(0);
    }

    public void synchroniserCycleAvecLot(LotPorc lot) {
        if (lot == null || lot.getId() == null)
            return;
        List<CycleProduction> cycles = repo.findByLotPorcIdAndStatutCycleNot(lot.getId(), "Clôturé");
        if (cycles == null || cycles.isEmpty())
            return;
        for (CycleProduction c : cycles) {
            if ("Naissance".equalsIgnoreCase(lot.getTypeEntree())) {
                c.setNombreNaissances(nz(lot.getNombreInitial()));
            }
            c.setNombrePertes(nz(lot.getNombreMorts()));
            c.setNombreVivants(nz(lot.getNombreActuel()));
            c.setNombreVendables(nz(lot.getNombreActuel()));
            repo.save(c);
        }
    }

    public void creerCycleDepuisLotSiAbsent(LotPorc lot) {
        if (lot == null || lot.getId() == null)
            return;
        if (!repo.findByLotPorcId(lot.getId()).isEmpty())
            return;
        CycleProduction cycle = new CycleProduction();
        cycle.setCodeCycle("CYCLE-" + lot.getId());
        cycle.setLotPorcId(lot.getId());
        if ("Achat".equalsIgnoreCase(lot.getTypeEntree())) {
            cycle.setDateDebut(lot.getDateAchat());
            cycle.setNombreNaissances(null);
        } else {
            cycle.setDateDebut(lot.getDateNaissanceEstimee());
            cycle.setNombreNaissances(nz(lot.getNombreInitial()));
        }
        cycle.setNombrePertes(nz(lot.getNombreMorts()));
        cycle.setNombreVivants(nz(lot.getNombreActuel()));
        cycle.setNombreVendables(nz(lot.getNombreActuel()));
        cycle.setStatutCycle("En cours");
        cycle.setObservation("Cycle créé automatiquement depuis le lot.");
        cycle.setCreatedAt(LocalDateTime.now());
        repo.save(cycle);
    }

    public void appliquerVenteAuCycle(DetailVente detail) {
        if (detail == null || detail.getLotPorcId() == null)
            return;
        int sold = detail.getNombrePorcsVendus() == null ? 0 : detail.getNombrePorcsVendus();
        if (sold <= 0)
            return;
        List<CycleProduction> cycles = repo.findByLotPorcIdAndStatutCycleNot(detail.getLotPorcId(), "Clôturé");
        if (cycles == null || cycles.isEmpty())
            return;
        for (CycleProduction c : cycles) {
            c.setNombreVivants(Math.max(0, nz(c.getNombreVivants()) - sold));
            c.setNombreVendables(Math.max(0, nz(c.getNombreVendables()) - sold));
            repo.save(c);
        }
    }

    private int nz(Integer value) {
        return value == null ? 0 : value;
    }

    public BigDecimal calculerTauxPerte(Long id) {
        if (id == null)
            return BigDecimal.ZERO;
        CycleProduction c = repo.findById(id).orElse(null);
        if (c == null || c.getNombreNaissances() == null || c.getNombreNaissances() == 0)
            return BigDecimal.ZERO;
        int pertes = c.getNombrePertes() == null ? 0 : c.getNombrePertes();
        return BigDecimal.valueOf(pertes).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(c.getNombreNaissances()), 2, RoundingMode.HALF_UP);
    }

    public void cloturerCycle(Long id, LocalDate dateFinReelle) {
        if (id == null)
            return;
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

    private CycleProductionDTO convertToDTO(CycleProduction c) {
        CycleProductionDTO dto = new CycleProductionDTO();
        dto.setId(c.getId());
        dto.setCodeCycle(c.getCodeCycle());
        dto.setLotPorcId(c.getLotPorcId());
        dto.setDateDebut(c.getDateDebut());
        dto.setDateFinPrevue(c.getDateFinPrevue());
        dto.setDateFinReelle(c.getDateFinReelle());
        dto.setNombreNaissances(c.getNombreNaissances());
        dto.setNombrePertes(c.getNombrePertes());
        dto.setNombreVivants(c.getNombreVivants());
        dto.setNombreVendables(c.getNombreVendables());
        dto.setStatutCycle(c.getStatutCycle());
        dto.setObservation(c.getObservation());
        return dto;
    }
}
