package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import com.madaporc.DTO.MouvementLotDTO;
import com.madaporc.service.MouvementLotService;

@Controller
public class MouvementLotController {
    private final MouvementLotService service;

    public MouvementLotController(MouvementLotService service) {
        this.service = service;
    }

    @GetMapping("/lots/{id}/mouvements")
    public String listMouvements(@PathVariable Long id, Model model) {
        model.addAttribute("mouvements", service.findMouvements(id));
        return "lots/mouvements";
    }

    @PostMapping("/lots/mouvements/save")
    public String saveMouvement(@ModelAttribute MouvementLotDTO dto, Model model, HttpSession session) {
        model.addAttribute("message", service.ajouterMouvement(dto, (Long) session.getAttribute("userId")));
        return "lots/mouvements";
    }
}
