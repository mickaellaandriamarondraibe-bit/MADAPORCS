package com.madaporc.controller;

import com.madaporc.DTO.VaccinDTO;
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
public class VaccinController {

    @GetMapping("/vaccins")
    public String listVaccins(Model model) {
        model.addAttribute("titre", "Gestion des Vaccins - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Vaccins - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinController");
        model.addAttribute("methodName", "listVaccins");
        model.addAttribute("route", "/vaccins");
        return "placeholder";
    }

}
