package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.PaiementDTO;
import com.madaporc.service.PaiementFactureService;

@Controller
public class PaiementFactureController {

    private final PaiementFactureService service;

    public PaiementFactureController(PaiementFactureService service) {
        this.service = service;
    }

    @GetMapping("/factures")
    public String listeFactures() {
        return "redirect:/ventes";
    }

    @GetMapping("/factures/form")
    public String formFacture() {
        return "redirect:/ventes";
    }

    @GetMapping("/factures/detail/{venteId}")
    public String detailFacture(@PathVariable Long venteId, Model model) {
        model.addAttribute("facture", service.getFacture(venteId));
        model.addAttribute("paiement", new PaiementDTO());

        return "factures/detail";
    }

    @PostMapping("/paiements/save")
    public String savePaiement(@ModelAttribute("paiement") PaiementDTO dto, Model model) {
        String erreur = service.enregistrerPaiement(dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("facture", service.getFacture(dto.getVenteId()));
            model.addAttribute("paiement", dto);

            return "factures/detail";
        }

        return "redirect:/factures/detail/" + dto.getVenteId();
    }

    @PostMapping("/factures/generer")
    public String genererFacture(@RequestParam Long venteId, Model model) {
        String erreur = service.genererFacture(venteId);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("facture", service.getFacture(venteId));
            model.addAttribute("paiement", new PaiementDTO());

            return "factures/detail";
        }

        return "redirect:/factures/detail/" + venteId;
    }

    @GetMapping("/factures/pdf/{venteId}")
    public String exporterFacturePdf(@PathVariable Long venteId, Model model) {
        model.addAttribute("facture", service.getFacture(venteId));

        return "factures/detail";
    }
}
