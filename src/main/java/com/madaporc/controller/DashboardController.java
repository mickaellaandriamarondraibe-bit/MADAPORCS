package com.madaporc.controller;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.service.DashboardService;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/dashboard")
    public String index(@RequestParam(name = "mois", required = false) String moisParam, Model model) {
        // Mois choisi (format "AAAA-MM"), par defaut le mois courant.
        YearMonth mois;
        try {
            mois = (moisParam != null && !moisParam.isBlank()) ? YearMonth.parse(moisParam) : YearMonth.now();
        } catch (DateTimeParseException e) {
            mois = YearMonth.now();
        }
        // Date de reference = fin du mois choisi, plafonnee a aujourd'hui.
        LocalDate aujourdHui = LocalDate.now();
        LocalDate asOf = mois.atEndOfMonth().isAfter(aujourdHui) ? aujourdHui : mois.atEndOfMonth();

        model.addAttribute("dashboard", dashboardService.getDashboard(mois));
        model.addAttribute("misesBasProches", dashboardService.listerMisesBasProches(asOf));
        model.addAttribute("dateJour", asOf);
        model.addAttribute("moisSelectionne", mois.toString());
        return "dashboard/index";
    }

    // Comparaison entre deux mois : on reutilise getDashboard(mois) pour chacun.
    @GetMapping("/dashboard/comparaison")
    public String comparaison(@RequestParam(name = "mois1", required = false) String m1,
                              @RequestParam(name = "mois2", required = false) String m2, Model model) {
        YearMonth mois1 = parseMois(m1, YearMonth.now().minusMonths(1));
        YearMonth mois2 = parseMois(m2, YearMonth.now());
        model.addAttribute("d1", dashboardService.getDashboard(mois1));
        model.addAttribute("d2", dashboardService.getDashboard(mois2));
        model.addAttribute("mois1", mois1.toString());
        model.addAttribute("mois2", mois2.toString());
        return "dashboard/comparaison";
    }

    private static YearMonth parseMois(String s, YearMonth defaut) {
        try {
            return (s != null && !s.isBlank()) ? YearMonth.parse(s) : defaut;
        } catch (DateTimeParseException e) {
            return defaut;
        }
    }
}
