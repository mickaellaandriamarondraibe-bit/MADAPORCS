package com.madaporc.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.dto.DepenseDTO;
import com.madaporc.model.Depense;
import com.madaporc.repository.CategorieDepenseRepository;
import com.madaporc.service.DepenseService;

@Controller
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @Autowired
    private CategorieDepenseRepository categorieDepenseRepository;

    @GetMapping("/depenses")
    public String listDepenses(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin,
            @RequestParam(required = false) Long categorieId,
            Model model) {

        List<Depense> depenses = depenseService.rechercher(dateDebut, dateFin, categorieId);
        model.addAttribute("depenses", depenses);
        model.addAttribute("totalDepenses", depenseService.total(depenses));
        model.addAttribute("categories", categorieDepenseRepository.findAllByOrderByNomAsc());

        // La vue relit les valeurs saisies dans le formulaire de filtre.
        Map<String, Object> filtre = new HashMap<>();
        filtre.put("dateDebut", dateDebut);
        filtre.put("dateFin", dateFin);
        filtre.put("categorieId", categorieId);
        model.addAttribute("filtre", filtre);

        return "finance/depenses";
    }

    @GetMapping("/depenses/form")
    public String showDepenseForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("depense", depenseService.getForm(id));
        model.addAttribute("categories", categorieDepenseRepository.findAllByOrderByNomAsc());
        return "finance/formDepense";
    }

    @PostMapping("/depenses/save")
    public String saveDepense(@ModelAttribute DepenseDTO dto, Model model) {
        String result;

        if (dto.getId() != null) {
            result = depenseService.modifierDepense(dto.getId(), dto);
        } else {
            result = depenseService.enregistrerDepense(dto);
        }

        if (result == null || !result.startsWith("redirect:")) {
            model.addAttribute("depense", dto);
            model.addAttribute("categories", categorieDepenseRepository.findAllByOrderByNomAsc());
            model.addAttribute("error", result);
            return "finance/formDepense";
        }

        return result;
    }
}
