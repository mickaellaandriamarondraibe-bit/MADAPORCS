package com.madaporc.controller;

import com.madaporc.DTO.VaccinDTO;
import com.madaporc.model.Vaccin;
import com.madaporc.service.VaccinService;
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
 * Contrôleur pour la gestion des vaccins.
 */
@Controller
@RequiredArgsConstructor
public class VaccinController {

    private final VaccinService vaccinService;

    /**
     * Liste tous les vaccins.
     */
    @GetMapping("/vaccins")
    public String listVaccins(@RequestParam(required = false) String motCle, Model model) {
        List<Vaccin> vaccins;
        if (motCle != null && !motCle.isEmpty()) {
            vaccins = vaccinService.rechercherVaccins(motCle);
        } else {
            vaccins = vaccinService.findAllVaccins();
        }
        
        model.addAttribute("titre", "Gestion des Vaccins - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Vaccins - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinController");
        model.addAttribute("methodName", "listVaccins");
        model.addAttribute("route", "/vaccins");
        model.addAttribute("vaccins", vaccins);
        model.addAttribute("motCle", motCle);
        return "sante/vaccins";
    }

    /**
     * Affiche le formulaire de création/modification d'un vaccin.
     */
    @GetMapping("/vaccins/form")
    public String showVaccinForm(@RequestParam(required = false) Long id, Model model) {
        VaccinDTO dto = new VaccinDTO();
        if (id != null) {
            Vaccin vaccin = vaccinService.findById(id);
            mapperEntityToDTO(vaccin, dto);
        }
        
        model.addAttribute("titre", "Formulaire Vaccin - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Formulaire Vaccin - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinController");
        model.addAttribute("methodName", "showVaccinForm");
        model.addAttribute("vaccin", dto);
        return "sante/formVaccin";
    }

    /**
     * Enregistre un vaccin.
     */
    @PostMapping("/vaccins/save")
    public String saveVaccin(@ModelAttribute VaccinDTO dto, Model model, HttpSession session) {
        try {
            String message;
            if (dto.getId() != null) {
                message = vaccinService.modifier(dto.getId(), dto);
            } else {
                message = vaccinService.creer(dto);
            }
            model.addAttribute("successMessage", message);
            return "redirect:/vaccins";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "sante/formVaccin";
        }
    }

    /**
     * Affiche les détails d'un vaccin.
     */
    @GetMapping("/vaccins/{id}")
    public String detailVaccin(@PathVariable Long id, Model model) {
        Vaccin vaccin = vaccinService.findById(id);
        model.addAttribute("titre", "Détails Vaccin - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Détails Vaccin - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinController");
        model.addAttribute("methodName", "detailVaccin");
        model.addAttribute("vaccin", vaccin);
        return "sante/detailVaccin";
    }

    /**
     * Désactive un vaccin.
     */
    @PostMapping("/vaccins/desactiver/{id}")
    public String desactiverVaccin(@PathVariable Long id, Model model) {
        try {
            String message = vaccinService.desactiverVaccin(id);
            model.addAttribute("successMessage", message);
            return "redirect:/vaccins";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la désactivation : " + e.getMessage());
            return "redirect:/vaccins";
        }
    }

    private void mapperEntityToDTO(Vaccin vaccin, VaccinDTO dto) {
        dto.setId(vaccin.getId());
        dto.setLibelle(vaccin.getLibelle());
        dto.setDescription(vaccin.getDescription());
        dto.setFabricant(vaccin.getFabricant());
        dto.setPrixDose(vaccin.getPrixDose());
        dto.setDelaiRappelJours(vaccin.getDelaiRappelJours());
        dto.setAgeMinimumJours(vaccin.getAgeMinimumJours());
        dto.setAgeMaximumJours(vaccin.getAgeMaximumJours());
        dto.setTemperatureStockageMin(vaccin.getTemperatureStockageMin());
        dto.setTemperatureStockageMax(vaccin.getTemperatureStockageMax());
        dto.setActif(vaccin.getActif());
    }
}
