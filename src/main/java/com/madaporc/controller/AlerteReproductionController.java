package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.madaporc.service.AlerteReproductionService;

@Controller
public class AlerteReproductionController {

    private final AlerteReproductionService alerteService;

    public AlerteReproductionController(AlerteReproductionService alerteService) {
        this.alerteService = alerteService;
    }

    @GetMapping("/reproduction/alertes")
    public String listAlertes(Model model) {
        // La génération est assurée par la tâche planifiée (toutes les 60 s).
        // On ne régénère PAS ici : une page d'affichage ne doit pas créer de données,
        // sinon des doublons apparaissent quand la tâche et la page s'exécutent en même temps.
        model.addAttribute("alertes", alerteService.listerAlertesActives());
        return "reproduction/alertes/liste";
    }

    @PostMapping("/reproduction/alertes/{id}/lire")
    public String marquerCommeLue(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        alerteService.marquerCommeLue(id);
        redirectAttributes.addFlashAttribute("success", "Alerte marquee comme lue.");
        return "redirect:/reproduction/alertes";
    }

    @PostMapping("/reproduction/alertes/{id}/traiter")
    public String traiter(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Long groupeId = alerteService.getGroupeId(id);
            return "redirect:/reproduction/groupes/" + groupeId + "/confirmer-mise-bas?alerteId=" + id;
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("erreur", e.getMessage());
            return "redirect:/reproduction/alertes";
        }
    }
}
