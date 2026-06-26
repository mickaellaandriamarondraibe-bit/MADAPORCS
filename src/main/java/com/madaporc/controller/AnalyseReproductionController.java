package com.madaporc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.madaporc.dto.AnalyseReproductionLotDTO;
import com.madaporc.service.AnalyseReproductionService;

@Controller
public class AnalyseReproductionController {

    @Autowired
    private AnalyseReproductionService analyseReproductionService;

    @GetMapping("/reproduction/analyse")
    public String index(Model model) {
        model.addAttribute("lots", analyseReproductionService.listerLotsPourAnalyse());
        return "reproduction/analyse/index";
    }

    @GetMapping("/reproduction/analyse/lots/{lotId}")
    public String analyserLot(@PathVariable Long lotId, Model model) {
        AnalyseReproductionLotDTO analyse = analyseReproductionService.analyserDTO(lotId);

        model.addAttribute("analyse", analyse);

        return "reproduction/analyse/analyseLot";
    }

    @PostMapping("/reproduction/analyse/generer/{lotId}")
    public String genererAnalyse(@PathVariable Long lotId) {
        AnalyseReproductionLotDTO analyse = analyseReproductionService.analyserDTO(lotId);
        analyseReproductionService.enregistrerAnalyse(analyse);

        return "redirect:/reproduction/analyse/lots/" + lotId;
    }
}