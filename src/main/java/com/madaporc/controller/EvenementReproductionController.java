package com.madaporc.controller;

import com.madaporc.DTO.EvenementReproductionDTO;
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
public class EvenementReproductionController {

    @GetMapping("/reproduction/evenements")
    public String listEvenements(@RequestParam(required=false) Long typeId, Model model) {
        model.addAttribute("titre", "Evenements de Reproduction - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Evenements de Reproduction - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "EvenementReproductionController");
        model.addAttribute("methodName", "listEvenements");
        model.addAttribute("route", "/reproduction/evenements");
        return "placeholder";
    }

   

}
