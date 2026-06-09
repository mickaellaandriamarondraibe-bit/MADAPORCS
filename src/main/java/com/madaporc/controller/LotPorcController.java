package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LotPorcController {

    @GetMapping("/lots")
    public String list(Model model) {
        model.addAttribute("titre", "Lots de porcs");
        model.addAttribute("referenceFigma", "Lots de porcs");
        model.addAttribute("message", "Lots de porcs - list");
        return "placeholder";
    }

}
