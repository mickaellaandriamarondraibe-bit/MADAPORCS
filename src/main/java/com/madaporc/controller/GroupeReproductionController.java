package com.madaporc.controller;

import com.madaporc.dto.ConfirmationMiseBasDTO;
import com.madaporc.dto.GroupeReproductionDTO;
import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.service.GroupeReproductionCreationService;
import com.madaporc.service.GroupeReproductionQueryService;
import com.madaporc.service.GroupeReproductionMiseBasService;
import com.madaporc.service.AlerteReproductionService;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class GroupeReproductionController {

    private final GroupeReproductionMiseBasService miseBasService;
    private final GroupeReproductionQueryService queryService;
    private final GroupeReproductionCreationService creationService;
    private final LotPorcRepository lotPorcRepository;
    private final AlerteReproductionService alerteService;

    public GroupeReproductionController(
            GroupeReproductionMiseBasService miseBasService,
            GroupeReproductionQueryService queryService,
            GroupeReproductionCreationService creationService,
            LotPorcRepository lotPorcRepository,
            AlerteReproductionService alerteService) {
        this.miseBasService = miseBasService;
        this.queryService = queryService;
        this.creationService = creationService;
        this.lotPorcRepository = lotPorcRepository;
        this.alerteService = alerteService;
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
        return "redirect:/reproduction/groupes";
    }



    @GetMapping("/reproduction/groupes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = miseBasService.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("joursRestants", miseBasService.calculerJoursRestants(id));
        model.addAttribute("pourcentageEvolution", miseBasService.calculerPourcentageEvolution(id));

        // Lot(s) naissance cree(s) pour ce groupe (femelle et/ou male)
        List<LotPorc> lotsNaissance = lotPorcRepository.findByGroupeReproductionOrigineId(id);
        model.addAttribute("lotsNaissance", lotsNaissance);
        return "reproduction/groupes/detail";
    }

    @GetMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String afficherFormulaireMiseBas(
            @PathVariable Long id,
            @RequestParam(required = false) Long alerteId,
            Model model) {
        GroupeReproductionDetailDTO detail = miseBasService.getDetailGroupe(id);
        model.addAttribute("detail", detail);
        model.addAttribute("dto", new ConfirmationMiseBasDTO());
        model.addAttribute("alerteId", alerteId);
        return "reproduction/groupes/confirmerMiseBas";
    }

    @PostMapping("/reproduction/groupes/{id}/confirmer-mise-bas")
    public String confirmerMiseBas(
            @PathVariable Long id,
            @Valid @ModelAttribute("dto") ConfirmationMiseBasDTO dto,
            @RequestParam(required = false) Long alerteId,
            BindingResult result,
            Model model,
            RedirectAttributes ra
    ) {
        if (result.hasErrors()) {
            model.addAttribute("detail", miseBasService.getDetailGroupe(id));
            model.addAttribute("alerteId", alerteId);
            return "reproduction/groupes/confirmerMiseBas";
        }

        try {
            if (alerteId != null && !id.equals(alerteService.getGroupeId(alerteId))) {
                throw new IllegalArgumentException("L'alerte ne correspond pas au groupe de reproduction confirmé.");
            }
            // On enregistre la mise bas. Le(s) lot(s) naissance (femelle / male) sont
            // crees dans la MEME transaction par confirmerMiseBas : ne pas rappeler
            // creerLotsNaissance ici, sinon on obtient des doublons (LOT-...-2).
            miseBasService.confirmerMiseBas(id, dto);
            if (alerteId != null) {
                alerteService.traiterApresConfirmationMiseBas(alerteId, id);
            }
            ra.addFlashAttribute("message", "Mise bas confirmée et lot(s) naissance créé(s).");
            return "redirect:/reproduction/groupes/" + id;
        } catch (IllegalArgumentException e) {
            model.addAttribute("detail", miseBasService.getDetailGroupe(id));
            model.addAttribute("alerteId", alerteId);
            model.addAttribute("erreur", e.getMessage());
            return "reproduction/groupes/confirmerMiseBas";
        }
    }

    // Cloture du groupe de reproduction.
    @PostMapping("/reproduction/groupes/{id}/cloturer")
    public String cloturer(@PathVariable Long id, RedirectAttributes ra) {
        try {
            miseBasService.cloturer(id);
            ra.addFlashAttribute("message", "Groupe clôturé.");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("erreur", e.getMessage());
        }
        return "redirect:/reproduction/groupes/" + id;
    }
}
