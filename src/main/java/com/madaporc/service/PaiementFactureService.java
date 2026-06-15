package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.FactureDTO;
import com.madaporc.DTO.PaiementDTO;
import com.madaporc.model.Facture;
import com.madaporc.model.Paiement;
import com.madaporc.model.Vente;
import com.madaporc.repository.FactureRepository;
import com.madaporc.repository.PaiementRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class PaiementFactureService {

    private final VenteRepository venteRepo;
    private final PaiementRepository paiementRepo;
    private final FactureRepository factureRepo;

    public PaiementFactureService(
            VenteRepository venteRepo,
            PaiementRepository paiementRepo,
            FactureRepository factureRepo
    ) {
        this.venteRepo = venteRepo;
        this.paiementRepo = paiementRepo;
        this.factureRepo = factureRepo;
    }


    public FactureDTO getFacture(Long venteId) {
        FactureDTO dto = new FactureDTO();

        if (venteId == null) {
            return dto;
        }

        Vente vente = venteRepo.findById(venteId).orElse(null);

        dto.setVenteId(venteId);

        if (vente != null) {
            dto.setMontantTotal(nz(vente.getMontantTotal()));
            dto.setMontantPaye(calculerMontantPaye(venteId));
            dto.setResteAPayer(calculerResteAPayer(venteId));
        }

        factureRepo.findByVenteId(venteId).ifPresent(facture -> {
            dto.setId(facture.getId());
            dto.setNumeroFacture(facture.getNumeroFacture());
            dto.setDateFacture(facture.getDateFacture());
        });

        return dto;
    }


    public String enregistrerPaiement(PaiementDTO dto) {
        String erreur = validerPaiement(dto);

        if (erreur != null) {
            return erreur;
        }

        Paiement paiement = new Paiement();

        paiement.setVenteId(dto.getVenteId());
        paiement.setMontant(dto.getMontant());
        paiement.setModePaiement(dto.getModePaiement());
        paiement.setReference(dto.getReference());
        paiement.setDatePaiement(
                dto.getDatePaiement() == null
                        ? LocalDateTime.now()
                        : dto.getDatePaiement()
        );

        paiementRepo.save(paiement);

        return null;
    }


    public BigDecimal calculerMontantPaye(Long venteId) {
        if (venteId == null) {
            return BigDecimal.ZERO;
        }

        return nz(paiementRepo.sumMontantByVenteId(venteId));
    }


    public BigDecimal calculerResteAPayer(Long venteId) {
        Vente vente = venteRepo.findById(venteId).orElse(null);

        if (vente == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal montantTotal = nz(vente.getMontantTotal());
        BigDecimal montantPaye = calculerMontantPaye(venteId);

        return montantTotal.subtract(montantPaye);
    }


    public String genererFacture(Long venteId) {
        if (venteId == null) {
            return "Vente obligatoire.";
        }

        if (factureRepo.findByVenteId(venteId).isPresent()) {
            return null;
        }

        Vente vente = venteRepo.findById(venteId).orElse(null);

        if (vente == null) {
            return "Vente introuvable.";
        }

        Facture facture = new Facture();

        facture.setVenteId(venteId);
        facture.setNumeroFacture(genererNumeroFacture());
        facture.setDateFacture(LocalDateTime.now());
        facture.setMontantTotal(nz(vente.getMontantTotal()));

        factureRepo.save(facture);

        return null;
    }


    public String genererNumeroFacture() {
        String numero = "FAC-" + System.currentTimeMillis();

        if (factureRepo.existsByNumeroFacture(numero)) {
            return numero + "-1";
        }

        return numero;
    }


    public boolean ventePayeeTotalement(Long venteId) {
        return calculerResteAPayer(venteId).compareTo(BigDecimal.ZERO) <= 0;
    }


    public String validerPaiement(PaiementDTO dto) {
        if (dto == null) {
            return "Paiement obligatoire.";
        }

        if (dto.getVenteId() == null) {
            return "Vente obligatoire.";
        }

        if (!venteRepo.existsById(dto.getVenteId())) {
            return "Vente introuvable.";
        }

        if (dto.getMontant() == null || dto.getMontant().compareTo(BigDecimal.ZERO) <= 0) {
            return "Montant invalide.";
        }

        if (dto.getMontant().compareTo(calculerResteAPayer(dto.getVenteId())) > 0) {
            return "Le paiement dépasse le reste à payer.";
        }

        if (dto.getModePaiement() == null || dto.getModePaiement().isBlank()) {
            return "Mode de paiement obligatoire.";
        }

        return null;
    }


    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}