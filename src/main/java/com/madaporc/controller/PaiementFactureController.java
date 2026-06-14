package com.madaporc.controller;

import com.madaporc.DTO.PaiementDTO;
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
public class PaiementFactureController {

    @GetMapping("/factures/{venteId}")
    public String detailFacture(@PathVariable Long venteId, Model model) {
        model.addAttribute("titre", "Paiements et Factures - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Paiements et Factures - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "PaiementFactureController");
        model.addAttribute("methodName", "detailFacture");
        model.addAttribute("route", "/factures/{venteId}");
        return "placeholder";
    }

}
