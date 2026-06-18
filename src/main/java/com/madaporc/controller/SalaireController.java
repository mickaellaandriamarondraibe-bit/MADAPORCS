package com.madaporc.controller;

import com.madaporc.DTO.SalaireEmployeDTO;
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
public class SalaireController {

    @GetMapping("/salaires")
    public String listSalaires(@RequestParam(required=false) Integer mois, @RequestParam(required=false) Integer annee, Model model) {
        model.addAttribute("titre", "Gestion des Salaires - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Salaires - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "SalaireController");
        model.addAttribute("methodName", "listSalaires");
        model.addAttribute("route", "/salaires");
        return "placeholder";
    }

  

}
