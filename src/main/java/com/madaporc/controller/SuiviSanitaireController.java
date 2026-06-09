package com.madaporc.controller;

import com.madaporc.DTO.SuiviSanitaireDTO;
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
public class SuiviSanitaireController {

    @GetMapping("/sante/suivis")
    public String listSuivis(@RequestParam(required=false) Long statutId, Model model) {
        model.addAttribute("titre", "Suivi Sanitaire - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Suivi Sanitaire - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "SuiviSanitaireController");
        model.addAttribute("methodName", "listSuivis");
        model.addAttribute("route", "/sante/suivis");
        return "placeholder";
    }


}
