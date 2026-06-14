package com.madaporc.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.service.RapportService;

@Controller
@RequestMapping("/rapports")
public class RapportController {

    @Autowired
    private RapportService rapportService;

    @GetMapping
    public String rapports(
            @RequestParam(required = false) LocalDate debut,
            @RequestParam(required = false) LocalDate fin,
            Model model) {

        model.addAttribute(
                "rapportFinancier",
                rapportService.genererRapportFinancier(debut, fin));

        model.addAttribute(
                "rapportSanitaire",
                rapportService.genererRapportSanitaire(debut, fin));

        model.addAttribute(
                "rapportStock",
                rapportService.genererRapportStock());

        model.addAttribute(
                "rapportPresence",
                rapportService.genererRapportPresence(debut, fin));

        model.addAttribute(
                "rapportProduction",
                rapportService.genererRapportProduction(debut, fin));

        model.addAttribute("debut", debut);
        model.addAttribute("fin", fin);

        return "rapports/index";
    }

    @GetMapping("/export/pdf")
    public String exportPdf(
            @RequestParam(required = false) LocalDate debut,
            @RequestParam(required = false) LocalDate fin,
            Model model) {

        // génération PDF à implémenter

        return "redirect:/rapports";
    }

    @GetMapping("/export/excel")
    public String exportExcel(
            @RequestParam(required = false) LocalDate debut,
            @RequestParam(required = false) LocalDate fin,
            Model model) {

        // génération Excel à implémenter

        return "redirect:/rapports";
    }
}