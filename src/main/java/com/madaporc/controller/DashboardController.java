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
public class DashboardController {

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        model.addAttribute("titre", "Tableau de bord - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Tableau de bord - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "DashboardController");
        model.addAttribute("methodName", "dashboard");
        model.addAttribute("route", "/dashboard");
        return "placeholder";
    }

}
