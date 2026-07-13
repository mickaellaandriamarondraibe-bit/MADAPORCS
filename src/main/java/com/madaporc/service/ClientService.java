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
            return validation;
        }

        Client client = new Client();
        client.setNom(dto.getNom().trim());
        client.setTelephone(dto.getTelephone().replaceAll("\\s", ""));
        client.setAdresse(dto.getAdresse());

        clientRepository.save(client);
        return "redirect:/clients";
    }

    public String modifier(Long id, ClientDTO dto) {
        Client client = clientRepository.findById(id).orElse(null);
        if (client == null) {
            return "Client introuvable.";
        }

        dto.setId(id); // pour exclure le client courant du contrôle d'unicité
        String validation = valider(dto);
        if (validation != null) {
            return validation;
        }

        client.setNom(dto.getNom().trim());
        client.setTelephone(dto.getTelephone().replaceAll("\\s", ""));
        client.setAdresse(dto.getAdresse());

        clientRepository.save(client);
        return "redirect:/clients";
    }

    public String valider(ClientDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            return "Le nom du client est obligatoire.";
        }
        String tel = dto.getTelephone() != null ? dto.getTelephone().trim() : "";
        if (tel.isEmpty()) {
            return "Le téléphone est obligatoire.";
        }
        // Regle de gestion : le telephone ne contient que des chiffres (espaces tolérés).
        if (!tel.replaceAll("\\s", "").matches("\\d+")) {
            return "Le téléphone ne doit contenir que des chiffres.";
        }
        // Unicité du téléphone (hors le client en cours d'édition), sur la valeur
        // normalisée (sans espaces) pour ne pas contourner l'unicité avec des espaces.
        Client existant = clientRepository.findByTelephone(tel.replaceAll("\\s", "")).orElse(null);
        if (existant != null && !existant.getId().equals(dto.getId())) {
            return "Ce numéro de téléphone est déjà utilisé par un autre client.";
        }
        return null;
    }
}
