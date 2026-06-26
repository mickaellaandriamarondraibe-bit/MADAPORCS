package com.madaporc.controller;

import com.madaporc.dto.ConfirmationMiseBasDTO;
import com.madaporc.dto.GroupeReproductionDTO;
import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.service.GroupeReproductionCreationService;
import com.madaporc.service.GroupeReproductionQueryService;
import com.madaporc.service.GroupeReproductionMiseBasService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GroupeReproductionController {

    private final GroupeReproductionMiseBasService miseBasService;
    private final GroupeReproductionQueryService queryService;
    private final GroupeReproductionCreationService creationService;

    public GroupeReproductionController(
            GroupeReproductionMiseBasService miseBasService,
            GroupeReproductionQueryService queryService,
            GroupeReproductionCreationService creationService) {
        this.miseBasService = miseBasService;
        this.queryService = queryService;
        this.creationService = creationService;
    }


    

    @GetMapping("/reproduction/groupes")
    public String listGroupes(Model model) {
        List<GroupeReproduction> groupes = queryService.findAll();
        model.addAttribute("groupes", groupes);
        return "reproduction/groupes/liste";
    }

    @GetMapping("/reproduction/groupes/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        queryService.prepareFormModel(model, id);
        return "reproduction/groupes/form";
    }

    @PostMapping("/reproduction/groupes/save")
    public String save(@ModelAttribute GroupeReproductionDTO dto, Model model, HttpSession session) {
        Long utilisateurId = (Long) session.getAttribute("utilisateurId");
        if (utilisateurId == null) {
            utilisateurId = 1L; 
        }

        String resultat = creationService.creer(dto, utilisateurId);

        if (!"SUCCESS".equals(resultat)) {
            model.addAttribute("erreur", resultat);
            model.addAttribute("groupeReproductionDTO", dto);
            queryService.prepareFormModel(model, null);
            return "reproduction/groupes/form";
        }
        return "reproduction/groupes";
    }



    @GetMapping("/reproduction/groupes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = miseBasService.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("joursRestants", miseBasService.calculerJoursRestants(id));
        model.addAttribute("pourcentageEvolution", miseBasService.calculerPourcentageEvolution(id));
        return "reproduction/groupes/detail";
    }

    @GetMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String afficherFormulaireMiseBas(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = miseBasService.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("dto", new ConfirmationMiseBasDTO());
        return "reproduction/groupes/confirmerMiseBas";
    }

    @PostMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String confirmerMiseBas(
            @PathVariable Long id,
            @Valid @ModelAttribute("dto") ConfirmationMiseBasDTO dto,
            BindingResult result,
            Model model
    ) {
        if (result.hasErrors()) {
            model.addAttribute("detail", miseBasService.getDetailGroupe(id));
            return "reproduction/groupes/confirmerMiseBas";
        }

        try {
            miseBasService.confirmerMiseBas(id, dto);
            return "redirect:/reproduction/groupes/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("detail", miseBasService.getDetailGroupe(id));
            model.addAttribute("erreur", e.getMessage());
            return "reproduction/groupes/confirmerMiseBas";
        }
    }
}