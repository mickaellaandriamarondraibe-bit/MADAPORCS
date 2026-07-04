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
        model.addAttribute("lot", mouvementLotService.getLot(id));
        model.addAttribute("mouvements", mouvementLotService.getMouvementsByLot(id));
        model.addAttribute("effectifTotal", mouvementLotService.getEffectifTotal(id));
        return "lots/mouvements";
    }

    @PostMapping("/lots/mouvements/save")
    public String saveMouvement(@ModelAttribute MouvementLotDTO dto, Model model) {
        String error = mouvementLotService.enregistrerMouvement(dto);

        if (error != null) {
            model.addAttribute("lot", mouvementLotService.getLot(dto.getLotId()));
            model.addAttribute("mouvements", mouvementLotService.getMouvementsByLot(dto.getLotId()));
            model.addAttribute("effectifTotal", mouvementLotService.getEffectifTotal(dto.getLotId()));
            model.addAttribute("mouvement", dto);
            model.addAttribute("error", error);
            return "lots/mouvements";
        }

        return "redirect:/lots/" + dto.getLotId() + "/mouvements";
    }
}