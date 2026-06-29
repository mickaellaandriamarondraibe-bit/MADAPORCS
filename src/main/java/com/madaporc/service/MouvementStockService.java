package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.dto.MouvementStockDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.MouvementStock;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.MouvementStockRepository;

@Service
public class MouvementStockService {

    private final MouvementStockRepository mouvementRepository;
    private final IngredientRepository ingredientRepository;

    public MouvementStockService(MouvementStockRepository mouvementRepository, IngredientRepository ingredientRepository) {
        this.mouvementRepository = mouvementRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // liste de tous les mouvements (le plus recent en premier)
    public List<MouvementStock> tousLesMouvements() {
        return mouvementRepository.findAllByOrderByIdDesc();
    }

    // methode principale : on valide puis on applique le mouvement
    public String enregistrerMouvementStock(MouvementStockDTO dto) {
        String erreur = validerMouvementStock(dto);
        if (erreur != null) {
            return erreur;
        }
        Ingredient ingredient = ingredientRepository.findById(dto.getIngredientId()).orElse(null);
        if (ingredient == null) {
            return "Ingrédient introuvable";
        }
        if (dto.getTypeMouvement().equals("ENTREE")) {
            return appliquerEntree(ingredient, dto.getQuantite());
        } else {
            return appliquerSortie(ingredient, dto.getQuantite());
        }
    }

    // verifie que le formulaire est bien rempli (retourne null si tout est ok)
    public String validerMouvementStock(MouvementStockDTO dto) {
        if (dto == null) {
            return "dto is null";
        }
        if (dto.getIngredientId() == null
                || dto.getTypeMouvement() == null || dto.getTypeMouvement().trim().isEmpty()
                || dto.getQuantite() == null || dto.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
            return "Veuillez remplir tous les champs correctement";
        }
        return null;
    }

    // verifie qu'il y a assez de stock pour une sortie
    public boolean verifierStockDisponible(Long ingredientId, BigDecimal quantite) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        if (ingredient == null) {
            return false;
        }
        return ingredient.getStockActuel().compareTo(quantite) >= 0;
    }

    // ENTREE : on ajoute au stock
    public String appliquerEntree(Ingredient ingredient, BigDecimal quantite) {
        BigDecimal nouveauStock = ingredient.getStockActuel().add(quantite);
        ingredient.setStockActuel(nouveauStock);
        ingredientRepository.save(ingredient);
        enregistrerLigne(ingredient, "ENTREE", quantite, nouveauStock);
        return "Mouvement enregistré";
    }

    // SORTIE : on retire du stock (si possible)
    public String appliquerSortie(Ingredient ingredient, BigDecimal quantite) {
        if (!verifierStockDisponible(ingredient.getId(), quantite)) {
            return "Stock insuffisant pour cette sortie";
        }
        BigDecimal nouveauStock = ingredient.getStockActuel().subtract(quantite);
        ingredient.setStockActuel(nouveauStock);
        ingredientRepository.save(ingredient);
        enregistrerLigne(ingredient, "SORTIE", quantite, nouveauStock);
        return "Mouvement enregistré";
    }

    // garde une trace du mouvement dans la table
    private void enregistrerLigne(Ingredient ingredient, String type, BigDecimal quantite, BigDecimal stockApres) {
        MouvementStock mouvement = new MouvementStock();
        mouvement.setIngredient(ingredient);
        mouvement.setTypeMouvement(type);
        mouvement.setQuantite(quantite);
        mouvement.setDateMouvement(LocalDate.now());
        mouvement.setStockApres(stockApres);
        mouvementRepository.save(mouvement);
    }
}
