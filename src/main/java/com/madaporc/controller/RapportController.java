package com.madaporc.controller;

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
public class RapportController {

    @GetMapping("/rapports")
    public String rapports(Model model) {
        model.addAttribute("titre", "Analyses & Rapports - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Analyses & Rapports - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "RapportController");
        model.addAttribute("methodName", "rapports");
        model.addAttribute("route", "/rapports");
        return "placeholder";
    }

}
