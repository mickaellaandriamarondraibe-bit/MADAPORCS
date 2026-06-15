package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.MouvementStockDTO;
import com.madaporc.service.MouvementStockService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/stocks/mouvements")
public class MouvementStockController {

    private final MouvementStockService service;

    public MouvementStockController(MouvementStockService service) {
        this.service = service;
    }

    @GetMapping
    public String index(
            @RequestParam(required = false) Long ingredientId,
            @RequestParam(required = false) Long typeId,
            @RequestParam(required = false) String debut,
            @RequestParam(required = false) String fin,
            Model model
    ) {
        service.prepareListeModel(model, ingredientId, typeId, debut, fin);
        return "stocks/mouvements/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        service.prepareFormModel(model, id);
        return "stocks/mouvements/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("mouvement") MouvementStockDTO dto,
            Model model,
            HttpSession session
    ) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        String erreur = service.ajouterMouvement(dto, utilisateurId);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("mouvement", dto);
            service.prepareFormModel(model, dto.getId());
            return "stocks/mouvements/form";
        }

        return "redirect:/stocks/mouvements";
    }

    @GetMapping("/detail/{id}")
    public String detail(
            @PathVariable Long id,
            Model model
    ) {
        model.addAttribute("mouvement", service.findById(id).orElse(null));
        return "stocks/mouvements/detail";
    }
}