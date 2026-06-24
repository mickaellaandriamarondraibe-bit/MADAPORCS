package com.madaporc.controller;

import com.madaporc.dto.PeseeLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.PeseeLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.service.PeseeLotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PeseeLotController {

    private final PeseeLotService peseeLotService;
    private final LotPorcRepository lotPorcRepository;

    @GetMapping("/lots/{id}/pesees")
    public String listPesees(@PathVariable Long id, Model model) {
        LotPorc lot = lotPorcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable"));

        List<PeseeLot> pesees = peseeLotService.getPeseesByLot(id);
        BigDecimal evolution = peseeLotService.calculerEvolutionPoids(id);

        model.addAttribute("lot", lot);
        model.addAttribute("pesees", pesees);
        model.addAttribute("evolution", evolution);

        // Formulaire: utilisé par pesees.jsp
        PeseeLotDTO form = new PeseeLotDTO();
        form.setLotId(lot.getId());
        model.addAttribute("vaccin", null); // no-op, garder compat si jsp le fait
        model.addAttribute("pesee", form);
        model.addAttribute("form", form);

        return "lots/pesees";
    }

    @PostMapping("/lots/pesees/save")
    public String savePesee(@ModelAttribute PeseeLotDTO dto, Model model) {
        String error = peseeLotService.enregistrerPesee(dto);
        Long lotId = dto != null ? dto.getLotId() : null;

        if (error != null) {
            model.addAttribute("error", error);
            return listPesees(lotId, model);
        }

        return "redirect:/lots/" + lotId + "/pesees";
    }
}

