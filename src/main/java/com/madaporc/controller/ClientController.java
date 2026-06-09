package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ClientController {

    @GetMapping("/clients")
    public String list(Model model) {
        model.addAttribute("titre", "Clients / acheteurs");
        model.addAttribute("referenceFigma", "Clients / acheteurs");
        model.addAttribute("message", "Clients / acheteurs - list");
        return "placeholder";
    }

   

}
