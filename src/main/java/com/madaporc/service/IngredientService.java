package com.madaporc.service;

import java.math.BigDecimal;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.IngredientDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.repository.IngredientRepository;

@Service
public class IngredientService {
    private final IngredientRepository repo;

    public IngredientService(IngredientRepository repo) {
        this.repo = repo;
    }

    public List<Ingredient> rechercherIngredients(String motCle) {
        return motCle == null || motCle.isBlank() ? repo.findAll() : repo.findByLibelleContainingIgnoreCase(motCle);
    }

    public String creer(IngredientDTO dto) {
        String e = validerIngredient(dto);
        if (e != null)
            return e;
        repo.save(conv(dto));
        return null;
    }

    public String modifier(Long id, IngredientDTO dto) {
        String e = validerIngredient(dto);
        if (e != null)
            return e;
        Ingredient i = conv(dto);
        i.setId(id);
        repo.save(i);
        return null;
    }

    public String desactiverIngredient(Long id) {
        repo.findById(id).ifPresent(i -> {
            i.setActif(false);
            repo.save(i);
        });
        return null;
    }

    public List<Ingredient> listerStocksFaibles() {
        return repo.findStocksFaibles();
    }

    public BigDecimal calculerValeurStock(Long id) {
        Ingredient i = repo.findById(id).orElse(null);
        if (i == null)
            return BigDecimal.ZERO;
        return i.getPrixKg().multiply(i.getStockActuelKg());
    }

    public String validerIngredient(IngredientDTO d) {
        if (d.getLibelle() == null || d.getLibelle().isBlank())
            return "Libellé obligatoire.";
        if (d.getPrixKg() == null || d.getPrixKg().compareTo(BigDecimal.ZERO) < 0)
            return "Prix invalide.";
        return null;
    }

    private Ingredient conv(IngredientDTO d) {
        Ingredient i = new Ingredient();
        i.setId(d.getId());
        i.setLibelle(d.getLibelle());
        i.setPrixKg(d.getPrixKg());
        i.setStockActuelKg(d.getStockActuelKg() == null ? BigDecimal.ZERO : d.getStockActuelKg());
        i.setSeuilMinKg(d.getSeuilMinKg() == null ? BigDecimal.ZERO : d.getSeuilMinKg());
        i.setUnite(d.getUnite() == null ? "kg" : d.getUnite());
        i.setActif(d.getActif() == null ? true : d.getActif());
        return i;
    }

    public void prepareIngredientFormModel(Model model, Long id) {
    IngredientDTO dto = new IngredientDTO();

    if (id != null) {
        repo.findById(id).ifPresent(ingredient -> {
            dto.setId(ingredient.getId());
            dto.setLibelle(ingredient.getLibelle());
            dto.setPrixKg(ingredient.getPrixKg());
            dto.setStockActuelKg(ingredient.getStockActuelKg());
            dto.setSeuilMinKg(ingredient.getSeuilMinKg());
            dto.setUnite(ingredient.getUnite());
            dto.setActif(ingredient.getActif());
        });
    }

    model.addAttribute("ingredient", dto);
}
}
