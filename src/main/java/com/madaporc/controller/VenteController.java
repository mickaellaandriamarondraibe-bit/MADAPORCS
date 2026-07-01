package com.madaporc.controller;

import com.madaporc.dto.VenteDTO;
import com.madaporc.service.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VenteController {

    @Autowired
    private VenteService venteService;

    @GetMapping("/ventes")
    public String listVentes(Model model) {

        model.addAttribute("listeVentes", venteService.findAll());

        return "vente/list";
    }

    @GetMapping("/ventes/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {

        VenteDTO dto;

        if (id == null) {
            dto = new VenteDTO();
        } else {
            dto = venteService.getForm(id);

            if (dto == null) {
                return "redirect:/ventes";
            }
        }

        model.addAttribute("vente", dto);

        return "vente/form";
    }

    @PostMapping("/ventes/save")
    public String save(@ModelAttribute("vente") VenteDTO dto, Model model) {

        String validation = venteService.validerDonneesVente(dto);

        if (!validation.equals("redirect:/ventes/form")) {
            return validation;
        }

        return venteService.creerVente(dto);
    }

    @PostMapping("/ventes/valider/{id}")
    public String valider(@PathVariable Long id) {

        return venteService.validerVente(id);
    }

    @PostMapping("/ventes/annuler/{id}")
    public String annuler(@PathVariable Long id) {

        return venteService.annulerVente(id);
    }

    @GetMapping("/ventes/{id}")
    public String detail(@PathVariable Long id, Model model) {

        if (venteService.findById(id) == null) {
            return "redirect:/ventes";
        }

        model.addAttribute("vente", venteService.findById(id));
        model.addAttribute("details", venteService.getDetailsVente(id));

        return "vente/detail";
    }

}