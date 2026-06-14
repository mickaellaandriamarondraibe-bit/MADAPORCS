package com.madaporc.controller;

import com.madaporc.DTO.ClientDTO;
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
public class ClientController {

    @GetMapping("/clients")
    public String listClients(@RequestParam(required=false) String motCle, @RequestParam(required=false) String typeClient, Model model) {
        model.addAttribute("titre", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "ClientController");
        model.addAttribute("methodName", "listClients");
        model.addAttribute("route", "/clients");
        return "placeholder";
    }

}
