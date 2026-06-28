package com.madaporc.controller;

import com.madaporc.dto.DepenseDTO;
import com.madaporc.model.CategorieDepense;
import com.madaporc.model.Depense;
import com.madaporc.repository.CategorieDepenseRepository;
import com.madaporc.service.DepenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class DepenseController {

    @Autowired
    private DepenseService depenseService;

    @Autowired
    private CategorieDepenseRepository categorieDepenseRepository;

    @GetMapping("/depenses")
    public String listDepenses(Model model) {
        List<Depense> depenses = depenseService.findAllDepenses();
        model.addAttribute("depenses", depenses);
        model.addAttribute("total", depenseService.calculerTotalDepenses());
        return "finance/depenses";
    }

    @GetMapping("/depenses/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        DepenseDTO dto = depenseService.getForm(id);
        List<CategorieDepense> categories = categorieDepenseRepository.findAll();
        model.addAttribute("depenseDTO", dto);
        model.addAttribute("categories", categories);
        return "finance/formDepense";
    }

    @PostMapping("/depenses/save")
    public String save(@ModelAttribute DepenseDTO dto, Model model) {
        String result;

        if (dto.getId() != null) {
            result = depenseService.modifierDepense(dto.getId(), dto);
        } else {
            result = depenseService.enregistrerDepense(dto);
        }

        if ("error".equals(result)) {
            List<CategorieDepense> categories = categorieDepenseRepository.findAll();
            model.addAttribute("depenseDTO", dto);
            model.addAttribute("categories", categories);
            model.addAttribute("error", "Données invalides : date et montant (> 0) sont obligatoires");
            return "finance/formDepense";
        }

        return result;
    }
}
