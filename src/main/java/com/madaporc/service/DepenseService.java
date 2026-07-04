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

    // Methode reutilisable : cree et enregistre une depense dans la table depenses.
    // Appelee par tous les modules qui depensent de l'argent (achat lot, ingredient, vaccination...).
    // Retourne la depense creee, ou null si le montant est absent/nul.
    public Depense creerDepense(BigDecimal montant, String description, LocalDate dateDepense, String nomCategorie) {
        if (montant == null || montant.compareTo(BigDecimal.ZERO) <= 0) {
            return null;
        }

        Depense depense = new Depense();
        depense.setMontant(montant);
        depense.setDescription(description);
        depense.setDateDepense(dateDepense != null ? dateDepense : LocalDate.now());
        depense.setCategorie(trouverOuCreerCategorie(nomCategorie));

        return depenseRepository.save(depense);
    }

    // Retrouve une categorie par son nom, la cree si elle n'existe pas encore.
    private CategorieDepense trouverOuCreerCategorie(String nom) {
        if (nom == null || nom.trim().isEmpty()) {
            return null;
        }

        return categorieDepenseRepository.findFirstByNomIgnoreCase(nom.trim())
                .orElseGet(() -> {
                    CategorieDepense categorie = new CategorieDepense();
                    categorie.setNom(nom.trim());
                    return categorieDepenseRepository.save(categorie);
                });
    }

    public BigDecimal calculerTotalDepenses() {
        return depenseRepository.totalDepenses();
    }

    public BigDecimal calculerTotalDepenses(LocalDate debut, LocalDate fin) {
        return depenseRepository.totalDepensesEntre(debut, fin);
    }

    // Recherche avec filtres optionnels (date debut, date fin, categorie).
    public List<Depense> rechercher(LocalDate debut, LocalDate fin, Long categorieId) {
        return depenseRepository.findAllByOrderByDateDepenseDesc().stream()
                .filter(d -> debut == null || !d.getDateDepense().isBefore(debut))
                .filter(d -> fin == null || !d.getDateDepense().isAfter(fin))
                .filter(d -> categorieId == null
                        || (d.getCategorie() != null && categorieId.equals(d.getCategorie().getId())))
                .toList();
    }

    // Total d'une liste de depenses deja filtree.
    public BigDecimal total(List<Depense> depenses) {
        return depenses.stream()
                .map(Depense::getMontant)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
