package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.dto.LotDetailDTO;
import com.madaporc.dto.LotFiltreDTO;
import com.madaporc.dto.LotPorcDTO;
import com.madaporc.service.LotPorcService;

@Controller
public class LotPorcController {
   private final LotPorcService lotPorcService;

    public LotPorcController(LotPorcService lotPorcService) {
        this.lotPorcService = lotPorcService;
    }

    @GetMapping("/lots")
    public String listLots(@ModelAttribute LotFiltreDTO filtre, Model model) {
        model.addAttribute("lots", lotPorcService.rechercherLots(filtre));
        return "lots";
    }

    @GetMapping("/lots/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            lotPorcService.prepareFormModel(model, id);
        } else {
            model.addAttribute("lot", new LotPorcDTO());
        }
        return "lot-form";
    }

    @PostMapping("/lots/save")
    public String save(@ModelAttribute LotPorcDTO dto, Model model) {
        return lotPorcService.creerLot(dto); 
    }

    @GetMapping("/lots/{id}")
    public String detail(@PathVariable Long id, Model model) {
        LotDetailDTO lotDetail = lotPorcService.getDetailLot(id);
        model.addAttribute("lot", lotDetail);
        return "lot-detail";
    }

    @PostMapping("/lots/archive/{id}")
    public String archiver(@PathVariable Long id) {
        return lotPorcService.archiverLot(id);
    }
}