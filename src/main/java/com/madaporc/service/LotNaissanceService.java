package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.madaporc.dto.LotNaissanceDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;

import jakarta.transaction.Transactional;

@Service
public class LotNaissanceService {

    private final LotPorcRepository lotPorcRepository;
    private final GroupeReproductionRepository groupeRepository;

    public LotNaissanceService(
            LotPorcRepository lotPorcRepository,
            GroupeReproductionRepository groupeRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.groupeRepository = groupeRepository;
    }

    // Pre-remplit un DTO de lot naissance a partir des donnees du groupe (apres mise bas).
    public LotNaissanceDTO preparerLotNaissanceDepuisGroupe(Long groupeId) {
        GroupeReproduction groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        LotNaissanceDTO dto = new LotNaissanceDTO();
        dto.setCodeLot("LOT-N-" + groupe.getCodeGroupe());
        dto.setSexe("MIXTE");
        dto.setObjectif("ENGRAISSEMENT");
        int vivants = groupe.getNbPorceletsVivants() != null ? groupe.getNbPorceletsVivants() : 0;
        dto.setEffectifInitial(vivants);
        dto.setDescription("Lot naissance issu du groupe " + groupe.getCodeGroupe());
        return dto;
    }

    // Cree le lot naissance apres validation de l'utilisateur, lie au lot mere et au groupe origine.
    @Transactional
    public String creerLotNaissanceApresMiseBas(Long groupeId, LotNaissanceDTO dto) {
        GroupeReproduction groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        if (!"MISE_BAS_CONFIRMEE".equals(groupe.getStatut())) {
            return "La mise bas doit être confirmée avant de créer le lot naissance.";
        }
        if (dto.getEffectifInitial() == null || dto.getEffectifInitial() < 1) {
            return "Aucun porcelet vivant : impossible de créer un lot naissance.";
        }
        if (lotPorcRepository.existsByCodeLot(dto.getCodeLot())) {
            return "Le code du lot naissance existe déjà.";
        }

        LotPorc lot = new LotPorc();
        lot.setCodeLot(dto.getCodeLot());
        lot.setSexe(dto.getSexe());
        lot.setObjectif(dto.getObjectif());
        lot.setEffectifInitial(dto.getEffectifInitial());
        lot.setEffectifActuel(dto.getEffectifInitial());
        lot.setStatut("ACTIF");
        lot.setOrigine("NAISSANCE");
        lot.setDateCreation(LocalDate.now());
        lot.setLotParent(groupe.getLotFemelle());           // lie au lot mere
        lot.setGroupeReproductionOrigine(groupe);           // lie au groupe origine
        lot.setDescription(dto.getDescription());
        lot.setCreatedAt(LocalDateTime.now());
        lot.setUpdatedAt(LocalDateTime.now());

        lotPorcRepository.save(lot);
        return "SUCCESS";
    }
}
