package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.DepenseDTO;
import com.madaporc.service.DepenseService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/depenses")
public class DepenseController {

    private final DepenseService depenseService;

    public DepenseController(DepenseService depenseService) {
        this.depenseService = depenseService;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("depenses", depenseService.findAllDepenses());
        return "depenses/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        depenseService.prepareDepenseFormModel(model, id);
        return "depenses/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("depense") DepenseDTO dto,
            Model model,
            HttpSession session
    ) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        String erreur = dto.getId() == null
                ? depenseService.creer(dto, utilisateurId)
                : depenseService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("depense", dto);
            depenseService.prepareDepenseFormModel(model, dto.getId());
            return "depenses/form";
        }

        return "redirect:/depenses";
    }
}