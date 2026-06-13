package com.madaporc.controller;

import com.madaporc.DTO.IngredientDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.repository.IngredientRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@Controller
public class IngredientController {

    private final IngredientRepository ingredientRepository;

    public IngredientController(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/ingredients")
    public String listIngredients(
            @RequestParam(value = "motCle", required = false) String motCle,
            Model model) {

        if (motCle != null && !motCle.trim().isEmpty()) {
            model.addAttribute("ingredients", ingredientRepository.findByLibelleContainingIgnoreCase(motCle.trim()));
        } else {
            model.addAttribute("ingredients", ingredientRepository.findAll());
        }

        model.addAttribute("motCle", motCle);

        return "ressources/ingredients";
    }

    @GetMapping("/ingredients/form")
    public String showForm(@RequestParam(value = "id", required = false) Long id,Model model) {

        IngredientDTO dto = new IngredientDTO();

        if (id != null) {

            Optional<Ingredient> ingredientOpt =
                    ingredientRepository.findById(id);

            if (ingredientOpt.isPresent()) {

                Ingredient ingredient = ingredientOpt.get();

                dto.setId(ingredient.getId());
                dto.setLibelle(ingredient.getLibelle());
                dto.setPrixKg(ingredient.getPrixKg());
                dto.setStockActuelKg(ingredient.getStockActuelKg());
                dto.setSeuilMinKg(ingredient.getSeuilMinKg());
                dto.setUnite(ingredient.getUnite());
                dto.setActif(ingredient.getActif());
            }

        } else {

            dto.setActif(true);
            dto.setStockActuelKg(BigDecimal.ZERO);
            dto.setSeuilMinKg(BigDecimal.ZERO);
            dto.setUnite("kg");
        }

        model.addAttribute("ingredient", dto);

        return "ressources/formIngredient";
    }

    @PostMapping("/ingredients/save")
    public String saveIngredient(@ModelAttribute IngredientDTO dto,Model model) {

        Ingredient ingredient;

        if (dto.getId() != null) {

            Optional<Ingredient> ingredientOpt = ingredientRepository.findById(dto.getId());

            if (ingredientOpt.isEmpty()) {

                model.addAttribute("message","Ingredient introuvable.");
                model.addAttribute("ingredient", dto);

                return "ressources/formIngredient";
            }

            ingredient = ingredientOpt.get();

        } else {

            ingredient = new Ingredient();
        }

        ingredient.setLibelle(dto.getLibelle());
        ingredient.setPrixKg(dto.getPrixKg());

        ingredient.setStockActuelKg(
                dto.getStockActuelKg() != null
                        ? dto.getStockActuelKg()
                        : BigDecimal.ZERO);

        ingredient.setSeuilMinKg(
                dto.getSeuilMinKg() != null
                        ? dto.getSeuilMinKg()
                        : BigDecimal.ZERO);

        ingredient.setUnite(
                dto.getUnite() != null
                        ? dto.getUnite()
                        : "kg");

        ingredient.setActif(
                dto.getActif() != null
                        ? dto.getActif()
                        : true);

        ingredientRepository.save(ingredient);

        return "redirect:/ingredients";
    }

    @PostMapping("/ingredients/delete/{id}")
    public String deleteIngredient(@PathVariable Long id) {

        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
        }

        return "redirect:/ingredients";
    }
}