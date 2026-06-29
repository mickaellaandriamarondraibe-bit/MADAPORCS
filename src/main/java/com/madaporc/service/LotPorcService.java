package com.madaporc.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.dto.LotDetailDTO;
import com.madaporc.dto.LotFiltreDTO;
import com.madaporc.dto.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.model.Race;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
import com.madaporc.repository.RaceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LotPorcService {

    private final LotPorcRepository lotPorcRepository;
    private final RaceRepository raceRepository;
    private final MouvementLotPorcRepository mouvementLotPorcRepository;
    private final RepartitionReproductiveService repartitionReproductiveService;

    public LotPorcService(
            LotPorcRepository lotPorcRepository,
            RaceRepository raceRepository,
            MouvementLotPorcRepository mouvementLotPorcRepository,
            RepartitionReproductiveService repartitionReproductiveService) {
        this.lotPorcRepository = lotPorcRepository;
        this.raceRepository = raceRepository;
        this.mouvementLotPorcRepository = mouvementLotPorcRepository;
        this.repartitionReproductiveService = repartitionReproductiveService;
    }

    @Transactional(readOnly = true)
    public List<LotPorc> rechercherLots(LotFiltreDTO filtre) {
        List<LotPorc> lots = new ArrayList<>(lotPorcRepository.findAll());

        if (filtre.getCodeLot() != null && !filtre.getCodeLot().trim().isEmpty()) {
            String codeLot = filtre.getCodeLot().trim().toLowerCase();
            lots.removeIf(lot -> lot.getCodeLot() == null ||
                    !lot.getCodeLot().toLowerCase().contains(codeLot));
        }

        if (filtre.getSexe() != null && !filtre.getSexe().trim().isEmpty()) {
            lots.removeIf(lot -> lot.getSexe() == null ||
                    !lot.getSexe().equalsIgnoreCase(filtre.getSexe()));
        }

        if (filtre.getObjectif() != null && !filtre.getObjectif().trim().isEmpty()) {
            lots.removeIf(lot -> lot.getObjectif() == null ||
                    !lot.getObjectif().equalsIgnoreCase(filtre.getObjectif()));
        }

        if (filtre.getStatut() != null && !filtre.getStatut().trim().isEmpty()) {
            lots.removeIf(lot -> lot.getStatut() == null ||
                    !lot.getStatut().equalsIgnoreCase(filtre.getStatut()));
        }

        if (filtre.getDateCreationDebut() != null) {
            lots.removeIf(lot -> lot.getDateCreation() == null ||
                    lot.getDateCreation().isBefore(filtre.getDateCreationDebut()));
        }

        if (filtre.getDateCreationFin() != null) {
            lots.removeIf(lot -> lot.getDateCreation() == null ||
                    lot.getDateCreation().isAfter(filtre.getDateCreationFin()));
        }

        return lots;
    }

    public void prepareFormModel(Model model, Long id) {
        LotPorcDTO dto = new LotPorcDTO();

        if (id != null) {
            LotPorc lot = findById(id);

            dto.setId(lot.getId());
            dto.setCodeLot(lot.getCodeLot());
            dto.setDateCreation(lot.getDateCreation());
            dto.setSexe(lot.getSexe());
            dto.setObjectif(lot.getObjectif());
            dto.setOrigine(lot.getOrigine());
            dto.setAgeMois(lot.getAgeMois());
            dto.setEffectifInitial(lot.getEffectifInitial());
            dto.setEffectifActuel(lot.getEffectifActuel());
            dto.setStatut(lot.getStatut());
            dto.setDescription(lot.getDescription());

            if (lot.getRace() != null) {
                dto.setRaceId(lot.getRace().getId());
            }

            if (lot.getLotParent() != null) {
                dto.setLotParentId(lot.getLotParent().getId());
            }

            if (lot.getGroupeReproductionOrigine() != null) {
                dto.setGroupeReproductionOrigineId(lot.getGroupeReproductionOrigine().getId());
            }
        }

        model.addAttribute("lot", dto);
        model.addAttribute("races", raceRepository.findAll());
    }

    public String creerLot(LotPorcDTO dto) {
        String erreur = validerCreation(dto);

        if (erreur != null) {
            return erreur;
        }

        LotPorc lot = new LotPorc();

        lot.setCodeLot(dto.getCodeLot().trim().toUpperCase());
        lot.setDateCreation(dto.getDateCreation() != null ? dto.getDateCreation() : LocalDate.now());
        lot.setSexe(dto.getSexe().toUpperCase());
        lot.setObjectif(dto.getObjectif().toUpperCase());
        lot.setOrigine(dto.getOrigine() != null && !dto.getOrigine().isBlank()
                ? dto.getOrigine().toUpperCase()
                : "ACHAT");
        lot.setDescription(dto.getDescription());

        // L'âge n'a de sens que pour un lot acheté (origine ACHAT).
        if ("ACHAT".equalsIgnoreCase(lot.getOrigine())) {
            lot.setAgeMois(dto.getAgeMois());
        }

        lot.setEffectifInitial(dto.getEffectifInitial());

        lot.setEffectifActuel(dto.getEffectifInitial());

        lot.setStatut("ACTIF");

        if (dto.getRaceId() != null) {
            Race race = raceRepository.findById(dto.getRaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Race non trouvée"));
            lot.setRace(race);
        }

        if (dto.getLotParentId() != null) {
            LotPorc lotParent = lotPorcRepository.findById(dto.getLotParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Lot parent non trouvé"));
            lot.setLotParent(lotParent);
        }

        LotPorc lotSauvegarde = lotPorcRepository.save(lot);

        /*
         * Mouvement automatique de création du lot.
         */
        creerMouvementInitial(lotSauvegarde);

        /*
         * Pour un lot femelle, on remplit tout de suite la répartition
         * reproductive (Prête / À surveiller / À retirer) selon l'âge.
         * C'est ce qui alimente la page "Analyse reproductive".
         */
        repartitionReproductiveService.initialiserRepartitionLotFemelle(lotSauvegarde.getId());

        return null;
    }

    private void creerMouvementInitial(LotPorc lot) {
        MouvementLotPorc mouvement = new MouvementLotPorc();

        mouvement.setLot(lot);
        mouvement.setQuantite(lot.getEffectifInitial());
        mouvement.setDateMouvement(lot.getDateCreation());

        String origine = lot.getOrigine();

        if ("NAISSANCE".equalsIgnoreCase(origine)) {
            mouvement.setTypeMouvement("NAISSANCE");
            mouvement.setObservation("Création automatique du lot par naissance.");
        } else if ("TRANSFERT".equalsIgnoreCase(origine)) {
            mouvement.setTypeMouvement("TRANSFERT_ENTREE");
            mouvement.setObservation("Création automatique du lot par transfert.");
        } else {
            mouvement.setTypeMouvement("ENTREE");
            mouvement.setObservation("Création automatique du lot.");
        }

        mouvementLotPorcRepository.save(mouvement);
    }

    public String modifierLot(Long id, LotPorcDTO dto) {
        LotPorc lot = findById(id);

        if (dto.getCodeLot() == null || dto.getCodeLot().trim().isEmpty()) {
            return "Le code du lot est obligatoire.";
        }

        String nouveauCode = dto.getCodeLot().trim().toUpperCase();

        if (!nouveauCode.equalsIgnoreCase(lot.getCodeLot())
                && lotPorcRepository.existsByCodeLot(nouveauCode)) {
            return "Ce code lot existe déjà.";
        }

        if (dto.getSexe() == null || dto.getSexe().isBlank()) {
            return "Le sexe du lot est obligatoire.";
        }

        if (!dto.getSexe().equalsIgnoreCase("MALE")
                && !dto.getSexe().equalsIgnoreCase("FEMELLE")) {
            return "Le sexe du lot doit être MALE ou FEMELLE.";
        }

        if (dto.getObjectif() == null || dto.getObjectif().isBlank()) {
            return "L'objectif du lot est obligatoire.";
        }

        lot.setCodeLot(nouveauCode);
        lot.setDateCreation(dto.getDateCreation() != null ? dto.getDateCreation() : lot.getDateCreation());
        lot.setSexe(dto.getSexe().toUpperCase());
        lot.setObjectif(dto.getObjectif().toUpperCase());
        lot.setOrigine(dto.getOrigine() != null ? dto.getOrigine().toUpperCase() : lot.getOrigine());
        lot.setDescription(dto.getDescription());

        // On met à jour l'âge seulement pour un lot acheté.
        if ("ACHAT".equalsIgnoreCase(lot.getOrigine())) {
            lot.setAgeMois(dto.getAgeMois());
        }

        if (dto.getRaceId() != null) {
            Race race = raceRepository.findById(dto.getRaceId())
                    .orElseThrow(() -> new IllegalArgumentException("Race non trouvée"));
            lot.setRace(race);
        }

        lotPorcRepository.save(lot);

        // Après modification (âge, date, race...), on recalcule la répartition
        // pour que la page "Analyse reproductive" reflète tout de suite le changement.
        repartitionReproductiveService.initialiserRepartitionLotFemelle(lot.getId());

        return null;
    }

    @Transactional(readOnly = true)
    public LotDetailDTO getDetailLot(Long lotId) {
        LotPorc lot = findById(lotId);

        LotDetailDTO detailDTO = new LotDetailDTO();

        detailDTO.setId(lot.getId());
        detailDTO.setCodeLot(lot.getCodeLot());
        detailDTO.setDateCreation(lot.getDateCreation());

        if (lot.getRace() != null) {
            detailDTO.setRaceId(lot.getRace().getId());
            detailDTO.setRaceNom(lot.getRace().getNom());
        }

        detailDTO.setSexe(lot.getSexe());
        detailDTO.setObjectif(lot.getObjectif());
        detailDTO.setOrigine(lot.getOrigine());
        detailDTO.setEffectifInitial(lot.getEffectifInitial());
        detailDTO.setEffectifActuel(lot.getEffectifActuel());
        detailDTO.setStatut(lot.getStatut());

        if (lot.getLotParent() != null) {
            detailDTO.setLotParentId(lot.getLotParent().getId());
            detailDTO.setCodeLotParent(lot.getLotParent().getCodeLot());
        }

        if (lot.getGroupeReproductionOrigine() != null) {
            detailDTO.setGroupeReproductionOrigineId(
                    lot.getGroupeReproductionOrigine().getId());
        }

        detailDTO.setDescription(lot.getDescription());
        detailDTO.setCreatedAt(lot.getCreatedAt());
        detailDTO.setUpdatedAt(lot.getUpdatedAt());

        return detailDTO;
    }

    public String archiverLot(Long lotId) {
        LotPorc lot = findById(lotId);

        lot.setStatut("ARCHIVE");
        lotPorcRepository.save(lot);

        return null;
    }

    @Transactional(readOnly = true)
    public boolean existeCodeLot(String codeLot) {
        if (codeLot == null || codeLot.trim().isEmpty()) {
            return false;
        }

        return lotPorcRepository.existsByCodeLot(codeLot.trim().toUpperCase());
    }

    @Transactional(readOnly = true)
    public boolean verifierLotActif(Long lotId) {
        LotPorc lot = findById(lotId);
        return "ACTIF".equalsIgnoreCase(lot.getStatut());
    }

    @Transactional(readOnly = true)
    public List<LotPorc> getAllActifs() {
        return lotPorcRepository.findByStatut("ACTIF");
    }

    @Transactional(readOnly = true)
    public LotPorc findById(Long id) {
        return lotPorcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lot non trouvé avec l'id : " + id));
    }

    private String validerCreation(LotPorcDTO dto) {
        if (dto.getCodeLot() == null || dto.getCodeLot().trim().isEmpty()) {
            return "Le code du lot est obligatoire.";
        }

        if (existeCodeLot(dto.getCodeLot())) {
            return "Ce code lot existe déjà.";
        }

        if (dto.getSexe() == null || dto.getSexe().isBlank()) {
            return "Le sexe du lot est obligatoire.";
        }

        if (!dto.getSexe().equalsIgnoreCase("MALE")
                && !dto.getSexe().equalsIgnoreCase("FEMELLE")) {
            return "Le sexe du lot doit être MALE ou FEMELLE.";
        }

        if (dto.getObjectif() == null || dto.getObjectif().isBlank()) {
            return "L'objectif du lot est obligatoire.";
        }

        if (dto.getEffectifInitial() == null || dto.getEffectifInitial() <= 0) {
            return "L'effectif initial doit être supérieur à 0.";
        }

        return null;
    }

    @Transactional(readOnly = true)
    public List<Race> getAllRaces() {
        return raceRepository.findAll();
    }
}