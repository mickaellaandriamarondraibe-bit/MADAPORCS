package com.madaporc.controller;

import com.madaporc.dto.ClientDTO;
import com.madaporc.model.Client;
import com.madaporc.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public String listClients(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        return "commerce/clients";
    }

    @GetMapping("/clients/form")
    public String showClientForm(@RequestParam(required = false) Long id, Model model) {
        ClientDTO dto = clientService.getForm(id);
        model.addAttribute("clientDTO", dto);
        return "commerce/formClient";
    }

    @PostMapping("/clients/save")
    public String saveClient(@ModelAttribute ClientDTO dto, Model model) {
        String result;

        if (dto.getId() != null) {
            result = clientService.modifier(dto.getId(), dto);
        } else {
            result = clientService.creer(dto);
        }

        if ("error".equals(result)) {
            model.addAttribute("clientDTO", dto);
            model.addAttribute("error", "Le nom est obligatoire");
            return "commerce/formClient";
        }

        return result;
    }
}
