package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.madaporc.dto.RapportFiltreDTO;
import com.madaporc.dto.RapportFinancierDTO;
import com.madaporc.service.RapportService;

@Controller
public class RapportController {

    private final RapportService rapportService;

    public RapportController(RapportService rapportService) {
        this.rapportService = rapportService;
    }

    // Page Rapports : affiche le rapport financier filtre par date (debut / fin).
    @GetMapping("/rapports")
    public String index(@ModelAttribute RapportFiltreDTO filtre, Model model) {
        model.addAttribute("filtre", filtre);
        try {
            RapportFinancierDTO rapport = rapportService.genererRapport(filtre);
            model.addAttribute("rapport", rapport);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erreur", e.getMessage());
            model.addAttribute("rapport", new RapportFinancierDTO());
        }
        return "rapports/index";
    }
}
