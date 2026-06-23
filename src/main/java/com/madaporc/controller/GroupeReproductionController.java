package com.madaporc.controller;

import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.service.GroupeReproductionMiseBasService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class GroupeReproductionController {

    private final GroupeReproductionMiseBasService service;

    public GroupeReproductionController(GroupeReproductionMiseBasService service) {
        this.service = service;
    }

    @GetMapping("/reproduction/groupes/{id}")
    public String detail(@PathVariable Long id, Model model) {
        GroupeReproductionDetailDTO detail = service.getDetailGroupe(id);
        Long nbjourRestante = service.calculerJoursRestants(id);

        model.addAttribute("detail", detail);
        model.addAttribute("joursRestants",nbjourRestante);
        return "reproduction/groupes/detail";
    }
}