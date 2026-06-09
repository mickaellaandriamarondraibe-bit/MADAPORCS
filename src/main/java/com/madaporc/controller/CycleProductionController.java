package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CycleProductionController {

    @GetMapping("/cycles-production")
    public String list(Model model) {
        model.addAttribute("titre", "Cycles de production");
        model.addAttribute("referenceFigma", "Cycles de production");
        model.addAttribute("message", "Cycles de production - list");
        return "placeholder";
    }

   

}
