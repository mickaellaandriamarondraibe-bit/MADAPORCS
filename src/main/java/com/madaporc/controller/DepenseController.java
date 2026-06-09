package com.madaporc.controller;

import com.madaporc.DTO.DepenseDTO;
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
public class DepenseController {

    @GetMapping("/depenses")
    public String listDepenses(@RequestParam(required=false) LocalDate debut, @RequestParam(required=false) LocalDate fin, @RequestParam(required=false) Long categorieId, Model model) {
        model.addAttribute("titre", "Analyses & Rapports - Depenses / Module Commerce-Finance");
        model.addAttribute("referenceFigma", "Analyses & Rapports - Depenses / Module Commerce-Finance");
        model.addAttribute("controllerName", "DepenseController");
        model.addAttribute("methodName", "listDepenses");
        model.addAttribute("route", "/depenses");
        return "placeholder";
    }


}
