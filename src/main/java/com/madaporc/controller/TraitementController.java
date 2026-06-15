package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.TraitementDTO;
import com.madaporc.service.TraitementService;

@Controller
@RequestMapping("/traitements")
public class TraitementController {

    private final TraitementService traitementService;

    public TraitementController(TraitementService traitementService) {
        this.traitementService = traitementService;
    }

    @GetMapping
    public String liste(
            @RequestParam(required = false) Long maladieId,
            @RequestParam(required = false) String motCle,
            Model model
    ) {
        model.addAttribute("traitements", traitementService.rechercherTraitements(maladieId, motCle));
        model.addAttribute("maladieId", maladieId);
        model.addAttribute("motCle", motCle);

        return "traitements/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        traitementService.prepareTraitementFormModel(model, id);
        return "traitements/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("traitement") TraitementDTO dto,
            Model model
    ) {
        String erreur = dto.getId() == null
                ? traitementService.creer(dto)
                : traitementService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("traitement", dto);
            traitementService.prepareTraitementFormModel(model, dto.getId());
            return "traitements/form";
        }

        return "redirect:/traitements";
    }

    @GetMapping("/desactiver/{id}")
    public String desactiver(@PathVariable Long id) {
        traitementService.desactiverTraitement(id);
        return "redirect:/traitements";
    }
}