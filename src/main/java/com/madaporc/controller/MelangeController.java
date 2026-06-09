package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MelangeController {

    @GetMapping("/melanges")
    public String list(Model model) {
        model.addAttribute("titre", "Mélanges alimentaires");
        model.addAttribute("referenceFigma", "Mélanges alimentaires");
        model.addAttribute("message", "Mélanges alimentaires - list");
        return "placeholder";
    }

 
}
