package com.madaporc.service;

import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.madaporc.DTO.DepenseDTO;
import com.madaporc.model.Depense;
import com.madaporc.repository.*;

@Service
public class DepenseService {
    private final DepenseRepository repo;
    private final CategorieDepenseRepository catRepo;

    public DepenseService(DepenseRepository repo, CategorieDepenseRepository catRepo) {
        this.repo = repo;
        this.catRepo = catRepo;
    }

    public List<Depense> rechercherDepenses(LocalDate d, LocalDate f, Long c) {
        if (c != null)
            return repo.findByCategorieDepenseId(c);
        if (d != null && f != null)
            return repo.findByDateDepenseBetween(d, f);
        return repo.findAll();
    }

    public String creer(DepenseDTO dto, Long uid) {
        String e = validerDepense(dto);
        if (e != null)
            return e;
        Depense x = conv(dto);
        x.setCreatedBy(uid);
        x.setCreatedAt(LocalDateTime.now());
        repo.save(x);
        return null;
    }

    public String modifier(Long id, DepenseDTO dto) {
        String e = validerDepense(dto);
        if (e != null)
            return e;
        Depense x = conv(dto);
        x.setId(id);
        repo.save(x);
        return null;
    }

    public BigDecimal calculerTotalDepenses(LocalDate d, LocalDate f) {
        return repo.sumMontantByDateBetween(d, f);
    }

    public String validerDepense(DepenseDTO dto) {
        if (dto.getCategorieDepenseId() == null)
            return "Catégorie obligatoire.";
        if (dto.getMontant() == null || dto.getMontant().compareTo(BigDecimal.ZERO) <= 0)
            return "Montant invalide.";
        return null;
    }

    public void prepareDepenseFormModel(Model m, Long id) {
        m.addAttribute("depense", new DepenseDTO());
        m.addAttribute("categories", catRepo.findAll());
    }

    private Depense conv(DepenseDTO d) {
        Depense x = new Depense();
        x.setId(d.getId());
        x.setCategorieDepenseId(d.getCategorieDepenseId());
        x.setLibelle(d.getLibelle());
        x.setMontant(d.getMontant());
        x.setDateDepense(d.getDateDepense());
        x.setDescription(d.getDescription());
        return x;
    }


    public List<Depense> findAllDepenses() {
    return repo.findAll();
}


}
