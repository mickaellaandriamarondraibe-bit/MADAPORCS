package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class IngredientController {

    @GetMapping("/ingredients")
    public String list(Model model) {
        model.addAttribute("titre", "Aliments et ingrédients");
        model.addAttribute("referenceFigma", "Aliments et ingrédients");
        model.addAttribute("message", "Aliments et ingrédients - list");
        return "placeholder";
    }

 

}
