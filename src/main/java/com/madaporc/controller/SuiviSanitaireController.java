package com.madaporc.controller;

import com.madaporc.dto.SuiviSanitaireDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Maladie;
import com.madaporc.model.Traitement;
import com.madaporc.model.SuiviSanitaire;
import com.madaporc.service.LotPorcService;
import com.madaporc.service.MaladieService;
import com.madaporc.service.SuiviSanitaireService;
import com.madaporc.service.TraitementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SuiviSanitaireController {

    private final SuiviSanitaireService suiviSanitaireService;
    private final LotPorcService lotPorcService;
    private final MaladieService maladieService;
    private final TraitementService traitementService;

    @GetMapping("/sante/suivis")
    public String listSuivis(Model model) {
        List<SuiviSanitaire> suivis = suiviSanitaireService.getAll();
        model.addAttribute("suivis", suivis);
        return "sante/suivis";
    }

    @GetMapping("/sante/suivis/form")
    public String formSuivi(@RequestParam(required = false) Long id, Model model) {
        SuiviSanitaireDTO dto = (id == null) ? new SuiviSanitaireDTO() : suiviSanitaireService.getDtoById(id);

        List<LotPorc> lots = lotPorcService.getAllActifs();
        List<Maladie> maladies = maladieService.getAll();
        List<Traitement> traitements = traitementService.getAll();

        model.addAttribute("suivi", dto);
        model.addAttribute("lots", lots);
        model.addAttribute("maladies", maladies);
        model.addAttribute("traitements", traitements);
        return "sante/formSuivi";
    }

    @PostMapping("/sante/suivis/save")
    public String saveSuivi(@ModelAttribute SuiviSanitaireDTO dto, Model model) {
        String error = suiviSanitaireService.enregistrer(dto);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("suivi", dto);
            model.addAttribute("lots", lotPorcService.getAllActifs());
            model.addAttribute("maladies", maladieService.getAll());
            model.addAttribute("traitements", traitementService.getAll());
            return "sante/formSuivi";
        }
        return "redirect:/sante/suivis";
    }
}

