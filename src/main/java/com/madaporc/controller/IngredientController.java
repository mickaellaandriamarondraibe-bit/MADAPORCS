package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.IngredientDTO;
import com.madaporc.service.IngredientService;

@Controller
@RequestMapping("/ingredients")
public class IngredientController {

    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public String liste(
            @RequestParam(required = false) String motCle,
            @RequestParam(required = false) Boolean actif,
            Model model
    ) {
        model.addAttribute("ingredients", ingredientService.rechercherIngredients(motCle));
        model.addAttribute("motCle", motCle);
        model.addAttribute("actif", actif);

        return "ingredients/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        ingredientService.prepareIngredientFormModel(model, id);
        return "ingredients/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("ingredient") IngredientDTO dto,
            Model model
    ) {
        String erreur = dto.getId() == null
                ? ingredientService.creer(dto)
                : ingredientService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("ingredient", dto);
            return "ingredients/form";
        }

        return "redirect:/ingredients";
    }

    @GetMapping("/desactiver/{id}")
    public String desactiver(@PathVariable Long id) {
        ingredientService.desactiverIngredient(id);
        return "redirect:/ingredients";
    }
}