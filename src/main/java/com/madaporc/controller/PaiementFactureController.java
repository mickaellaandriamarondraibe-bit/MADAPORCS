package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PaiementFactureController {

    @GetMapping("/paiements-factures")
    public String list(Model model) {
        model.addAttribute("titre", "Paiements et factures");
        model.addAttribute("referenceFigma", "Paiements et factures");
        model.addAttribute("message", "Paiements et factures - list");
        return "placeholder";
    }


}
