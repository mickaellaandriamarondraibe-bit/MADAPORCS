package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.ReproducteurDTO;
import com.madaporc.service.ReproducteurService;

@Controller
public class ReproducteurController {
    private final ReproducteurService service;

    public ReproducteurController(ReproducteurService service) {
        this.service = service;
    }

    @GetMapping("/reproducteurs")
    public String listReproducteurs(@RequestParam(required = false) String motCle,
            @RequestParam(required = false) Long sexeId, @RequestParam(required = false) Long statutId, Model model) {
        model.addAttribute("reproducteurs", service.rechercherReproducteurs(motCle, sexeId, statutId));
        return "reproducteurs/listeReproducteurs";
    }

    @GetMapping("/reproducteurs/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        service.prepareReproducteurFormModel(model, id);
        return "reproducteurs/listeReproducteurs";
    }

    @PostMapping("/reproducteurs/save")
    public String save(@ModelAttribute ReproducteurDTO dto, Model model, HttpSession session) {
        String e = dto.getId() == null ? service.creer(dto, (Long) session.getAttribute("userId"))
                : service.modifier(dto.getId(), dto);
        model.addAttribute("message", e);
        return "reproducteurs/listeReproducteurs";
    }

    @PostMapping("/reproducteurs/archive/{id}")
    public String archiver(@PathVariable Long id, Model model) {
        service.archiverReproducteur(id);
        return "redirect:/reproducteurs";
    }

    @GetMapping("/reproducteurs/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("reproducteur", service.getDetailReproducteur(id));
        return "reproducteurs/detailReproducteur";
    }
}
