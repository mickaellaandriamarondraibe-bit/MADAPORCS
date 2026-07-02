package com.madaporc.controller;

import com.madaporc.dto.VenteDTO;
import com.madaporc.service.VenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VenteController {

	private final VenteService venteService;

	public VenteController(VenteService venteService) {
		this.venteService = venteService;
	}

	@GetMapping("/ventes")
	public String listVentes(Model model) {
		model.addAttribute("ventes", venteService.listerVentes());
		return "commerce/ventes";
	}

	@GetMapping("/ventes/form")
	public String formVente(@RequestParam(required = false) Long id, Model model) {
		model.addAttribute("vente", venteService.preparerFormulaire(id));
		model.addAttribute("clients", venteService.listerClients());
		model.addAttribute("lots", venteService.listerLots());
		return "commerce/formVente";
	}

	@PostMapping("/ventes/save")
	public String saveVente(@ModelAttribute VenteDTO dto) {
		return "redirect:/ventes/" + venteService.creerVente(dto).getId();
	}

	@GetMapping("/ventes/{id}")
	public String detailVente(@PathVariable Long id, Model model) {
		model.addAttribute("vente", venteService.chargerVente(id));
		return "commerce/detailVente";
	}

	@PostMapping("/ventes/valider/{id}")
	public String validerVente(@PathVariable Long id) {
		venteService.validerVente(id);
		return "redirect:/ventes/" + id;
	}

    @PostMapping("/ventes/{id}/annuler")
    public String annulerVente(@PathVariable Long id) {
        venteService.annulerVente(id);
        return "redirect:/ventes";
    }

}
