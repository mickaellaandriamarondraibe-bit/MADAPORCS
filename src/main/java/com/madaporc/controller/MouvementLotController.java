package com.madaporc.controller;

import com.madaporc.DTO.MouvementLotDTO;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import com.madaporc.model.TypeMouvementLot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.madaporc.service.MouvementLotService;
import com.madaporc.repository.TypeMouvementLotRepository;
import com.madaporc.model.MouvementLotPorc;
import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lots/mouvements")
public class MouvementLotController {
    @Autowired
    private MouvementLotService mouvementLotService;
    @Autowired
    private TypeMouvementLotRepository typeMouvementLotRepository;

    @GetMapping("/{id}")
    public String listMouvements(@PathVariable Long id, Model model) {
        model.addAttribute("titre", "Detail du Lot - Onglet Mouvements / Liste des Lots - Actions");
        List<MouvementLotPorc> mouvements = mouvementLotService.findMouvements(id);
        model.addAttribute("mouvements", mouvements);
        model.addAttribute("lotPorcId", id);

        return "lots/mouvements/liste";
    }  

    @GetMapping("/showForm/{id}")
    public String showForm(@PathVariable Long id, Model model) {
        List<TypeMouvementLot> typesMouvements = typeMouvementLotRepository.findAll();
        model.addAttribute("typesMouvements", typesMouvements);
        model.addAttribute("mouvementLotPorc", new MouvementLotPorc());
        model.addAttribute("lotPorcId", id);

        return "lots/mouvements/form";
    }

    @PostMapping("/save")
    public String saveMouvement(@ModelAttribute("mouvementLotPorc") MouvementLotDTO dto, HttpSession session) {
        Long utilisateurId = (Long) session.getAttribute("utilisateurId");
        
        if (utilisateurId == null) {
            return "redirect:/login";
        }

        mouvementLotService.ajouterMouvement(dto, utilisateurId);
        return "redirect:/lots/mouvements/" + dto.getLotPorcId();
    }

}
