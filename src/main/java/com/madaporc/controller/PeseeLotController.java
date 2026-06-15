package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.PeseeLotDTO;
import com.madaporc.model.PeseeLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.PeseeLotRepository;
import com.madaporc.service.PeseeLotService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/lots/pesees")
public class PeseeLotController {

    private final PeseeLotService peseeLotService;
    private final PeseeLotRepository peseeLotRepository;
    private final LotPorcRepository lotPorcRepository;

    public PeseeLotController(
            PeseeLotService peseeLotService,
            PeseeLotRepository peseeLotRepository,
            LotPorcRepository lotPorcRepository
    ) {
        this.peseeLotService = peseeLotService;
        this.peseeLotRepository = peseeLotRepository;
        this.lotPorcRepository = lotPorcRepository;
    }

    @GetMapping
    public String liste(Model model) {
        model.addAttribute("pesees", peseeLotRepository.findAll());
        return "lots/pesees/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        PeseeLotDTO dto = new PeseeLotDTO();

        if (id != null) {
            peseeLotRepository.findById(id).ifPresent(pesee -> {
                dto.setId(pesee.getId());
                dto.setLotPorcId(pesee.getLotPorcId());
                dto.setDatePesee(pesee.getDatePesee());
                dto.setPoidsMoyenKg(pesee.getPoidsMoyenKg());
                dto.setObservation(pesee.getObservation());
            });
        }

        model.addAttribute("pesee", dto);
        model.addAttribute("lots", lotPorcRepository.findAll());

        return "lots/pesees/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("pesee") PeseeLotDTO dto,
            Model model,
            HttpSession session
    ) {
        Long utilisateurId = (Long) session.getAttribute("userId");

        String erreur = peseeLotService.ajouterPesee(dto, utilisateurId);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("pesee", dto);
            model.addAttribute("lots", lotPorcRepository.findAll());
            return "lots/pesees/form";
        }

        return "redirect:/lots/pesees";
    }
}