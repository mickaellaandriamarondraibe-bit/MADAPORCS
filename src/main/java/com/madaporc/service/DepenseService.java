package com.madaporc.service;

import com.madaporc.dto.DepenseDTO;
import com.madaporc.model.CategorieDepense;
import com.madaporc.model.Depense;
import com.madaporc.repository.CategorieDepenseRepository;
import com.madaporc.repository.DepenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DepenseService {

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private CategorieDepenseRepository categorieDepenseRepository;

    public List<Depense> findAllDepenses() {
        return depenseRepository.findAllByOrderByDateDepenseDesc();
    }

    public Depense findById(Long id) {
        return depenseRepository.findById(id).orElse(null);
    }

    public DepenseDTO getForm(Long id) {
        DepenseDTO dto = new DepenseDTO();
        if (id != null) {
            Depense depense = findById(id);
            if (depense != null) {
                dto.setId(depense.getId());
                dto.setDateDepense(depense.getDateDepense());
                dto.setMontant(depense.getMontant());
                dto.setDescription(depense.getDescription());
                if (depense.getCategorie() != null) {
                    dto.setCategorieId(depense.getCategorie().getId());
                }
            }
        }
        return dto;
    }

    public String enregistrerDepense(DepenseDTO dto) {
        String validation = validerDepense(dto);
        if (validation != null) {
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

    public String modifierDepense(Long id, DepenseDTO dto) {
        Depense depense = depenseRepository.findById(id).orElse(null);
        if (depense == null) {
            return "error";
        }

        String validation = validerDepense(dto);
        if (validation != null) {
            return "error";
        }

        depense.setDateDepense(dto.getDateDepense());
        depense.setMontant(dto.getMontant());
        depense.setDescription(dto.getDescription());

        if (dto.getCategorieId() != null) {
            CategorieDepense categorie = categorieDepenseRepository.findById(dto.getCategorieId()).orElse(null);
            depense.setCategorie(categorie);
        } else {
            depense.setCategorie(null);
        }

        depenseRepository.save(depense);
        return "redirect:/depenses";
    }

    public String validerDepense(DepenseDTO dto) {
        if (dto.getDateDepense() == null) {
            return "error";
        }
        if (dto.getMontant() == null) {
            return "error";
        }
        if (dto.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return "error";
        }
        return null;
    }

    public BigDecimal calculerTotalDepenses() {
        return depenseRepository.totalDepenses();
    }

    public BigDecimal calculerTotalDepenses(LocalDate debut, LocalDate fin) {
        return depenseRepository.totalDepensesEntre(debut, fin);
    }
}
