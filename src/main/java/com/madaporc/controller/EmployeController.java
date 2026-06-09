package com.madaporc.controller;

import com.madaporc.DTO.EmployeDTO;
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
public class EmployeController {

    @GetMapping("/employes")
    public String listEmployes(@RequestParam(required=false) String motCle, Model model) {
        model.addAttribute("titre", "Gestion des Employes - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Employes - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "EmployeController");
        model.addAttribute("methodName", "listEmployes");
        model.addAttribute("route", "/employes");
        return "placeholder";
    }

}
