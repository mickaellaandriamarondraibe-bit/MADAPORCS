package com.madaporc.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.dto.MouvementAuditDTO;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.model.MouvementStock;
import com.madaporc.repository.MouvementLotPorcRepository;
import com.madaporc.repository.MouvementStockRepository;

// Agrege tous les mouvements (lots de porcs + stock d'aliment) en une seule
// liste, du plus recent au plus ancien, pour la page d'audit / tracabilite.
@Service
public class MouvementAuditService {

    private final MouvementLotPorcRepository lotMouvementRepository;
    private final MouvementStockRepository stockMouvementRepository;

    public MouvementAuditService(
            MouvementLotPorcRepository lotMouvementRepository,
            MouvementStockRepository stockMouvementRepository) {
        this.lotMouvementRepository = lotMouvementRepository;
        this.stockMouvementRepository = stockMouvementRepository;
    }

    @Transactional(readOnly = true)
    public List<MouvementAuditDTO> listerTousMouvements() {
        List<MouvementAuditDTO> tout = new ArrayList<>();

        for (MouvementLotPorc m : lotMouvementRepository.findAll()) {
            tout.add(depuisLot(m));
        }
        for (MouvementStock m : stockMouvementRepository.findAll()) {
            tout.add(depuisStock(m));
        }

        // Plus recent en premier (date nulle en dernier).
        tout.sort(Comparator.comparing(MouvementAuditDTO::getDate,
                Comparator.nullsLast(Comparator.reverseOrder())));
        return tout;
    }

    // Meme liste, restreinte a l'onglet choisi (tout, lot, vente, deces,
    // naissance, stock). "tout" ou valeur inconnue => aucun filtre.
    @Transactional(readOnly = true)
    public List<MouvementAuditDTO> listerMouvements(String filtre) {
        List<MouvementAuditDTO> tout = listerTousMouvements();
        if (filtre == null || filtre.isBlank() || "tout".equalsIgnoreCase(filtre)) {
            return tout;
        }

        String f = filtre.toLowerCase();
        List<MouvementAuditDTO> filtres = new ArrayList<>();
        for (MouvementAuditDTO m : tout) {
            if (correspond(m, f)) {
                filtres.add(m);
            }
        }
        return filtres;
    }

    private boolean correspond(MouvementAuditDTO m, String filtre) {
        switch (filtre) {
            case "lot":
                return "Lot de porcs".equals(m.getCategorie());
            case "stock":
                return "Stock aliment".equals(m.getCategorie());
            case "vente":
                return "VENTE".equals(m.getType());
            case "deces":
                return "DECES".equals(m.getType());
            case "naissance":
                return "NAISSANCE".equals(m.getType());
            default:
                return true;
        }
    }

    private MouvementAuditDTO depuisLot(MouvementLotPorc m) {
        MouvementAuditDTO dto = new MouvementAuditDTO();
        dto.setDate(m.getDateMouvement());
        dto.setCategorie("Lot de porcs");
        dto.setType(m.getTypeMouvement());
        dto.setSens(sensLot(m.getTypeMouvement()));
        dto.setCible(m.getLot() != null ? m.getLot().getCodeLot() : "—");
        int q = m.getQuantite() != null ? m.getQuantite() : 0;
        dto.setQuantite(q + " porc" + (q > 1 ? "s" : ""));
        dto.setDetail(m.getObservation());
        return dto;
    }

    private MouvementAuditDTO depuisStock(MouvementStock m) {
        MouvementAuditDTO dto = new MouvementAuditDTO();
        dto.setDate(m.getDateMouvement());
        dto.setCategorie("Stock aliment");
        dto.setType(m.getTypeMouvement());
        dto.setSens("ENTREE".equalsIgnoreCase(m.getTypeMouvement()) ? "ENTREE" : "SORTIE");
        String unite = m.getIngredient() != null && m.getIngredient().getUnite() != null
                ? " " + m.getIngredient().getUnite() : "";
        dto.setCible(m.getIngredient() != null ? m.getIngredient().getNom() : "—");
        dto.setQuantite((m.getQuantite() != null ? m.getQuantite().toPlainString() : "0") + unite);
        if (m.getStockApres() != null) {
            dto.setDetail("Stock après : " + m.getStockApres().toPlainString() + unite);
        }
        return dto;
    }

    // Sens d'un mouvement de lot : entree pour les gains d'effectif, sortie sinon.
    private String sensLot(String type) {
        if ("ENTREE".equals(type) || "NAISSANCE".equals(type) || "TRANSFERT_ENTREE".equals(type)) {
            return "ENTREE";
        }
        return "SORTIE";
    }
}
