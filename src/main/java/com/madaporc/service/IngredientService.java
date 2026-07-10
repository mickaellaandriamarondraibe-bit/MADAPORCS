package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.dto.IngredientDTO;
import com.madaporc.dto.MouvementStockDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.MouvementStock;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
import com.madaporc.repository.MouvementStockRepository;

@Service
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    private final MouvementStockRepository mouvementStockRepository;
    private final MouvementStockService mouvementStockService;
    public IngredientService(IngredientRepository ingredientRepository , MouvementStockRepository mouvementStockRepository,
            MouvementStockService mouvementStockService) {
        this.ingredientRepository = ingredientRepository;
        this.mouvementStockRepository = mouvementStockRepository;
        this.mouvementStockService = mouvementStockService;
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAllByOrderByNomAsc();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id).orElse(null);
    }

    public IngredientDTO getForm(Long id) {
        Ingredient ingredient = findById(id);
        if (ingredient != null) {
            IngredientDTO dto = new IngredientDTO();
            dto.setId(ingredient.getId());
            dto.setNom(ingredient.getNom());
            dto.setUnite(ingredient.getUnite());
            dto.setStockActuel(ingredient.getStockActuel());
            dto.setSeuilAlerte(ingredient.getSeuilAlerte());
            return dto;
        }
        return null;
    }

    public String creerIngredient(IngredientDTO dto) {
        String erreur = validerDto(dto);
        if (erreur != null) {
            return erreur;
        }
        if (ingredientRepository.existsByNomIgnoreCase(dto.getNom().trim())) {
            return "Un ingrédient avec ce nom existe déjà";
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setNom(dto.getNom().trim());
        ingredient.setUnite(dto.getUnite());
        // Le stock démarre à zéro : le mouvement ENTREE ci-dessous l'amène au stock saisi
        // (sinon le stock initial serait compté deux fois).
        ingredient.setStockActuel(BigDecimal.ZERO);
        ingredient.setSeuilAlerte(dto.getSeuilAlerte());
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setUpdatedAt(LocalDateTime.now());

        
        ingredientRepository.save(ingredient);
        
        MouvementStockDTO mouvement = new MouvementStockDTO();
        mouvement.setIngredientId(ingredientRepository.findFirstByOrderByCreatedAtDesc().getId());
        mouvement.setQuantite(dto.getStockActuel());
        mouvement.setTypeMouvement("ENTREE");

        
        mouvementStockService.enregistrerMouvementStock(mouvement);

        
        return "Ingredient created successfully";
    }

    public String modifierIngredient(Long id, IngredientDTO dto) {
        String erreur = validerDto(dto);
        if (erreur != null) {
            return erreur;
        }
        Ingredient ingredient = findById(id);
        if (ingredient != null) {
            ingredient.setNom(dto.getNom().trim());
            ingredient.setUnite(dto.getUnite());
            ingredient.setStockActuel(dto.getStockActuel());
            ingredient.setSeuilAlerte(dto.getSeuilAlerte());
            ingredient.setUpdatedAt(LocalDateTime.now());
            ingredientRepository.save(ingredient);
            return "Ingredient modified successfully";
        }
        return "Ingredient not found";
    }

    private String validerDto(IngredientDTO dto) {
        if (dto == null) {
            return "dto is null";
        }
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()
                || dto.getUnite() == null || dto.getUnite().trim().isEmpty()
                || dto.getStockActuel() == null || dto.getStockActuel().compareTo(BigDecimal.ZERO) < 0
                || dto.getSeuilAlerte() == null || dto.getSeuilAlerte().compareTo(BigDecimal.ZERO) < 0) {
            return "Please fill in all fields with valid values";
        }
        return null;
    }

    boolean estStockFaible(Ingredient ingredient) {
        return ingredient.getStockActuel().compareTo(ingredient.getSeuilAlerte()) <= 0;
    }
    
    List<Ingredient> listerStocksFaibles(){
        return ingredientRepository.findStocksFaibles();
    }
}