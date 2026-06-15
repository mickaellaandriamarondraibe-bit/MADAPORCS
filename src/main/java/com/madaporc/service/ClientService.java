package com.madaporc.service;

import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.ClientDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;
import org.springframework.ui.Model;


@Service
public class ClientService {
    private final ClientRepository repo;
    private final VenteRepository venteRepo;

    public ClientService(ClientRepository repo, VenteRepository venteRepo) {
        this.repo = repo;
        this.venteRepo = venteRepo;
    }

    public List<Client> rechercherClients(String motCle, String typeClient) {
        if (typeClient != null && !typeClient.isBlank())
            return repo.findByTypeClient(typeClient);
        if (motCle != null && !motCle.isBlank())
            return repo.findByNomContainingIgnoreCase(motCle);
        return repo.findAll();
    }

    public String creer(ClientDTO dto) {
        String e = validerClient(dto);
        if (e != null)
            return e;
        Client c = conv(dto);
        c.setCreatedAt(LocalDateTime.now());
        repo.save(c);
        return null;
    }

    public String modifier(Long id, ClientDTO dto) {
        String e = validerClient(dto);
        if (e != null)
            return e;
        Client c = conv(dto);
        c.setId(id);
        c.setUpdatedAt(LocalDateTime.now());
        repo.save(c);
        return null;
    }

    public Optional<Client> findClient(Long id) {
        return repo.findById(id);
    }

    public List<Vente> getVentesClient(Long clientId) {
        return venteRepo.findByClientIdOrderByDateVenteDesc(clientId);
    }

    public String validerClient(ClientDTO dto) {
        if (dto.getNom() == null || dto.getNom().isBlank())
            return "Nom obligatoire.";
        return null;
    }

    private Client conv(ClientDTO d) {
        Client c = new Client();
        c.setId(d.getId());
        c.setNom(d.getNom());
        c.setTypeClient(d.getTypeClient());
        c.setContact(d.getContact());
        c.setEmail(d.getEmail());
        c.setAdresse(d.getAdresse());
        return c;
    }


    public void prepareClientFormModel(Model model, Long id) {
    ClientDTO dto = new ClientDTO();

    if (id != null) {
        repo.findById(id).ifPresent(client -> {
            dto.setId(client.getId());
            dto.setNom(client.getNom());
            dto.setTypeClient(client.getTypeClient());
            dto.setContact(client.getContact());
            dto.setEmail(client.getEmail());
            dto.setAdresse(client.getAdresse());
        });
    }

    model.addAttribute("client", dto);
}
}
