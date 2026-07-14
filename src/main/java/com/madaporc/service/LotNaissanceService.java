package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.madaporc.dto.ConfirmationMiseBasDTO;
import com.madaporc.dto.LotNaissanceDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.LotPorc;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MouvementLotPorcRepository;

import jakarta.transaction.Transactional;

@Service
public class LotNaissanceService {

    private final LotPorcRepository lotPorcRepository;
    private final GroupeReproductionRepository groupeRepository;
    private final MouvementLotPorcRepository mouvementLotPorcRepository;

    public LotNaissanceService(
            LotPorcRepository lotPorcRepository,
            GroupeReproductionRepository groupeRepository,
            MouvementLotPorcRepository mouvementLotPorcRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.groupeRepository = groupeRepository;
        this.mouvementLotPorcRepository = mouvementLotPorcRepository;
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

    // Cree automatiquement le(s) lot(s) naissance a partir de la confirmation de mise bas.
    // 1 lot FEMELLE si nbFemelles >= 1, 1 lot MALE si nbMales >= 1.
    @Transactional
    public String creerLotsNaissance(Long groupeId, ConfirmationMiseBasDTO dto) {
        GroupeReproduction groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        int nbFemelles = dto.getNbFemelles() != null ? dto.getNbFemelles() : 0;
        int nbMales = dto.getNbMales() != null ? dto.getNbMales() : 0;

        if (nbFemelles < 1 && nbMales < 1) {
            return "Aucun porcelet vivant réparti : aucun lot naissance créé.";
        }

        if (nbFemelles >= 1) {
            creerLot(groupe, "FEMELLE", "F", nbFemelles, dto.getObjectifLot());
        }
        if (nbMales >= 1) {
            creerLot(groupe, "MALE", "M", nbMales, dto.getObjectifLot());
        }
        return "SUCCESS";
    }

    // Construit et enregistre un lot naissance pour un sexe donne.
    private void creerLot(GroupeReproduction groupe, String sexe, String suffixe, int effectif, String objectif) {
        LotPorc lot = new LotPorc();
        lot.setCodeLot(genererCodeUnique("LOT-N-" + suffixe + "-" + groupe.getCodeGroupe()));
        lot.setSexe(sexe);
        lot.setObjectif(objectif != null && !objectif.isBlank() ? objectif : "ENGRAISSEMENT");
        lot.setEffectifInitial(effectif);
        lot.setEffectifActuel(effectif);
        lot.setStatut("ACTIF");
        lot.setOrigine("NAISSANCE");
        lot.setDateCreation(LocalDate.now());
        lot.setRace(groupe.getLotFemelle() != null ? groupe.getLotFemelle().getRace() : null);
        lot.setLotParent(groupe.getLotFemelle());           // lie au lot mere
        lot.setGroupeReproductionOrigine(groupe);           // lie au groupe origine
        lot.setDescription("Lot naissance (" + sexe + ") issu du groupe " + groupe.getCodeGroupe());
        lot.setCreatedAt(LocalDateTime.now());
        lot.setUpdatedAt(LocalDateTime.now());
        lotPorcRepository.save(lot);

        // Mouvement d'entree "NAISSANCE" : trace la naissance dans l'historique
        // du lot (et alimente l'onglet Naissances de la page Mouvements).
        MouvementLotPorc mouvement = new MouvementLotPorc();
        mouvement.setLot(lot);
        mouvement.setTypeMouvement("NAISSANCE");
        mouvement.setQuantite(effectif);
        mouvement.setDateMouvement(lot.getDateCreation());
        mouvement.setObservation("Naissance issue de la mise bas du groupe " + groupe.getCodeGroupe());
        mouvement.setCreatedAt(LocalDateTime.now());
        mouvementLotPorcRepository.save(mouvement);
    }

    // Garantit un code de lot unique (ajoute un suffixe -2, -3... si deja pris).
    private String genererCodeUnique(String base) {
        String code = base;
        int i = 2;
        while (lotPorcRepository.existsByCodeLot(code)) {
            code = base + "-" + i;
            i++;
        }
        return code;
    }
}
