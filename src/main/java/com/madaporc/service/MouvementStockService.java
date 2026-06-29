package com.madaporc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.repository.MouvementStockRepository;

import jakarta.transaction.Transactional;

import com.madaporc.model.MouvementStockAliment;
import com.madaporc.model.enums.TypeMouvement;

import java.util.List;
import java.math.BigDecimal;
import com.madaporc.model.Ingredient;
import com.madaporc.repository.IngredientRepository;

import com.madaporc.dto.MouvementStockDTO;

@Service
public class MouvementStockService {
    @Autowired
    private MouvementStockRepository mouvementStockRepository;
    @Autowired
    private Ingredient ingredient;
    @Autowired
    private IngredientRepository ingredientRepository;

    @Transactional(readOnly = true)
    public List<MouvementStockAliment> getAllMouvementsStock() {
        return mouvementStockRepository.findAllByOrderDateMouvementDesc();
    }

    public IngredientRepository getIngredientRepository() {
        return ingredientRepository;
    }

    @Transactional
    public String enregistrerMouvementStock(MouvementStockDTO mouvementStockDTO) {
        String validationMessage = validerMouvementStock(mouvementStockDTO);
        if (!validationMessage.equals("success")) {
            return validationMessage;
        }

        MouvementStockAliment mouvementStock = new MouvementStockAliment();
        Ingredient ingredient = ingredientRepository.findById(mouvementStockDTO.getIngredientId()).orElse(null);
        
        if (ingredient == null) {
            return "Ingrédient non trouvé.";
        }

        if( mouvementStockDTO.getTypeMouvement().equals(TypeMouvement.ENTREE.toString()) ) {
            appliquerEntree(ingredient, mouvementStockDTO.getQuantite());
        } 
        else if( mouvementStockDTO.getTypeMouvement().equals(TypeMouvement.SORTIE.toString()) ) {
            String sortieResult = appliquerSortie(ingredient, mouvementStockDTO.getQuantite());
            if (!sortieResult.equals("success")) {
                return sortieResult;
            }
        }

        mouvementStock.setIngredient(ingredient);
        mouvementStock.setTypeMouvement(TypeMouvement.valueOf(mouvementStockDTO.getTypeMouvement()));
        mouvementStock.setQuantite(mouvementStockDTO.getQuantite());
        mouvementStock.setDateMouvement(mouvementStockDTO.getDateMouvement());
        mouvementStock.setObservation(mouvementStockDTO.getObservation());
        mouvementStockRepository.save(mouvementStock);

        return "success";
    }

    public String validerMouvementStock(MouvementStockDTO mouvementStockDTO) {
        String statut = mouvementStockDTO.getTypeMouvement();
        
        if (statut.equals(TypeMouvement.ENTREE.toString())) {
            if (mouvementStockDTO.getQuantite().compareTo(BigDecimal.ZERO) <= 0) {
                return "La quantité d'entrée doit être supérieure à zéro.";
            }
        } else if (statut.equals(TypeMouvement.SORTIE.toString())) {
            if (!verifierStockDisponible(mouvementStockDTO.getIngredientId(), mouvementStockDTO.getQuantite())) {
                return "Stock insuffisant pour effectuer la sortie.";
            }
        }

        return "success";
    }

    public boolean verifierStockDisponible(Long ingredientId, BigDecimal quantite) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        
        if (ingredient != null) {
            BigDecimal stockDisponible = ingredient.getStockActuel();
            return stockDisponible.compareTo(quantite) >= 0;
        }

        return false;
    }

    public MouvementStockDTO convertirEnDTO(MouvementStockAliment mouvementStock) {
        MouvementStockDTO dto = new MouvementStockDTO();
        dto.setId(mouvementStock.getId());
        dto.setIngredientId(mouvementStock.getIngredient().getId());
        dto.setTypeMouvement(mouvementStock.getTypeMouvement().toString());
        dto.setQuantite(mouvementStock.getQuantite());
        dto.setDateMouvement(mouvementStock.getDateMouvement());
        dto.setObservation(mouvementStock.getObservation());
        return dto;
    }

    public String appliquerEntree(Ingredient ingredient, BigDecimal quantite) {
        BigDecimal stockActuel = ingredient.getStockActuel();
        BigDecimal nouveauStock = stockActuel.add(quantite);
        ingredient.setStockActuel(nouveauStock);
        ingredientRepository.save(ingredient);

        return "success";
    }

    public String appliquerSortie(Ingredient ingredient, BigDecimal quantite) {
        BigDecimal stockActuel = ingredient.getStockActuel();
        
        if (!verifierStockDisponible(ingredient.getId(), quantite)) {
            return "Stock insuffisant pour effectuer la sortie.";
        }

        BigDecimal nouveauStock = stockActuel.subtract(quantite);
        ingredient.setStockActuel(nouveauStock);
        ingredientRepository.save(ingredient);

        return "success";
    }
}