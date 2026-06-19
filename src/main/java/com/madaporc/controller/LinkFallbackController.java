package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class LinkFallbackController {

    @GetMapping("/")
    public String home() {
        return "redirect:/dashboard";
    }

    @GetMapping("/pesees")
    public String peseesAlias() {
        return "redirect:/lots/pesees";
    }

    @GetMapping("/lots/{id}")
    public String lotDetailAlias(@PathVariable Long id) {
        return "redirect:/lots/detail/" + id;
    }

    @GetMapping("/lots/pesees/{id}")
    public String peseeDetail(@PathVariable Long id) {
        return "lots/pesees/detail";
    }

    @GetMapping("/factures/{venteId}")
    public String factureDetailAlias(@PathVariable Long venteId) {
        return "redirect:/factures/detail/" + venteId;
    }

    @GetMapping("/roles")
    public String roles() {
        return "roles/list";
    }

    @GetMapping("/roles/form")
    public String roleForm() {
        return "roles/form";
    }

    @GetMapping("/roles/permissions")
    public String rolePermissions() {
        return "roles/permissions";
    }

    @GetMapping("/roles/{id}")
    public String roleDetail(@PathVariable Long id) {
        return "roles/detail";
    }

    @GetMapping("/cycles/{id}")
    public String cycleDetail(@PathVariable Long id) {
        return "cycles/detail";
    }

    @GetMapping("/melanges/form")
    public String melangeForm() {
        return "melanges/form";
    }

    @GetMapping("/melanges/{id}")
    public String melangeDetail(@PathVariable Long id) {
        return "melanges/detail";
    }

    @GetMapping("/employes/form")
    public String employeForm() {
        return "employes/form";
    }

    @GetMapping("/employes/{id}")
    public String employeDetail(@PathVariable Long id) {
        return "employes/detail";
    }

    @GetMapping("/presences/form")
    public String presenceForm() {
        return "presences/form";
    }

    @GetMapping("/presences/{id}")
    public String presenceDetail(@PathVariable Long id) {
        return "presences/detail";
    }

    @GetMapping("/salaires/form")
    public String salaireForm() {
        return "salaires/form";
    }

    @GetMapping("/salaires/{id}")
    public String salaireDetail(@PathVariable Long id) {
        return "salaires/detail";
    }

    @GetMapping("/distributions/form")
    public String distributionForm() {
        return "distributions/form";
    }

    @GetMapping("/distributions/{id}")
    public String distributionDetail(@PathVariable Long id) {
        return "distributions/detail";
    }

    @GetMapping("/vaccinations/form")
    public String vaccinationForm() {
        return "vaccinations/form";
    }

    @GetMapping("/vaccinations/{id}")
    public String vaccinationDetail(@PathVariable Long id) {
        return "vaccinations/detail";
    }
}
