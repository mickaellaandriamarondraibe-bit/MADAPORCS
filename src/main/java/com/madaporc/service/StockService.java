package com.madaporc.service;

import com.madaporc.dto.DepenseDTO;
import com.madaporc.model.CategorieDepense;
import com.madaporc.model.Depense;
import com.madaporc.repository.CategorieDepenseRepository;
import com.madaporc.repository.DepenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private CategorieDepenseRepository categorieDepenseRepository;

    public List<Depense> findAllDepenses() {
        return depenseRepository.findAllByOrderByDateDepenseDesc();
    }

    public String enregistrerDepense(DepenseDTO dto) {
        if (dto.getDateDepense() == null) {
            return "error";
        }
        if (dto.getMontant() == null) {
            return "error";
        }
        if (dto.getMontant().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            return "error";
        }

        Depense depense = new Depense();
        depense.setDateDepense(dto.getDateDepense());
        depense.setMontant(dto.getMontant());
        depense.setDescription(dto.getDescription());

        if (dto.getCategorieId() != null) {
            CategorieDepense categorie = categorieDepenseRepository.findById(dto.getCategorieId()).orElse(null);
            depense.setCategorie(categorie);
        }

        depenseRepository.save(depense);
        return "redirect:/depenses";
    }
}