package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.MouvementLotDTO;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class MouvementLotService {
    private final MouvementLotPorcRepository repo;
    private final LotPorcRepository lotRepo;

    public MouvementLotService(MouvementLotPorcRepository repo, LotPorcRepository lotRepo) {
        this.repo = repo;
        this.lotRepo = lotRepo;
    }

    public String ajouterMouvement(MouvementLotDTO dto, Long utilisateurId) {
        String e = validerMouvementLot(dto);
        if (e != null)
            return e;
        MouvementLotPorc m = new MouvementLotPorc();
        m.setLotPorcId(dto.getLotPorcId());
        m.setTypeMouvementLotId(dto.getTypeMouvementLotId());
        m.setQuantite(dto.getQuantite());
        m.setDateMouvement(dto.getDateMouvement() == null ? LocalDateTime.now() : dto.getDateMouvement());
        m.setMotif(dto.getMotif());
        m.setCreatedBy(utilisateurId);
        m.setCreatedAt(LocalDateTime.now());
        repo.save(m);
        mettreAJourEffectifLot(dto.getLotPorcId(), dto.getQuantite(), String.valueOf(dto.getTypeMouvementLotId()));
        return null;
    }

    public List<MouvementLotPorc> findMouvements(Long lotId) {
        return repo.findByLotPorcIdOrderByDateMouvementDesc(lotId);
    }

    public List<MouvementLotPorc> rechercherMouvements(String motCle, Long typeMouvementId, LocalDate date, Long lotId) {
        return repo.findAllByOrderByDateMouvementDesc().stream()
                .filter(m -> lotId == null || lotId.equals(m.getLotPorcId()))
                .filter(m -> typeMouvementId == null || typeMouvementId.equals(m.getTypeMouvementLotId()))
                .filter(m -> date == null || m.getDateMouvement() != null && m.getDateMouvement().toLocalDate().equals(date))
                .filter(m -> motCle == null || motCle.isBlank() || matchesMouvementSearch(m, motCle))
                .toList();
    }

    private boolean matchesMouvementSearch(MouvementLotPorc mouvement, String motCle) {
        String normalized = motCle.trim().toLowerCase();
        boolean motifMatch = mouvement.getMotif() != null && mouvement.getMotif().toLowerCase().contains(normalized);
        Long lotPorcId = mouvement.getLotPorcId();
        boolean lotMatch = lotPorcId != null && lotRepo.findById(lotPorcId)
                .map(l -> l.getCodeLot() != null && l.getCodeLot().toLowerCase().contains(normalized))
                .orElse(false);
        return motifMatch || lotMatch;
    }

    public String verifierQuantiteDisponible(Long lotId, Integer quantite, String typeMouvement) {
        if (lotId == null) {
            return "Lot introuvable.";
        }
        LotPorc l = lotRepo.findById(lotId).orElse(null);
        if (l == null)
            return "Lot introuvable.";
        if (quantite == null || quantite <= 0)
            return "Quantité invalide.";
        if (typeMouvement != null && typeMouvement.toLowerCase().matches(".*(sortie|perte|deces|décès|vente).*")
                && quantite > l.getNombreActuel())
            return "Quantité supérieure au stock.";
        return null;
    }

    public void mettreAJourEffectifLot(Long lotId, Integer q, String type) {
        if (lotId == null) {
            return;
        }
        LotPorc l = lotRepo.findById(lotId).orElse(null);
        if (l == null || q == null)
            return;
        int a = l.getNombreActuel() == null ? 0 : l.getNombreActuel();
        if (type != null && type.toLowerCase().matches(".*(sortie|perte|deces|décès|vente|2|3|4).*"))
            l.setNombreActuel(Math.max(0, a - q));
        else
            l.setNombreActuel(a + q);
        lotRepo.save(l);
    }

    public String validerMouvementLot(MouvementLotDTO dto) {
        if (dto == null)
            return "Mouvement obligatoire.";
        if (dto.getLotPorcId() == null)
            return "Lot obligatoire.";
        if (dto.getTypeMouvementLotId() == null)
            return "Type obligatoire.";
        if (dto.getQuantite() == null || dto.getQuantite() <= 0)
            return "Quantité invalide.";
        return null;
    }
}
