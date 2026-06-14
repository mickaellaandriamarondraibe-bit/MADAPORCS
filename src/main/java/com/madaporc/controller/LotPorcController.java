package com.madaporc.controller;

import com.madaporc.DTO.LotPorcDTO;
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
public class LotPorcController {

    @GetMapping("/lots")
    public String listLots(@RequestParam(required=false) String code, @RequestParam(required=false) Long raceId, @RequestParam(required=false) Long statutId, Model model) {
        model.addAttribute("titre", "Liste des Lots / Ajouter Lot / Detail Lot - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Liste des Lots / Ajouter Lot / Detail Lot - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "LotPorcController");
        model.addAttribute("methodName", "listLots");
        model.addAttribute("route", "/lots");
        return "placeholder";
    }

   

}
