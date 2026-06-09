package com.madaporc.controller;

import com.madaporc.DTO.ReproducteurDTO;
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
public class ReproducteurController {

    @GetMapping("/reproducteurs")
    public String listReproducteurs(@RequestParam(required=false) String motCle, @RequestParam(required=false) Long sexeId, @RequestParam(required=false) Long statutId, Model model) {
        model.addAttribute("titre", "Liste des Reproducteurs / Detail du Reproducteur - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Liste des Reproducteurs / Detail du Reproducteur - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ReproducteurController");
        model.addAttribute("methodName", "listReproducteurs");
        model.addAttribute("route", "/reproducteurs");
        return "placeholder";
    }


}
