package com.madaporc.service;

import com.madaporc.DTO.ClientDTO;
import com.madaporc.model.Client;
import com.madaporc.model.Vente;
import com.madaporc.repository.ClientRepository;
import com.madaporc.repository.VenteRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final VenteRepository venteRepository;

    public ClientService(ClientRepository clientRepository, VenteRepository venteRepository) {
        this.clientRepository = clientRepository;
        this.venteRepository = venteRepository;
    }

    public List<Client> rechercherClients(String motCle, String typeClient) {
        if (motCle != null && !motCle.trim().isEmpty()) {
            String recherche = motCle.trim();
            return clientRepository.findByNomContainingIgnoreCaseOrEmailContainingIgnoreCaseOrContactContainingIgnoreCase(
                    recherche,
                    recherche,
                    recherche
            );
        }

        if (typeClient != null && !typeClient.trim().isEmpty()) {
            return clientRepository.findByTypeClient(typeClient.trim());
        }

        return clientRepository.findAll();
    }

    public String creer(ClientDTO dto) {
        Client client = new Client();
        appliquerDto(client, dto);
        client.setCreatedAt(LocalDateTime.now());
        client.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(client);
        return "Client cree avec succes.";
    }

    public String modifier(Long id, ClientDTO dto) {
        Client client = clientRepository.findById(id).orElseThrow();
        appliquerDto(client, dto);
        client.setUpdatedAt(LocalDateTime.now());
        clientRepository.save(client);
        return "Client modifie avec succes.";
    }

    public Optional<Client> findClient(Long id) {
        return clientRepository.findById(id);
    }

    public ClientDTO getClientDto(Long id) {
        Client client = clientRepository.findById(id).orElseThrow();
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setTypeClient(client.getTypeClient());
        dto.setContact(client.getContact());
        dto.setEmail(client.getEmail());
        dto.setAdresse(client.getAdresse());
        return dto;
    }

    public List<Vente> getVentesClient(Long clientId) {
        return venteRepository.findByClient_IdOrderByDateVenteDesc(clientId);
    }

    public String validerClient(ClientDTO dto) {
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) {
            return "Le nom du client est obligatoire.";
        }
        return "Client valide.";
    }

    private void appliquerDto(Client client, ClientDTO dto) {
        client.setNom(dto.getNom());
        client.setTypeClient(dto.getTypeClient());
        client.setContact(dto.getContact());
        client.setEmail(dto.getEmail());
        client.setAdresse(dto.getAdresse());
    }
}
