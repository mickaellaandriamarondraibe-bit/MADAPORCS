package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PresenceController {

    @GetMapping("/presences")
    public String list(Model model) {
        model.addAttribute("titre", "Présences et pointage");
        model.addAttribute("referenceFigma", "Présences et pointage");
        model.addAttribute("message", "Présences et pointage - list");
        return "placeholder";
    }


}
