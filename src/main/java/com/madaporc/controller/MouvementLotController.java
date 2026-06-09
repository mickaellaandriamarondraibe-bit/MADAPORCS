package com.madaporc.controller;

import com.madaporc.DTO.MouvementLotDTO;
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
public class MouvementLotController {

    @GetMapping("/lots/{id}/mouvements")
    public String listMouvements(@PathVariable Long id, Model model) {
        model.addAttribute("titre", "Detail du Lot - Onglet Mouvements / Liste des Lots - Actions");
        model.addAttribute("referenceFigma", "Detail du Lot - Onglet Mouvements / Liste des Lots - Actions");
        model.addAttribute("controllerName", "MouvementLotController");
        model.addAttribute("methodName", "listMouvements");
        model.addAttribute("route", "/lots/{id}/mouvements");
        return "placeholder";
    }

  

}
