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
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.RaceRepository;
import com.madaporc.service.LotPorcService;

@Controller
public class LotPorcController {
    private final LotPorcService lotPorcService;
    private final RaceRepository raceRepository;
    private final LotPorcRepository lotPorcRepository;

    public LotPorcController(LotPorcService lotPorcService ,RaceRepository raceRepository,LotPorcRepository lotPorcRepository ) {
        this.lotPorcService = lotPorcService;
        this.lotPorcRepository = lotPorcRepository;
        this.raceRepository = raceRepository;   
    }

    @GetMapping("/lots")
    public String listLots(@ModelAttribute LotFiltreDTO filtre, Model model) {
        model.addAttribute("lots", lotPorcService.rechercherLots(filtre));
        return "lots/listeLots";
    }

    @GetMapping("/lots/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        if (id != null) {
            lotPorcService.prepareFormModel(model, id);
        } else {
            model.addAttribute("lot", new LotPorcDTO());
        }
        model.addAttribute("races", raceRepository.findAll());
        model.addAttribute("lotsParents", lotPorcRepository.findAll());

        return "lots/formLot";
    }

    @PostMapping("/lots/save")
    public String save(@ModelAttribute LotPorcDTO dto, Model model) {
        return lotPorcService.creerLot(dto);
    }

    @GetMapping("/lots/{id}")
    public String detail(@PathVariable Long id, Model model) {
        LotDetailDTO lotDetail = lotPorcService.getDetailLot(id);
        model.addAttribute("detail", lotDetail);
        return "lots/detailLot";
    }
    @PostMapping("/lots/archive/{id}")
    public String archiver(@PathVariable Long id) {
        return lotPorcService.archiverLot(id);
    }
}