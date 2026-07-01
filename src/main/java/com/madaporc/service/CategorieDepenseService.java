package com.madaporc.service;

import com.madaporc.dto.CategorieDepenseDTO;
import com.madaporc.model.CategorieDepense;
import com.madaporc.repository.CategorieDepenseRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CategorieDepenseService {

    private final CategorieDepenseRepository categorieDepenseRepository;

    public List<CategorieDepense> findAllCategories() {
        return categorieDepenseRepository.findAllByOrderByNomAsc();
    }

    public CategorieDepense findById(Long id) {
        return categorieDepenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Catégorie de dépense non trouvée"));
    }

    public String creerCategorie(CategorieDepenseDTO dto) {
        String erreur = validerCategorie(dto);
        if (erreur != null) {
            return erreur;
        }
        CategorieDepense categorie = new CategorieDepense();
        categorie.setNom(dto.getNom().trim());
        categorieDepenseRepository.save(categorie);
        return "Catégorie créée avec succès";
    }

    public String modifierCategorie(Long id, CategorieDepenseDTO dto) {
        CategorieDepense categorie = findById(id);
        String nomTrimmed = dto.getNom() != null ? dto.getNom().trim() : "";
        if (!categorie.getNom().equalsIgnoreCase(nomTrimmed)) {
            if (categorieDepenseRepository.existsByNomIgnoreCase(nomTrimmed)) {
                return "Une catégorie avec ce nom existe déjà";
            }
        }
        categorie.setNom(nomTrimmed);
        categorieDepenseRepository.save(categorie);
        return "Catégorie modifiée avec succès";
    }

    public String validerCategorie(CategorieDepenseDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            return "Le nom de la catégorie est obligatoire";
        }
        if (categorieDepenseRepository.existsByNomIgnoreCase(dto.getNom().trim())) {
            return "Une catégorie avec ce nom existe déjà";
        }
        return null;
    }
}
