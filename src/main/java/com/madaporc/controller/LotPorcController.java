package com.madaporc.controller;

import com.madaporc.dto.LotFiltreDTO;
import com.madaporc.dto.LotPorcDTO;
import com.madaporc.service.LotPorcService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class LotPorcController {

    private final LotPorcService lotPorcService;

    @GetMapping("/lots")
    public String listLots(@ModelAttribute LotFiltreDTO filtre, Model model) {
        model.addAttribute("lots", lotPorcService.rechercherLots(filtre));
        model.addAttribute("filtre", filtre);
        return "lots/listeLots";
    }

    @GetMapping("/lots/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        lotPorcService.prepareFormModel(model, id);
        return "lots/formLot";
    }

    @PostMapping("/lots/save")
    public String save(@ModelAttribute LotPorcDTO dto, Model model) {
        String error;

        if (dto.getId() == null) {
            error = lotPorcService.creerLot(dto);
        } else {
            error = lotPorcService.modifierLot(dto.getId(), dto);
        }

        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("lot", dto);
            model.addAttribute("races", lotPorcService.getAllRaces());
            return "lots/formLot";
        }

        return "redirect:/lots";
    }

    @GetMapping("/lots/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("lot", lotPorcService.getDetailLot(id));
        return "lots/detailLot";
    }

    @PostMapping("/lots/archive/{id}")
    public String archiver(@PathVariable Long id) {
        String error = lotPorcService.archiverLot(id);

        if (error != null) {
            return "redirect:/lots?error=" + error;
        }

        return "redirect:/lots";
    }
    
    @GetMapping("/lots/archive/{id}")
    public String archiverGet(@PathVariable Long id) {
        String error = lotPorcService.archiverLot(id);

        if (error != null) {
            return "redirect:/lots?error=" + error;
        }

        return "redirect:/lots";
    }
}