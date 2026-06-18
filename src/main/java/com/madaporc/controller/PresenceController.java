package com.madaporc.controller;

import com.madaporc.DTO.PresenceDTO;
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
public class PresenceController {

    @GetMapping("/presences")
    public String listPresences(@RequestParam(required=false) LocalDate date, @RequestParam(required=false) Long employeId, Model model) {
        model.addAttribute("titre", "Presence et Pointage - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Presence et Pointage - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "PresenceController");
        model.addAttribute("methodName", "listPresences");
        model.addAttribute("route", "/presences");
        return "placeholder";
    }

    

}
