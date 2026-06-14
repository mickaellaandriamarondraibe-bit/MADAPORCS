package com.madaporc.controller;

import com.madaporc.DTO.VaccinationDTO;
import com.madaporc.model.Vaccination;
import com.madaporc.service.VaccinationService;
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
 * Contrôleur pour la gestion des vaccinations.
 */
@Controller
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService vaccinationService;
    private final VaccinService vaccinService;

    /**
     * Liste toutes les vaccinations.
     */
    @GetMapping("/vaccinations")
    public String listVaccinations(@RequestParam(required = false) Long lotId, 
                                   @RequestParam(required = false) Long reproducteurId, 
                                   Model model) {
        List<Vaccination> vaccinations;
        
        if (lotId != null) {
            vaccinations = vaccinationService.findVaccinationsByLot(lotId);
        } else if (reproducteurId != null) {
            vaccinations = vaccinationService.findVaccinationsByReproducteur(reproducteurId);
        } else {
            vaccinations = vaccinationService.findAllVaccinations();
        }
        
        model.addAttribute("titre", "Journal des Vaccinations - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Journal des Vaccinations - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinationController");
        model.addAttribute("methodName", "listVaccinations");
        model.addAttribute("route", "/vaccinations");
        model.addAttribute("vaccinations", vaccinations);
        model.addAttribute("lotId", lotId);
        model.addAttribute("reproducteurId", reproducteurId);
        return "sante/vaccinations";
    }

    /**
     * Affiche le formulaire de création/modification d'une vaccination.
     */
    @GetMapping("/vaccinations/form")
    public String showVaccinationForm(@RequestParam(required = false) Long id, Model model) {
        VaccinationDTO dto = new VaccinationDTO();
        if (id != null) {
            Vaccination vaccination = vaccinationService.findById(id);
            mapperEntityToDTO(vaccination, dto);
        }
        
        model.addAttribute("titre", "Formulaire Vaccination - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Formulaire Vaccination - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinationController");
        model.addAttribute("methodName", "showVaccinationForm");
        model.addAttribute("vaccination", dto);
        model.addAttribute("vaccins", vaccinService.findAllVaccinsActifs());
        return "sante/formVaccination";
    }

    /**
     * Enregistre une vaccination.
     */
    @PostMapping("/vaccinations/save")
    public String saveVaccination(@ModelAttribute VaccinationDTO dto, Model model, HttpSession session) {
        try {
            String message;
            if (dto.getId() != null) {
                message = vaccinationService.modifier(dto.getId(), dto);
            } else {
                message = vaccinationService.creer(dto);
            }
            model.addAttribute("successMessage", message);
            return "redirect:/vaccinations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement : " + e.getMessage());
            return "sante/formVaccination";
        }
    }

    /**
     * Affiche les détails d'une vaccination.
     */
    @GetMapping("/vaccinations/{id}")
    public String detailVaccination(@PathVariable Long id, Model model) {
        Vaccination vaccination = vaccinationService.findById(id);
        model.addAttribute("titre", "Détails Vaccination - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Détails Vaccination - MADAPORC / GestPorc");
        model.addAttribute("controllerName", "VaccinationController");
        model.addAttribute("methodName", "detailVaccination");
        model.addAttribute("vaccination", vaccination);
        return "sante/detailVaccination";
    }

    /**
     * Enregistre un rappel de vaccination.
     */
    @PostMapping("/vaccinations/rappel/{id}")
    public String enregistrerRappel(@PathVariable Long id, Model model) {
        try {
            String message = vaccinationService.enregistrerRappel(id);
            model.addAttribute("successMessage", message);
            return "redirect:/vaccinations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de l'enregistrement du rappel : " + e.getMessage());
            return "redirect:/vaccinations";
        }
    }

    /**
     * Désactive une vaccination.
     */
    @PostMapping("/vaccinations/desactiver/{id}")
    public String desactiverVaccination(@PathVariable Long id, Model model) {
        try {
            String message = vaccinationService.desactiverVaccination(id);
            model.addAttribute("successMessage", message);
            return "redirect:/vaccinations";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Erreur lors de la désactivation : " + e.getMessage());
            return "redirect:/vaccinations";
        }
    }

    private void mapperEntityToDTO(Vaccination vaccination, VaccinationDTO dto) {
        dto.setId(vaccination.getId());
        if (vaccination.getVaccin() != null) {
            dto.setVaccinId(vaccination.getVaccin().getId());
        }
        if (vaccination.getLot() != null) {
            dto.setLotId(vaccination.getLot().getId());
        }
        if (vaccination.getReproducteur() != null) {
            dto.setReproducteurId(vaccination.getReproducteur().getId());
        }
        dto.setDateVaccination(vaccination.getDateVaccination());
        dto.setDateRappelPrevue(vaccination.getDateRappelPrevue());
        dto.setDateRappelEffectuee(vaccination.getDateRappelEffectuee());
        dto.setNumeroDose(vaccination.getNumeroDose());
        dto.setVeterinaire(vaccination.getVeterinaire());
        dto.setNotes(vaccination.getNotes());
        if (vaccination.getStatutRappel() != null) {
            dto.setStatutRappel(vaccination.getStatutRappel().toString());
        }
        dto.setActif(vaccination.getActif());
    }
}
