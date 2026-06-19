package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import com.madaporc.DTO.*;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class VenteService {
    private final VenteRepository repo;
    private final DetailVenteRepository detailRepo;
    private final ClientRepository clientRepo;
    private final LotPorcRepository lotRepo;
    private final PaiementRepository paiementRepo;
    private final MouvementLotPorcRepository mouvRepo;
    private final CycleProductionService cycleProductionService;

    public VenteService(VenteRepository repo, DetailVenteRepository detailRepo, ClientRepository clientRepo,
            LotPorcRepository lotRepo, PaiementRepository paiementRepo, MouvementLotPorcRepository mouvRepo,
            CycleProductionService cycleProductionService) {
        this.repo = repo;
        this.detailRepo = detailRepo;
        this.clientRepo = clientRepo;
        this.lotRepo = lotRepo;
        this.paiementRepo = paiementRepo;
        this.mouvRepo = mouvRepo;
        this.cycleProductionService = cycleProductionService;
    }

    public List<Vente> findAllVentes() {
        return repo.findAllByOrderByDateVenteDesc();
    }

    public void prepareVenteFormModel(Model model, Long id) {
        model.addAttribute("vente", new VenteDTO());
        model.addAttribute("clients", clientRepo.findAll());
        model.addAttribute("lots", lotRepo.findAll());
    }

    public String creer(VenteDTO dto, Long uid) {
        Vente v = new Vente();
        v.setClientId(dto.getClientId());
        v.setDateVente(dto.getDateVente() == null ? LocalDateTime.now() : dto.getDateVente());
        v.setStatutVente(dto.getStatutVente() == null ? "Brouillon" : dto.getStatutVente());
        v.setObservation(dto.getObservation());
        v.setCreatedBy(uid);
        v.setCreatedAt(LocalDateTime.now());
        v.setMontantTotal(BigDecimal.ZERO);
        v = repo.save(v);
        BigDecimal total = BigDecimal.ZERO;
        if (dto.getDetails() != null)
            for (DetailVenteDTO d : dto.getDetails()) {
                BigDecimal m = calculerMontantDetail(d.getNombrePorcsVendus(), d.getPoidsTotalKg(), d.getPrixKg(),
                        d.getPrixUnitaire());
                DetailVente det = new DetailVente();
                det.setVenteId(v.getId());
                det.setLotPorcId(d.getLotPorcId());
                det.setNombrePorcsVendus(d.getNombrePorcsVendus());
                det.setPoidsTotalKg(d.getPoidsTotalKg());
                det.setPrixKg(d.getPrixKg());
                det.setPrixUnitaire(d.getPrixUnitaire());
                det.setMontant(m);
                detailRepo.save(det);
                total = total.add(m);
            }
        v.setMontantTotal(total);
        repo.save(v);
        return null;
    }

    public String modifier(Long id, VenteDTO dto) {
        Vente v = repo.findById(id).orElse(null);
        if (v == null)
            return "Vente introuvable.";
        v.setClientId(dto.getClientId());
        v.setDateVente(dto.getDateVente());
        v.setStatutVente(dto.getStatutVente());
        v.setObservation(dto.getObservation());
        repo.save(v);
        return null;
    }

    public BigDecimal calculerMontantDetail(Integer nb, BigDecimal poids, BigDecimal prixKg, BigDecimal prixU) {
        if (poids != null && prixKg != null)
            return poids.multiply(prixKg);
        if (nb != null && prixU != null)
            return BigDecimal.valueOf(nb).multiply(prixU);
        return BigDecimal.ZERO;
    }

    public String validerVente(Long id) {
        Vente v = repo.findById(id).orElse(null);
        if (v == null)
            return "Vente introuvable.";
        for (DetailVente d : detailRepo.findByVenteId(id)) {
            mettreAJourLotApresVente(d);
            cycleProductionService.appliquerVenteAuCycle(d);
            creerMouvementLotApresVente(d, v.getCreatedBy());
        }
        v.setStatutVente("Validee");
        repo.save(v);
        return null;
    }

    public String annulerVente(Long id) {
        Vente v = repo.findById(id).orElse(null);
        if (v == null)
            return "Vente introuvable.";
        v.setStatutVente("Annulee");
        repo.save(v);
        return null;
    }

    public void mettreAJourLotApresVente(DetailVente d) {
        lotRepo.findById(d.getLotPorcId()).ifPresent(l -> {
            int a = l.getNombreActuel() == null ? 0 : l.getNombreActuel();
            int q = d.getNombrePorcsVendus() == null ? 0 : d.getNombrePorcsVendus();
            l.setNombreActuel(Math.max(0, a - q));
            lotRepo.save(l);
        });
    }

    public void creerMouvementLotApresVente(DetailVente d, Long uid) {
        MouvementLotPorc m = new MouvementLotPorc();
        m.setLotPorcId(d.getLotPorcId());
        m.setQuantite(d.getNombrePorcsVendus());
        m.setMotif("Vente");
        m.setDateMouvement(LocalDateTime.now());
        m.setCreatedBy(uid);
        m.setCreatedAt(LocalDateTime.now());
        mouvRepo.save(m);
    }
}
