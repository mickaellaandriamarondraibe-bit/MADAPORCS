package com.madaporc.controller;

import com.madaporc.DTO.TraitementDTO;
import com.madaporc.model.Traitement;
import com.madaporc.model.Maladie;
import com.madaporc.service.TraitementService;
import com.madaporc.service.MaladieService;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour la gestion des traitements.
 */
@Controller
@RequiredArgsConstructor
public class TraitementController {

    private final TraitementService traitementService;
    private final MaladieService maladieService;

    /**
     * Liste tous les traitements.
     */
    @GetMapping("/traitements")
    public String listTraitements(@RequestParam(required = false) String motCle, 
                                  @RequestParam(required = false) Long maladieId, 
                                  Model model) {
        List<Traitement> traitements;
        
        if (motCle != null && !motCle.isEmpty()) {
            traitements = traitementService.rechercherTraitements(motCle);
        } else if (maladieId != null) {
            traitements = traitementService.findTraitementsByMaladie(maladieId);
        } else {
            traitements = traitementService.findAllTraitements();
        }
        
        List<Maladie> maladies = maladieService.findAllMaladiesActives();
        
        model.addAttribute("titre", "Gestion des Traitements - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Traitements - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "TraitementController");
        model.addAttribute("methodName", "listTraitements");
        model.addAttribute("route", "/traitements");
        model.addAttribute("traitements", traitements);
        model.addAttribute("maladies", maladies);
        model.addAttribute("motCle", motCle);
        model.addAttribute("maladieId", maladieId);
        return "sante/traitements";
    }

    /**
     * Affiche le formulaire de création/modification d'un traitement.
     */
    @GetMapping("/traitements/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        TraitementDTO dto = new TraitementDTO();
        if (id != null) {
            Traitement traitement = traitementService.findById(id);
            mapperEntityToDTO(traitement, dto);
        }
        
        List<Maladie> maladies = maladieService.findAllMaladiesActives();
        
        model.addAttribute("titre", "Formulaire Traitement - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Formulaire Traitement - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "TraitementController");
        model.addAttribute("methodName", "showForm");
        model.addAttribute("traitement", dto);
        model.addAttribute("maladies", maladies);
        return "sante/formTraitement";
    }

    /**
     * Enregistre un traitement.
     */
    @PostMapping("/traitements/save")
    public String saveTraitement(@ModelAttribute TraitementDTO dto, Model model, HttpSession session) {
        try {
            String message;
            if (dto.getId() != null) {
                message = traitementService.modifier(dto.getId(), dto);
            } else {
                message = traitementService.creer(dto);
            }
            model.addAttribute("successMessage", message);
            return "redirect:/traitements";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "sante/formTraitement";
        }
    }

    /**
     * Affiche les détails d'un traitement.
     */
    @GetMapping("/traitements/{id}")
    public String detailTraitement(@PathVariable Long id, Model model) {
        Traitement traitement = traitementService.findById(id);
        model.addAttribute("titre", "Détails Traitement - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Détails Traitement - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "TraitementController");
        model.addAttribute("methodName", "detailTraitement");
        model.addAttribute("traitement", traitement);
        return "sante/detailTraitement";
    }

    /**
     * Désactive un traitement.
     */
    @PostMapping("/traitements/desactiver/{id}")
    public String desactiverTraitement(@PathVariable Long id, Model model) {
        try {
            String message = traitementService.desactiverTraitement(id);
            model.addAttribute("successMessage", message);
            return "redirect:/traitements";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la désactivation : " + e.getMessage());
            return "redirect:/traitements";
        }
    }

    private void mapperEntityToDTO(Traitement traitement, TraitementDTO dto) {
        dto.setId(traitement.getId());
        dto.setLibelle(traitement.getLibelle());
        dto.setDescription(traitement.getDescription());
        if (traitement.getMaladie() != null) {
            dto.setMaladieId(traitement.getMaladie().getId());
        }
        dto.setPrincipe(traitement.getPrincipe());
        dto.setDosageMl(traitement.getDosageMl());
        dto.setFrequenceJours(traitement.getFrequenceJours());
        dto.setPrixUnite(traitement.getPrixUnite());
        dto.setNombreJoursTraitement(traitement.getNombreJoursTraitement());
        dto.setActif(traitement.getActif());
    }
}
