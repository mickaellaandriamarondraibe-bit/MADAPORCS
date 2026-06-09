package com.madaporc.controller;

import com.madaporc.DTO.VaccinationDTO;
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
public class VaccinationController {

    @GetMapping("/vaccinations")
    public String listVaccinations(@RequestParam(required=false) Long lotId, @RequestParam(required=false) Long reproducteurId, Model model) {
        model.addAttribute("titre", "Journal des Vaccinations - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Journal des Vaccinations - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinationController");
        model.addAttribute("methodName", "listVaccinations");
        model.addAttribute("route", "/vaccinations");
        return "placeholder";
    }


}
