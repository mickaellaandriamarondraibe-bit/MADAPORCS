package com.madaporc.controller;

import com.madaporc.DTO.PeseeLotDTO;
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
public class PeseeLotController {

    @GetMapping("/lots/{id}/pesees")
    public String listPesees(@PathVariable Long id, Model model) {
        model.addAttribute("titre", "Detail du Lot - Evolution du poids / Onglet Pesees");
        model.addAttribute("referenceFigma", "Detail du Lot - Evolution du poids / Onglet Pesees");
        model.addAttribute("controllerName", "PeseeLotController");
        model.addAttribute("methodName", "listPesees");
        model.addAttribute("route", "/lots/{id}/pesees");
        return "placeholder";
    }



}
