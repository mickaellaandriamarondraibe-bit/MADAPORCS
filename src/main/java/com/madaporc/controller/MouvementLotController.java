package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.madaporc.dto.MouvementLotDTO;
import com.madaporc.service.MouvementLotService;

@Controller
public class MouvementLotController {
    private final MouvementLotService mouvementLotService;

    public MouvementLotController(MouvementLotService mouvementLotService) {
        this.mouvementLotService = mouvementLotService;
    }

    @GetMapping("/lots/{id}/mouvements")
    public String listMouvements(@PathVariable Long id, Model model) {
        model.addAttribute("mouvements", mouvementLotService.getMouvementsByLot(id));
        model.addAttribute("effectifTotal", mouvementLotService.getEffectifTotal(id));
        return "lots/mouvements";
    }

    @PostMapping("/lots/mouvements/save")
    public String saveMouvement(@ModelAttribute MouvementLotDTO dto, Model model) {
        return mouvementLotService.enregistrerMouvement(dto);
    }
}