package com.madaporc.service;

import com.madaporc.DTO.FactureDTO;
import com.madaporc.DTO.PaiementDTO;
import com.madaporc.model.Facture;
import com.madaporc.model.Paiement;
import com.madaporc.model.Vente;
import com.madaporc.repository.FactureRepository;
import com.madaporc.repository.PaiementRepository;
import com.madaporc.repository.VenteRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PaiementFactureService {

    private final FactureRepository factureRepository;
    private final PaiementRepository paiementRepository;
    private final VenteRepository venteRepository;

    public PaiementFactureService(FactureRepository factureRepository,
                                  PaiementRepository paiementRepository,
                                  VenteRepository venteRepository) {
        this.factureRepository = factureRepository;
        this.paiementRepository = paiementRepository;
        this.venteRepository = venteRepository;
    }

    public FactureDTO getFacture(Long venteId) {
        FactureDTO dto = new FactureDTO();
        factureRepository.findByVente_Id(venteId).ifPresent(facture -> dto.setId(facture.getId()));
        return dto;
    }

    public List<Paiement> getPaiements(Long venteId) {
        return paiementRepository.findByVente_IdOrderByDatePaiementDesc(venteId);
    }

    public String enregistrerPaiement(PaiementDTO dto) {
        Paiement paiement = dto.getId() == null ? new Paiement() : paiementRepository.findById(dto.getId()).orElseThrow();
        Vente vente = venteRepository.findById(dto.getVenteId()).orElseThrow();

        paiement.setVente(vente);
        paiement.setMontant(dto.getMontant());
        paiement.setModePaiement(dto.getModePaiement());
        paiement.setReference(dto.getReference());
        paiement.setDatePaiement(dto.getDatePaiement() == null ? LocalDateTime.now() : dto.getDatePaiement());
        paiementRepository.save(paiement);
        return "Paiement enregistre avec succes.";
    }

    public BigDecimal calculerResteAPayer(Long venteId) {
        Vente vente = venteRepository.findById(venteId).orElseThrow();
        BigDecimal totalPaiements = paiementRepository.findByVente_IdOrderByDatePaiementDesc(venteId)
                .stream()
                .map(Paiement::getMontant)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return vente.getMontantTotal().subtract(totalPaiements);
    }

    public String genererFacture(Long venteId) {
        Vente vente = venteRepository.findById(venteId).orElseThrow();
        Facture facture = factureRepository.findByVente_Id(venteId).orElseGet(Facture::new);

        facture.setVente(vente);
        facture.setNumeroFacture(genererNumeroFacture());
        facture.setDateFacture(LocalDateTime.now());
        facture.setMontantTotal(vente.getMontantTotal());
        factureRepository.save(facture);
        return "Facture generee avec succes.";
    }

    public String genererNumeroFacture() {
        return "FAC-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }

    public boolean ventePayeeTotalement(Long venteId) {
        return calculerResteAPayer(venteId).compareTo(BigDecimal.ZERO) <= 0;
    }
}
