package com.madaporc.controller;

import com.madaporc.DTO.PeseeLotDTO;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.madaporc.service.PeseeLotService;
import com.madaporc.model.PeseeLot;
import java.util.List;

@Controller
@RequestMapping("/lots/pesees")
public class PeseeLotController {
    @Autowired
    private PeseeLotService peseeLotService;

    @GetMapping("/{id}")
    public String listPesees(@PathVariable Long id, Model model) {
        List<PeseeLot> pesees = peseeLotService.findPesees(id);
        model.addAttribute("titre", "Detail du Lot - Evolution du poids / Onglet Pesees");
        model.addAttribute("pesees", pesees);
        model.addAttribute("lotPorcId", id);
        return "lots/pesee/list";
    }

    @GetMapping("/showForm/{id}")
    public String showForm(@PathVariable Long id, Model model) {
        model.addAttribute("peseeLot", new PeseeLotDTO());
        model.addAttribute("lotPorcId", id);
        return "lots/pesee/form";
    }

    @PostMapping("/save")
    public String savePesee(@ModelAttribute PeseeLotDTO dto, HttpSession session) {
        Long utilisateurId = (Long) session.getAttribute("utilisateurId");
        peseeLotService.ajouterPesee(dto, utilisateurId);
        return "redirect:/lots/pesees/" + dto.getLotPorcId();
    }

    @GetMapping("/delete/{lotPorcId}/{id}")
    public String deletePesee(@PathVariable Long id, @PathVariable Long lotPorcId) {
        peseeLotService.supprimerPesee(id);
        return "redirect:/lots/pesees/" + lotPorcId;
    }

    @GetMapping("/edit/{lotPorcId}/{id}")
    public String editPesee(@PathVariable Long id, @PathVariable Long lotPorcId, Model model) {
        PeseeLotDTO dto = new PeseeLotDTO(); // TODO: récupérer la pesee existante et la convertir en DTO
        model.addAttribute("peseeLot", dto);
        model.addAttribute("lotPorcId", lotPorcId);
        return "lots/pesee/form";
    }
}
