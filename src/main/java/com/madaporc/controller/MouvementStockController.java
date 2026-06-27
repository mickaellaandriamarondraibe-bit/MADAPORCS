package com.madaporc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.madaporc.service.MouvementStockService;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.madaporc.model.MouvementStockAliment;
import java.util.List;
import com.madaporc.model.Ingredient;
import com.madaporc.dto.MouvementStockDTO;

@Controller
@RequestMapping("/stocks/mouvements")
public class MouvementStockController {
    @Autowired
    private MouvementStockService mouvementStockService;

    @GetMapping("")
    public String listMouvements(Model model) {
        model.addAttribute("mouvements", mouvementStockService.getAllMouvementsStock());
        return "mouvements-list";
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        List<String> typeMouvements = List.of("ENTREE", "SORTIE");
        List<Ingredient> ingredients = mouvementStockService.getIngredientRepository().findAll();
        model.addAttribute("typeMouvements", typeMouvements);
        model.addAttribute("ingredients", ingredients);
        model.addAttribute("mouvement", new MouvementStockAliment());
        return "mouvements-form";
    }

    @PostMapping("/save")
    public String saveMouvement(MouvementStockAliment mouvementStock, Model model) {
        MouvementStockDTO mouvementStockDTO = mouvementStockService.convertirEnDTO(mouvementStock);
        String result = mouvementStockService.enregistrerMouvementStock(mouvementStockDTO);
        if (result.equals("success")) {
            return "redirect:/stocks/mouvements";
        } else {
            model.addAttribute("errorMessage", result);
            return "mouvements-form";
        }
    }
}