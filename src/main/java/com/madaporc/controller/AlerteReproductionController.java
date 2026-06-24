package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ch.qos.logback.core.model.Model;

@Controller
public class AlerteReproductionController {
    @GetMapping("/reproduction/alertes")
    public String listAlertes(Model model) {
        return "test";
    }

    @PostMapping("/reproduction/alertes/{id}/lire")
    public String marquerCommeLue(@PathVariable Long id) {
        return "test";
    }

    @PostMapping("/reproduction/alertes/{id}/traiter")
    public String traiter(@PathVariable Long id) {
        return "test";
    }

}