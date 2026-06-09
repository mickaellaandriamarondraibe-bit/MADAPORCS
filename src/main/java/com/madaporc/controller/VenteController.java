package com.madaporc.controller;

import com.madaporc.DTO.VenteDTO;
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
public class VenteController {

    @GetMapping("/ventes")
    public String listVentes(Model model) {
        model.addAttribute("titre", "Gestion des Ventes - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Ventes - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VenteController");
        model.addAttribute("methodName", "listVentes");
        model.addAttribute("route", "/ventes");
        return "placeholder";
    }


}
