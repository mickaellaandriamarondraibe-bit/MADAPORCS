package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.madaporc.service.AnalyseReproductionService;
import com.madaporc.dto.AnalyseReproductionLotDTO;
import com.madaporc.model.LotPorc;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/reproduction/analyse")
public class AnalyseReproductionController {
    @Autowired
    private AnalyseReproductionService analyseReproductionService;

    @GetMapping("/lots/{lotId}")
    public String analyseLot(@PathVariable Long lotId, Model model) {
        LotPorc lot = analyseReproductionService.getLotPorcRepository().findById(lotId)
            .orElseThrow(() -> new RuntimeException("Lot non trouvé"));
        
        AnalyseReproductionLotDTO analyse = analyseReproductionService.analyserDTO(lotId);
        
        model.addAttribute("lot", lot);
        model.addAttribute("analyse", analyse);
        model.addAttribute("tauxAptitudeGlobal", analyseReproductionService.calculerTauxAptitudeGlobal(analyse));
        model.addAttribute("tauxRecommande", analyseReproductionService.calculerTauxRecommande(analyse));
        model.addAttribute("tauxFertilite", analyseReproductionService.calculerTauxFertiliteObserve(analyse));
        model.addAttribute("decision", analyseReproductionService.genererDecision(analyse));
        
        return "analyse/analyseLot";
    }

    @PostMapping("/lots/generer/{lotId}")
    public String genererAnalyse(@PathVariable Long lotId) {
        AnalyseReproductionLotDTO analyse = analyseReproductionService.analyserDTO(lotId);
        analyseReproductionService.enregistrerAnalyse(analyse);
        return "redirect:/reproduction/analyse/lots/" + lotId;
    }
}