package com.madaporc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.madaporc.DTO.ClientDTO;
import com.madaporc.service.ClientService;

@Controller
@RequestMapping("/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String liste(
            @RequestParam(required = false) String motCle,
            @RequestParam(required = false) String typeClient,
            Model model
    ) {
        model.addAttribute("clients", clientService.rechercherClients(motCle, typeClient));
        model.addAttribute("motCle", motCle);
        model.addAttribute("typeClient", typeClient);

        return "clients/list";
    }

    @GetMapping("/form")
    public String form(
            @RequestParam(required = false) Long id,
            Model model
    ) {
        clientService.prepareClientFormModel(model, id);
        return "clients/form";
    }

    @PostMapping("/save")
    public String save(
            @ModelAttribute("client") ClientDTO dto,
            Model model
    ) {
        String erreur = dto.getId() == null
                ? clientService.creer(dto)
                : clientService.modifier(dto.getId(), dto);

        if (erreur != null) {
            model.addAttribute("erreur", erreur);
            model.addAttribute("client", dto);
            return "clients/form";
        }

        return "redirect:/clients";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("client", clientService.findClient(id).orElse(null));
        return "clients/detail";
    }
}