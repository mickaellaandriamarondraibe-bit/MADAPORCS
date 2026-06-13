package com.madaporc.controller;

import com.madaporc.DTO.VenteDTO;
import com.madaporc.service.VenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VenteController {

    private final VenteService venteService;

    public VenteController(VenteService venteService) {
        this.venteService = venteService;
    }

    @GetMapping("/ventes")
    public String listVentes(Model model) {
        model.addAttribute("titre", "Gestion des Ventes - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Ventes - MADAPORC / GestPorc");
        model.addAttribute("ventes", venteService.findAllVentes());
        model.addAttribute("vente", new VenteDTO());
        venteService.prepareVenteFormModel(model, null);
        return "commerce/ventes";
    }

    @GetMapping("/ventes/form")
    public String showVenteForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("titre", "Gestion des Ventes - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Ventes - MADAPORC / GestPorc");
        venteService.prepareVenteFormModel(model, id);
        return "commerce/ventes";
    }

    @PostMapping("/ventes/save")
    public String saveVente(@ModelAttribute VenteDTO dto, Model model, HttpSession session) {
        Long utilisateurId = session.getAttribute("utilisateurId") instanceof Long id ? id : null;
        String message = dto.getId() == null ? venteService.creer(dto, utilisateurId) : venteService.modifier(dto.getId(), dto);
        model.addAttribute("message", message);
        return "redirect:/ventes";
    }

    @PostMapping("/ventes/valider/{id}")
    public String validerVente(@PathVariable Long id, Model model) {
        model.addAttribute("message", venteService.validerVente(id));
        return "redirect:/ventes";
    }

    @PostMapping("/ventes/annuler/{id}")
    public String annulerVente(@PathVariable Long id, Model model) {
        model.addAttribute("message", venteService.annulerVente(id));
        return "redirect:/ventes";
    }
}
