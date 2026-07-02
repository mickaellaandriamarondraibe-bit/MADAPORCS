package com.madaporc.controller;

import com.madaporc.dto.VenteDTO;
<<<<<<< HEAD
import com.madaporc.service.VenteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
=======
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.service.ClientService;
import com.madaporc.service.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
>>>>>>> 5e4d8bf (correction)

@Controller
public class VenteController {

<<<<<<< HEAD
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
=======
    @Autowired
    private VenteService venteService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private LotPorcRepository lotPorcRepository;

    @GetMapping("/ventes")
    public String listVentes(Model model) {

        model.addAttribute("listeVentes", venteService.findAll());

        return "commerce/ventes";
    }

    @GetMapping("/ventes/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {

        VenteDTO dto;

        if (id == null) {
            dto = new VenteDTO();
        } else {
            dto = venteService.getForm(id);

            if (dto == null) {
                return "redirect:/ventes";
            }
        }

        model.addAttribute("vente", dto);
        model.addAttribute("clients", clientService.findAll());
        model.addAttribute("lots", lotPorcRepository.findAll());

        return "commerce/formVente";
    }

    @PostMapping("/ventes/save")
    public String save(@ModelAttribute("vente") VenteDTO dto, Model model) {

        String validation = venteService.validerDonneesVente(dto);

        if (!validation.equals("redirect:/ventes/form")) {
            return validation;
        }

        return venteService.creerVente(dto);
    }

    @PostMapping("/ventes/valider/{id}")
    public String valider(@PathVariable Long id) {

        return venteService.validerVente(id);
    }

    @PostMapping("/ventes/annuler/{id}")
    public String annuler(@PathVariable Long id) {

        return venteService.annulerVente(id);
    }

    @GetMapping("/ventes/{id}")
    public String detail(@PathVariable Long id, Model model) {

        if (venteService.findById(id) == null) {
            return "redirect:/ventes";
        }

        model.addAttribute("vente", venteService.findById(id));
        model.addAttribute("details", venteService.getDetailsVente(id));

        return "commerce/detailVente";
    }

}
>>>>>>> 5e4d8bf (correction)
