package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.madaporc.dto.MouvementStockDTO;
import com.madaporc.service.IngredientService;
import com.madaporc.service.MouvementStockService;

@Controller
public class MouvementStockController {

    private final MouvementStockService mouvementService;
    private final IngredientService ingredientService;

    public MouvementStockController(MouvementStockService mouvementService, IngredientService ingredientService) {
        this.mouvementService = mouvementService;
        this.ingredientService = ingredientService;
    }

    // page liste des mouvements
    @GetMapping("/stocks/mouvements")
    public String liste(Model model) {
        model.addAttribute("mouvements", mouvementService.tousLesMouvements());
        return "stocks/mouvements";
    }

    // page formulaire (besoin de la liste des ingredients pour le menu deroulant)
    @GetMapping("/stocks/mouvements/form")
    public String form(Model model) {
        model.addAttribute("ingredients", ingredientService.findAllIngredients());
        return "stocks/formMouvement";
    }

    // enregistrement du formulaire
    @PostMapping("/stocks/mouvements/save")
    public String save(@ModelAttribute MouvementStockDTO dto, RedirectAttributes redirectAttributes) {
        String message = mouvementService.enregistrerMouvementStock(dto);
        redirectAttributes.addFlashAttribute("message", message);
        return "redirect:/stocks/mouvements";
    }
}
