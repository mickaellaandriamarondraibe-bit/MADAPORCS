package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.EvenementReproductionDTO;
import com.madaporc.service.EvenementReproductionService;

@Controller
public class EvenementReproductionController {
    private final EvenementReproductionService service;

    public EvenementReproductionController(EvenementReproductionService service) {
        this.service = service;
    }

    @GetMapping("/reproduction/evenements")
    public String listEvenements(@RequestParam(required = false) Long typeId, Model model) {
        model.addAttribute("evenements", service.rechercherEvenements(typeId));
        return "reproduction/evenements/list";
    }

    @GetMapping("/reproduction/evenements/form")
    public String showEvenementForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("evenement", new EvenementReproductionDTO());
        return "reproduction/evenements/list";
    }

    @PostMapping("/reproduction/evenements/save")
    public String saveEvenement(@ModelAttribute EvenementReproductionDTO dto, Model model, HttpSession session) {
        String e = dto.getId() == null ? service.ajouter(dto, (Long) session.getAttribute("userId"))
                : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e);
        return "reproduction/evenements/list";
    }
}
