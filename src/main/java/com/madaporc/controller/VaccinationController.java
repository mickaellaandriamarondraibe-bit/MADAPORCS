package com.madaporc.controller;

import com.madaporc.dto.VaccinationDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Vaccin;
import com.madaporc.model.Vaccination;
import com.madaporc.service.LotPorcService;
import com.madaporc.service.VaccinService;
import com.madaporc.service.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VaccinationController {

    private final VaccinationService vaccinationService;
    private final LotPorcService lotPorcService;
    private final VaccinService vaccinService;

    @GetMapping("/vaccinations")
    public String listVaccinations(Model model) {
        List<Vaccination> vaccinations = vaccinationService.getAll();
        model.addAttribute("vaccinations", vaccinations);
        return "sante/vaccinations";
    }

    @GetMapping("/vaccinations/form")
    public String formVaccination(@RequestParam(required = false) Long id, Model model) {
        VaccinationDTO dto = (id == null) ? new VaccinationDTO() : vaccinationService.getDtoById(id);

        List<LotPorc> lots = lotPorcService.getAllActifs();
        List<Vaccin> vaccins = vaccinService.getAll();

        model.addAttribute("vaccination", dto);
        model.addAttribute("lots", lots);
        model.addAttribute("vaccins", vaccins);
        return "sante/formVaccination";
    }

    @PostMapping("/vaccinations/save")
    public String saveVaccination(@ModelAttribute VaccinationDTO dto, Model model) {
        String error = vaccinationService.enregistrer(dto);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("vaccination", dto);
            model.addAttribute("lots", lotPorcService.getAllActifs());
            model.addAttribute("vaccins", vaccinService.getAll());
            return "sante/formVaccination";
        }
        return "redirect:/vaccinations";
    }
}

