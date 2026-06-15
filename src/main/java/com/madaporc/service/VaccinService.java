package com.madaporc.service;

import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.VaccinDTO;
import com.madaporc.model.Vaccin;
import com.madaporc.repository.*;

@Service
public class VaccinService {
    private final VaccinRepository repo;
    private final VaccinationRepository vaccinationRepo;

    public VaccinService(VaccinRepository repo, VaccinationRepository vaccinationRepo) {
        this.repo = repo;
        this.vaccinationRepo = vaccinationRepo;
    }

    public List<Vaccin> findAllVaccins() {
        return repo.findAll();
    }

    public String creer(VaccinDTO d) {
        repo.save(ent(d));
        return null;
    }

    public String modifier(Long id, VaccinDTO d) {
        Vaccin v = ent(d);
        v.setId(id);
        repo.save(v);
        return null;
    }

    public String desactiverVaccin(Long id) {
        repo.findById(id).ifPresent(v -> {
            v.setActif(false);
            repo.save(v);
        });
        return null;
    }

    public long compterVaccinsActifs() {
        return repo.findByActifTrue().size();
    }

    public BigDecimal calculerBudgetVaccins() {
        return repo.findAll().stream().map(v -> v.getPrix() == null ? BigDecimal.ZERO : v.getPrix())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long compterRappelsCritiques(LocalDate dateLimite) {
        return vaccinationRepo.countByDateRappelBefore(dateLimite);
    }

    private Vaccin ent(VaccinDTO d) {
        Vaccin v = new Vaccin();
        v.setId(d.getId());
        v.setLibelle(d.getLibelle());
        v.setPrix(d.getPrix());
        v.setDelaiRappelJours(d.getDelaiRappelJours());
        v.setDescription(d.getDescription());
        v.setActif(d.getActif() == null ? true : d.getActif());
        return v;
    }

    public void prepareVaccinFormModel(Model model, Long id) {
    VaccinDTO dto = new VaccinDTO();

    if (id != null) {
        repo.findById(id).ifPresent(vaccin -> {
            dto.setId(vaccin.getId());
            dto.setLibelle(vaccin.getLibelle());
            dto.setPrix(vaccin.getPrix());
            dto.setDelaiRappelJours(vaccin.getDelaiRappelJours());
            dto.setDescription(vaccin.getDescription());
            dto.setActif(vaccin.getActif());
        });
    }

    model.addAttribute("vaccin", dto);
}
}
