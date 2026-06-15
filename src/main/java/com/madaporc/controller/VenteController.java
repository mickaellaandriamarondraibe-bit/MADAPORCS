package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.VenteDTO;
import com.madaporc.service.VenteService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/ventes")
public class VenteController {

    private final VenteService venteService;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("ventes", venteService.findAllVentes());
        return "ventes/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        venteService.prepareVenteFormModel(model, id);
        return "ventes/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("vente") VenteDTO dto,
            Model model,
            HttpSession session
    ) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        String erreur = dto.getId() == null
                ? venteService.creer(dto, utilisateurId)
                : venteService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            venteService.prepareVenteFormModel(model, dto.getId());
            model.addAttribute("vente", dto);
            return "ventes/form";
        }

        return "redirect:/ventes";
    }

    @GetMapping("/valider/{id}")
    public String valider(@PathVariable Long id) {
        venteService.validerVente(id);
        return "redirect:/ventes";
    }

    @GetMapping("/annuler/{id}")
    public String annuler(@PathVariable Long id) {
        venteService.annulerVente(id);
        return "redirect:/ventes";
    }
}