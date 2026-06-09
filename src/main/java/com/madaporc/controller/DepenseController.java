package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class DepenseController {

    @GetMapping("/depenses")
    public String list(Model model) {
        model.addAttribute("titre", "Dépenses");
        model.addAttribute("referenceFigma", "Dépenses");
        model.addAttribute("message", "Dépenses - list");
        return "placeholder";
    }

}
