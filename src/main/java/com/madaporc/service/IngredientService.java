package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.dto.IngredientDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.repository.IngredientRepository;

@Service
public class IngredientService {
    
    private final IngredientRepository ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public List<Ingredient> findAllIngredients() {
        return ingredientRepository.findAll();
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
        if (dto == null) {
            return "dto is null";
        }
        if (dto.getNom() == null || dto.getNom().isEmpty() || dto.getStockActuel().compareTo(BigDecimal.ZERO) < 0 || dto.getSeuilAlerte().compareTo(BigDecimal.ZERO) < 0
                || dto.getUnite().trim().isEmpty()) {
            return "Please fill in all fields with valid values";
        }
        Ingredient ingredient = new Ingredient();
        ingredient.setNom(dto.getNom());
        ingredient.setUnite(dto.getUnite());
        ingredient.setStockActuel(dto.getStockActuel());
        ingredient.setSeuilAlerte(dto.getSeuilAlerte());
        ingredient.setCreatedAt(LocalDateTime.now());
        ingredient.setUpdatedAt(LocalDateTime.now());
        ingredientRepository.save(ingredient);
        return "Ingredient created successfully";
    }

    public String modifierIngredient(Long id, IngredientDTO dto) {
        
        if (dto == null) {
            return "dto is null";
        }
        if (dto.getNom() == null || dto.getNom().trim().isEmpty() || dto.getStockActuel().compareTo(BigDecimal.ZERO) < 0 || dto.getSeuilAlerte().compareTo(BigDecimal.ZERO) < 0) {
            return "Please fill in all fields with valid values";
        }
        Ingredient ingredient = findById(id);
        if (ingredient != null) {
            ingredient.setNom(dto.getNom());
            ingredient.setUnite(dto.getUnite());
            ingredient.setStockActuel(dto.getStockActuel());
            ingredient.setSeuilAlerte(dto.getSeuilAlerte());
            ingredientRepository.save(ingredient);
            return "Ingredient modified successfully";
        }
        return "Ingredient not found";
    }

    public String validerIngredient(IngredientDTO dto) {
        // Implementation for validating an ingredient
        return "Ingredient validated successfully";
    }

    boolean estStockFaible(Ingredient ingredient) {
        return ingredient.getStockActuel().compareTo(ingredient.getSeuilAlerte()) <= 0;
    }
    
    List<Ingredient> listerStocksFaibles(){
        return ingredientRepository.findStocksFaibles();
    }
}