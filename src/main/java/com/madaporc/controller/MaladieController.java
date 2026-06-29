package com.madaporc.controller;

import com.madaporc.DTO.MaladieDTO;
import com.madaporc.model.Maladie;
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
 * Contrôleur pour la gestion des maladies.
 */
@Controller
@RequiredArgsConstructor
public class MaladieController {

    private final MaladieService maladieService;

    /**
     * Liste toutes les maladies.
     */
    @GetMapping("/maladies")
    public String listMaladies(@RequestParam(required = false) String motCle, Model model) {
        List<Maladie> maladies;
        if (motCle != null && !motCle.isEmpty()) {
            maladies = maladieService.rechercherMaladies(motCle);
        } else {
            maladies = maladieService.findAllMaladies();
        }
        
        model.addAttribute("titre", "Gestion des Maladies - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Maladies - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "MaladieController");
        model.addAttribute("methodName", "listMaladies");
        model.addAttribute("route", "/maladies");
        model.addAttribute("maladies", maladies);
        model.addAttribute("motCle", motCle);
        return "sante/maladies";
    }

    /**
     * Affiche le formulaire de création/modification d'une maladie.
     */
    @GetMapping("/maladies/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        MaladieDTO dto = new MaladieDTO();
        if (id != null) {
            Maladie maladie = maladieService.findById(id);
            mapperEntityToDTO(maladie, dto);
        }
        
        model.addAttribute("titre", "Formulaire Maladie - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Formulaire Maladie - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "MaladieController");
        model.addAttribute("methodName", "showForm");
        model.addAttribute("maladie", dto);
        return "sante/formMaladie";
    }

    /**
     * Enregistre une maladie.
     */
    @PostMapping("/maladies/save")
    public String saveMaladie(@ModelAttribute MaladieDTO dto, Model model, HttpSession session) {
        try {
            String message;
            if (dto.getId() != null) {
                message = maladieService.modifier(dto.getId(), dto);
            } else {
                message = maladieService.creer(dto);
            }
            model.addAttribute("successMessage", message);
            return "redirect:/maladies";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "sante/formMaladie";
        }
    }

    /**
     * Affiche les détails d'une maladie.
     */
    @GetMapping("/maladies/{id}")
    public String detailMaladie(@PathVariable Long id, Model model) {
        Maladie maladie = maladieService.findById(id);
        model.addAttribute("titre", "Détails Maladie - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Détails Maladie - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "MaladieController");
        model.addAttribute("methodName", "detailMaladie");
        model.addAttribute("maladie", maladie);
        return "sante/detailMaladie";
    }

    /**
     * Désactive une maladie.
     */
    @PostMapping("/maladies/desactiver/{id}")
    public String desactiverMaladie(@PathVariable Long id, Model model) {
        try {
            String message = maladieService.desactiverMaladie(id);
            model.addAttribute("successMessage", message);
            return "redirect:/maladies";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la désactivation : " + e.getMessage());
            return "redirect:/maladies";
        }
    }

    private void mapperEntityToDTO(Maladie maladie, MaladieDTO dto) {
        dto.setId(maladie.getId());
        dto.setLibelle(maladie.getLibelle());
        dto.setDescription(maladie.getDescription());
        dto.setSymptomes(maladie.getSymptomes());
        dto.setTraitement(maladie.getTraitement());
        dto.setDureTraitementJours(maladie.getDureTraitementJours());
        dto.setTauxMortalitePercent(maladie.getTauxMortalitePercent());
        dto.setContagieux(maladie.getContagieux());
        dto.setActif(maladie.getActif());
    }
}
