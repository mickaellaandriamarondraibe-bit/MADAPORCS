package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PeseeLotController {

    @GetMapping("/pesees-lots")
    public String list(Model model) {
        model.addAttribute("titre", "Pesées des lots");
        model.addAttribute("referenceFigma", "Pesées des lots");
        model.addAttribute("message", "Pesées des lots - list");
        return "placeholder";
    }


}
