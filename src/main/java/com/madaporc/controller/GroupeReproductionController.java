package com.madaporc.controller;

import com.madaporc.dto.ConfirmationMiseBasDTO;
import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.service.GroupeReproductionMiseBasService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class GroupeReproductionController {

    private final GroupeReproductionMiseBasService service;

    public GroupeReproductionController(GroupeReproductionMiseBasService service) {
        this.service = service;
    }

    @GetMapping("/reproduction/groupes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = service.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("joursRestants", service.calculerJoursRestants(id));
        model.addAttribute("pourcentageEvolution", service.calculerPourcentageEvolution(id));
        return "reproduction/groupes/detail";
    }

    @GetMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String afficherFormulaireMiseBas(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = service.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("dto", new ConfirmationMiseBasDTO());
        return "reproduction/groupes/confirmerMiseBas";
    }

    @PostMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String confirmerMiseBas(
            @PathVariable Long id,
            @Valid @ModelAttribute("dto") ConfirmationMiseBasDTO dto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("detail", service.getDetailGroupe(id));
            return "reproduction/groupes/confirmerMiseBas";
        }

        try {
            service.confirmerMiseBas(id, dto);
            return "redirect:/reproduction/groupes/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("detail", service.getDetailGroupe(id));
            model.addAttribute("erreur", e.getMessage());
            return "reproduction/groupes/confirmerMiseBas";
        }
    }
}