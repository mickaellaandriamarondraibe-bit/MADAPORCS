package com.madaporc.service;

import com.madaporc.DTO.VenteDTO;
import com.madaporc.model.DetailVente;
import com.madaporc.model.Vente;
import com.madaporc.repository.ClientRepository;
import com.madaporc.repository.VenteRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class VenteService {

    private final VenteRepository venteRepository;
    private final ClientRepository clientRepository;

    public VenteService(VenteRepository venteRepository, ClientRepository clientRepository) {
        this.venteRepository = venteRepository;
        this.clientRepository = clientRepository;
    }

    public List<Vente> findAllVentes() {
        return venteRepository.findAll();
    }

    public void prepareVenteFormModel(Model model, Long id) {
        model.addAttribute("vente", id == null ? new VenteDTO() : getVenteDto(id));
        model.addAttribute("clients", clientRepository.findAll());
        model.addAttribute("statutsVente", List.of("Brouillon", "Validee", "Annulee"));
    }

    public String creer(VenteDTO dto, Long utilisateurId) {
        Vente vente = new Vente();
        appliquerDto(vente, dto);
        vente.setMontantTotal(BigDecimal.ZERO);
        vente.setStatutVente(dto.getStatutVente() == null ? "Brouillon" : dto.getStatutVente());
        vente.setCreatedBy(utilisateurId);
        vente.setCreatedAt(LocalDateTime.now());
        venteRepository.save(vente);
        return "Vente creee avec succes.";
    }

    public String modifier(Long id, VenteDTO dto) {
        Vente vente = venteRepository.findById(id).orElseThrow();
        appliquerDto(vente, dto);
        if (dto.getStatutVente() != null) {
            vente.setStatutVente(dto.getStatutVente());
        }
        venteRepository.save(vente);
        return "Vente modifiee avec succes.";
    }

    public BigDecimal calculerMontantDetail(Integer nbPorcs,
                                            BigDecimal poidsTotalKg,
                                            BigDecimal prixKg,
                                            BigDecimal prixUnitaire) {
        if (poidsTotalKg != null && prixKg != null) {
            return poidsTotalKg.multiply(prixKg);
        }
        if (nbPorcs != null && prixUnitaire != null) {
            return BigDecimal.valueOf(nbPorcs).multiply(prixUnitaire);
        }
        return BigDecimal.ZERO;
    }

    public String validerVente(Long venteId) {
        Vente vente = venteRepository.findById(venteId).orElseThrow();
        vente.setStatutVente("Validee");
        venteRepository.save(vente);
        return "Vente validee avec succes.";
    }

    public String annulerVente(Long venteId) {
        Vente vente = venteRepository.findById(venteId).orElseThrow();
        vente.setStatutVente("Annulee");
        venteRepository.save(vente);
        return "Vente annulee avec succes.";
    }

    public void mettreAJourLotApresVente(DetailVente detail) {
    }

    public void creerMouvementLotApresVente(DetailVente detail, Long utilisateurId) {
    }

    private VenteDTO getVenteDto(Long id) {
        Vente vente = venteRepository.findById(id).orElseThrow();
        VenteDTO dto = new VenteDTO();
        dto.setId(vente.getId());
        dto.setClientId(vente.getClient().getId());
        dto.setDateVente(vente.getDateVente());
        dto.setStatutVente(vente.getStatutVente());
        dto.setObservation(vente.getObservation());
        return dto;
    }

    private void appliquerDto(Vente vente, VenteDTO dto) {
        vente.setClient(clientRepository.findById(dto.getClientId()).orElseThrow());
        vente.setDateVente(dto.getDateVente() == null ? LocalDateTime.now() : dto.getDateVente());
        vente.setObservation(dto.getObservation());
    }
}
