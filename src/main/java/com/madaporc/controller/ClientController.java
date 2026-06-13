package com.madaporc.controller;

import com.madaporc.DTO.ClientDTO;
import com.madaporc.service.ClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/clients")
    public String listClients(@RequestParam(required = false) String motCle,
                              @RequestParam(required = false) String typeClient,
                              Model model) {
        model.addAttribute("titre", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("clients", clientService.rechercherClients(motCle, typeClient));
        model.addAttribute("client", new ClientDTO());
        model.addAttribute("motCle", motCle);
        model.addAttribute("typeClient", typeClient);
        return "commerce/clients";
    }

    @GetMapping("/clients/form")
    public String showClientForm(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("titre", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("client", id == null ? new ClientDTO() : clientService.getClientDto(id));
        model.addAttribute("clients", clientService.rechercherClients(null, null));
        return "commerce/clients";
    }

    @PostMapping("/clients/save")
    public String saveClient(@ModelAttribute ClientDTO dto, Model model) {
        String message = dto.getId() == null ? clientService.creer(dto) : clientService.modifier(dto.getId(), dto);
        model.addAttribute("message", message);
        return "redirect:/clients";
    }

    @GetMapping("/clients/{id}")
    public String detailClient(@PathVariable Long id, Model model) {
        model.addAttribute("titre", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("referenceFigma", "Gestion des Clients - MADAPORC / GestPorc");
        model.addAttribute("client", clientService.findClient(id).orElseThrow());
        model.addAttribute("ventes", clientService.getVentesClient(id));
        return "commerce/clients";
    }
}
