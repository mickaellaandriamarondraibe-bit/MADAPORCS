package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.VaccinDTO;
import com.madaporc.service.VaccinService;

@Controller
@RequestMapping("/vaccins")
public class VaccinController {

    private final VaccinService vaccinService;

    public VaccinController(VaccinService vaccinService) {
        this.vaccinService = vaccinService;
    }

    @GetMapping
    public String liste(
            @RequestParam(required = false) Boolean actif,
            Model model
    ) {
        model.addAttribute("vaccins", vaccinService.findAllVaccins());
        model.addAttribute("actif", actif);

        return "vaccins/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        vaccinService.prepareVaccinFormModel(model, id);
        return "vaccins/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("vaccin") VaccinDTO dto,
            Model model
    ) {
        String erreur = dto.getId() == null
                ? vaccinService.creer(dto)
                : vaccinService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("vaccin", dto);
            return "vaccins/form";
        }

        return "redirect:/vaccins";
    }

    @GetMapping("/desactiver/{id}")
    public String desactiver(@PathVariable Long id) {
        vaccinService.desactiverVaccin(id);
        return "redirect:/vaccins";
    }
}