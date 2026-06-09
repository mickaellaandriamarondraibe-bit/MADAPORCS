package com.madaporc.controller;

import com.madaporc.DTO.DistributionAlimentDTO;
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
public class DistributionAlimentController {

    @GetMapping("/distributions")
    public String listDistributions(Model model) {
        model.addAttribute("titre", "Distribution des Aliments - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Distribution des Aliments - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "DistributionAlimentController");
        model.addAttribute("methodName", "listDistributions");
        model.addAttribute("route", "/distributions");
        return "placeholder";
    }


}
