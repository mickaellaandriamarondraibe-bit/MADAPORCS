package com.madaporc.service;

import com.madaporc.model.DetailVente;
import com.madaporc.model.Vente;
import com.madaporc.repository.DetailVenteRepository;
import com.madaporc.repository.VenteRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import com.madaporc.dto.*;

@Service
public class VenteService {
    protected final VenteRepository venteRepository;
    protected final DetailVenteRepository detailVenteRepository;
    protected final LotPorcService lotPorcService;

    public VenteService(VenteRepository venteRepository, DetailVenteRepository detailVenteRepository, LotPorcService lotPorcService) {
        this.venteRepository = venteRepository;
        this.detailVenteRepository = detailVenteRepository;
        this.lotPorcService = lotPorcService;
    }

    public List<Vente> findAll() {
        List<Vente> ventes = venteRepository.findAll();
        return ventes;
    }

    public Vente findById(Long id) {
        return venteRepository.findById(id).orElse(null);
    }

    public VenteDTO getForm(Long id) {
        Vente vente = findById(id);
        if (vente == null) {
            return null;
        }

        VenteDTO venteDTO = new VenteDTO();
        venteDTO.setId(vente.getId());
        venteDTO.setDateVente(vente.getDateVente());
        venteDTO.setStatut(vente.getStatut());

        return venteDTO;
    }

    public String creerVente(VenteDTO venteDTO) {
        Vente vente = new Vente();
        vente.setDateVente(venteDTO.getDateVente());
        vente.setMontantTotal(calculerMontantTotal(venteDTO));
        if(venteDTO.getStatut() == null) {
            vente.setStatut("brouillon");
        } else {
            vente.setStatut(venteDTO.getStatut());
        }

        venteRepository.save(vente);
        return "redirect:/ventes";
    }

    public List<DetailVente> getDetailsVente(Long venteID) {
        return detailVenteRepository.findByVenteId(venteID);
    }

    public String validerVente(Long venteId) {
        VenteDTO venteDTO = getForm(venteId);

        if (venteDTO == null) {
            return "redirect:/ventes?error=venteNotFound";
        }

        LotDetailDTO detailLotDTO = lotPorcService.getDetailLot(venteDTO.getLotId());
        if(detailLotDTO == null) {
            return "redirect:/ventes?error=lotNotFound";
        }

        if(venteDTO.getQuantite() > detailLotDTO.getEffectifActuel()) {
            return "redirect:/ventes?error=quantiteInsuffisante";
        }

        venteDTO.setStatut("valide");
        creerVente(venteDTO);
        return "redirect:/ventes";
    }

    public String annulerVente(Long venteId) {
        VenteDTO venteDTO = getForm(venteId);

        if (venteDTO == null) {
            return "redirect:/ventes?error=venteNotFound";
        }

        venteDTO.setStatut("annule");
        creerVente(venteDTO);
        return "redirect:/ventes";
    }

    public BigDecimal calculerMontantTotal(VenteDTO venteDTO) {
        return venteDTO.getPrixUnitaire().multiply(new BigDecimal(venteDTO.getQuantite()));
    }

    String validerDonneesVente(VenteDTO venteDTO) {
        if(venteDTO.getClientId() == null) {
            return "redirect:/ventes/form?error=clientIdManquant";
        }

        if(venteDTO.getLotId() == null) {
            return "redirect:/ventes/form?error=lotIdManquant";
        }

        if(venteDTO.getQuantite() == null || venteDTO.getQuantite() <= 0) {
            return "redirect:/ventes/form?error=quantiteInvalide";
        }

        if(venteDTO.getPrixUnitaire() == null || venteDTO.getPrixUnitaire().compareTo(BigDecimal.ZERO) <= 0) {
            return "redirect:/ventes/form?error=prixUnitaireInvalide";
        }

        return "redirect:/ventes/form";
    }
}