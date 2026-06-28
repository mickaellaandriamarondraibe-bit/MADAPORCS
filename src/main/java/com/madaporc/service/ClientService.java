package com.madaporc.service;

import com.madaporc.dto.ClientDTO;
import com.madaporc.model.Client;
import com.madaporc.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAll() {
        return clientRepository.findAllByOrderByNomAsc();
    }

    public Client findById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    public ClientDTO getForm(Long id) {
        ClientDTO dto = new ClientDTO();
        if (id != null) {
            Client client = findById(id);
            if (client != null) {
                dto.setId(client.getId());
                dto.setNom(client.getNom());
                dto.setTelephone(client.getTelephone());
                dto.setAdresse(client.getAdresse());
            }
        }
        return dto;
    }

    public String creer(ClientDTO dto) {
        String validation = valider(dto);
        if (validation != null) {
            return "error";
        }

        Client client = new Client();
        client.setNom(dto.getNom());
        client.setTelephone(dto.getTelephone());
        client.setAdresse(dto.getAdresse());

        clientRepository.save(client);
        return "redirect:/clients";
    }

    public String modifier(Long id, ClientDTO dto) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return "error";
        }

        String validation = valider(dto);
        if (validation != null) {
            return "error";
        }

        client.setNom(dto.getNom());
        client.setTelephone(dto.getTelephone());
        client.setAdresse(dto.getAdresse());

        clientRepository.save(client);
        return "redirect:/clients";
    }

    public String valider(ClientDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            return "error";
        }
        return null;
    }
}
