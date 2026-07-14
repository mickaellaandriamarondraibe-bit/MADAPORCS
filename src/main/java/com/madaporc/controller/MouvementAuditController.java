package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.madaporc.service.MouvementAuditService;

// Page d'audit : historique de tous les mouvements (lots + stock).
@Controller
public class MouvementAuditController {

    private final MouvementAuditService mouvementAuditService;

    public MouvementAuditController(MouvementAuditService mouvementAuditService) {
        this.mouvementAuditService = mouvementAuditService;
    }

    @GetMapping("/mouvements")
    public String liste(
            @RequestParam(required = false, defaultValue = "tout") String filtre,
            Model model) {
        model.addAttribute("mouvements", mouvementAuditService.listerMouvements(filtre));
        model.addAttribute("filtreActif", filtre);
        return "mouvements/liste";
    }
}
