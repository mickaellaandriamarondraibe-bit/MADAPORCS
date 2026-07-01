package com.madaporc.controller;

import com.madaporc.dto.VaccinDTO;
import com.madaporc.model.Vaccin;
import com.madaporc.service.VaccinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class VaccinController {

    private final VaccinService vaccinService;

    @GetMapping("/vaccins")
    public String listVaccins(Model model) {
        List<Vaccin> vaccins = vaccinService.getAll();
        model.addAttribute("vaccins", vaccins);
        return "sante/vaccins";
    }

    @GetMapping("/vaccins/form")
    public String formVaccin(@RequestParam(required = false) Long id, Model model) {
        VaccinDTO dto = (id == null) ? new VaccinDTO() : vaccinService.getDtoById(id);
        model.addAttribute("vaccin", dto);
        return "sante/formVaccin";
    }

    @PostMapping("/vaccins/save")
    public String saveVaccin(@ModelAttribute VaccinDTO dto, Model model) {
        String error = vaccinService.enregistrer(dto);
        if (error != null) {
            model.addAttribute("error", error);
            model.addAttribute("vaccin", dto);
            return "sante/formVaccin";
        }
        return "redirect:/vaccins";
    }
}

