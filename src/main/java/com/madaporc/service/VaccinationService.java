package com.madaporc.service;

import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.VaccinationDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class VaccinationService {
    private final VaccinationRepository repo;
    private final VaccinRepository vaccinRepo;

    public VaccinationService(VaccinationRepository repo, VaccinRepository vaccinRepo) {
        this.repo = repo;
        this.vaccinRepo = vaccinRepo;
    }

    public List<Vaccination> rechercherVaccinations(Long lotId, Long repId, LocalDate debut, LocalDate fin) {
        if (lotId != null)
            return repo.findByLotPorcId(lotId);
        if (repId != null)
            return repo.findByReproducteurId(repId);
        if (debut != null && fin != null)
            return repo.findByDateRappelBetween(debut, fin);
        return repo.findAll();
    }

    public String ajouter(VaccinationDTO d, Long uid) {
        String e = verifierCibleVaccination(d.getLotPorcId(), d.getReproducteurId());
        if (e != null)
            return e;
        Vaccination v = new Vaccination();
        v.setLotPorcId(d.getLotPorcId());
        v.setReproducteurId(d.getReproducteurId());
        v.setVaccinId(d.getVaccinId());
        v.setDateVaccination(d.getDateVaccination());
        v.setDateRappel(d.getDateRappel() == null ? calculerDateRappel(d.getVaccinId(), d.getDateVaccination())
                : d.getDateRappel());
        v.setDose(d.getDose());
        v.setObservation(d.getObservation());
        v.setCreatedBy(uid);
        v.setCreatedAt(LocalDateTime.now());
        repo.save(v);
        return null;
    }

    public LocalDate calculerDateRappel(Long vaccinId, LocalDate date) {
        Vaccin v = vaccinRepo.findById(vaccinId).orElse(null);
        return v == null || v.getDelaiRappelJours() == null ? date : date.plusDays(v.getDelaiRappelJours());
    }

    public String verifierCibleVaccination(Long lotId, Long repId) {
        return (lotId == null && repId == null) || (lotId != null && repId != null)
                ? "Choisir un lot ou un reproducteur."
                : null;
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate limite) {
        return repo.findByDateRappelBetween(LocalDate.now(), limite);
    }
}
