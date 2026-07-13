package com.madaporc.controller;

import com.madaporc.repository.AlerteReproductionRepository;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * Données communes injectées dans toutes les vues (menu latéral) :
 * le badge du menu « Alertes » = nombre d'alertes non traitées.
 */
@ControllerAdvice
public class GlobalModelAdvice {

    private final AlerteReproductionRepository alerteRepository;

    public GlobalModelAdvice(AlerteReproductionRepository alerteRepository) {
        this.alerteRepository = alerteRepository;
    }

    @ModelAttribute("nbAlertes")
    public long nbAlertes() {
        return alerteRepository.countByStatutNot("TRAITEE");
    }
}
