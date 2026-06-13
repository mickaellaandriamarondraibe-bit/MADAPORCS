package com.madaporc.controller;

import com.madaporc.DTO.PaiementDTO;
import com.madaporc.service.PaiementFactureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PaiementFactureController {

    private final PaiementFactureService paiementFactureService;

    public PaiementFactureController(PaiementFactureService paiementFactureService) {
        this.paiementFactureService = paiementFactureService;
    }

    @GetMapping("/factures/{venteId}")
    public String detailFacture(@PathVariable Long venteId, Model model) {
        model.addAttribute("titre", "Paiements et Factures - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Paiements et Factures - MADAPORC / GestPorc");
        model.addAttribute("facture", paiementFactureService.getFacture(venteId));
        model.addAttribute("paiements", paiementFactureService.getPaiements(venteId));
        model.addAttribute("resteAPayer", paiementFactureService.calculerResteAPayer(venteId));
        model.addAttribute("paiement", new PaiementDTO());
        return "commerce/factureDetail";
    }

    @PostMapping("/paiements/save")
    public String savePaiement(@ModelAttribute PaiementDTO dto, Model model) {
        model.addAttribute("message", paiementFactureService.enregistrerPaiement(dto));
        return "redirect:/factures/" + dto.getVenteId();
    }

    @PostMapping("/factures/generer")
    public String genererFacture(@RequestParam Long venteId, Model model) {
        model.addAttribute("message", paiementFactureService.genererFacture(venteId));
        return "redirect:/factures/" + venteId;
    }

    @GetMapping("/factures/pdf/{venteId}")
    public String exporterFacturePdf(@PathVariable Long venteId, Model model) {
        model.addAttribute("facture", paiementFactureService.getFacture(venteId));
        return "commerce/factureDetail";
    }
}
