package com.madaporc.controller;

import com.madaporc.DTO.IngredientDTO;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IngredientController {

    @GetMapping("/ingredients")
    public String listIngredients(@RequestParam(required=false) String motCle, Model model) {
        model.addAttribute("titre", "Gestion des Ingredients - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Ingredients - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "IngredientController");
        model.addAttribute("methodName", "listIngredients");
        model.addAttribute("route", "/ingredients");
        return "placeholder";
    }


}
