package com.madaporc.service;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.dto.LotDetailDTO;
import com.madaporc.dto.LotFiltreDTO;
import com.madaporc.dto.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Race;

import java.util.List;
import java.util.ArrayList;
import com.madaporc.repository.*;

@Service
public class LotPorcService {
    private final LotPorcRepository lotPorcRepository;
    private final RaceRepository raceRepository;

    public LotPorcService(LotPorcRepository lotPorcRepository, RaceRepository raceRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.raceRepository = raceRepository;
    }
public List<LotPorc> rechercherLots(LotFiltreDTO filtre) {
    List<LotPorc> lots = new ArrayList<>(lotPorcRepository.findAll());

    if (filtre.getCodeLot() != null && !filtre.getCodeLot().trim().isEmpty()) {
        String codeLot = filtre.getCodeLot().trim().toLowerCase();

        lots.removeIf(lot ->
                lot.getCodeLot() == null ||
                !lot.getCodeLot().toLowerCase().contains(codeLot)
        );
    }

    if (filtre.getSexe() != null && !filtre.getSexe().trim().isEmpty()) {
        lots.removeIf(lot ->
                lot.getSexe() == null ||
                !lot.getSexe().equals(filtre.getSexe())
        );
    }

    if (filtre.getObjectif() != null && !filtre.getObjectif().trim().isEmpty()) {
        lots.removeIf(lot ->
                lot.getObjectif() == null ||
                !lot.getObjectif().equals(filtre.getObjectif())
        );
    }

    if (filtre.getStatut() != null && !filtre.getStatut().trim().isEmpty()) {
        lots.removeIf(lot ->
                lot.getStatut() == null ||
                !lot.getStatut().equals(filtre.getStatut())
        );
    }

    if (filtre.getDateCreationDebut() != null) {
        lots.removeIf(lot ->
                lot.getDateCreation() == null ||
                lot.getDateCreation().isBefore(filtre.getDateCreationDebut())
        );
    }

    if (filtre.getDateCreationFin() != null) {
        lots.removeIf(lot ->
                lot.getDateCreation() == null ||
                lot.getDateCreation().isAfter(filtre.getDateCreationFin())
        );
    }

    return lots;
}

    public void prepareFormModel(Model model, Long id) {
        LotPorc lot = lotPorcRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lot non trouvé"));

        LotPorcDTO dto = new LotPorcDTO();

        dto.setId(lot.getId());
        dto.setCodeLot(lot.getCodeLot());
        dto.setDateCreation(lot.getDateCreation());
        dto.setSexe(lot.getSexe());
        dto.setObjectif(lot.getObjectif());
        dto.setOrigine(lot.getOrigine());
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

        model.addAttribute("lot", dto);
    }

    public String creerLot(LotPorcDTO dto) {
        if (existeCodeLot(dto.getCodeLot())) {
            return "redirect:/lots/form?error=codeLotExiste";
        }

        if (dto.getEffectifInitial() == null || dto.getEffectifInitial() <= 0) {
            return "redirect:/lots/form?error=effectifInitialInvalide";
        }

        LotPorc lot = new LotPorc();

        lot.setCodeLot(dto.getCodeLot());
        lot.setDateCreation(dto.getDateCreation());
        lot.setSexe(dto.getSexe());
        lot.setObjectif(dto.getObjectif());
        if(dto.getOrigine() == null){
            lot.setOrigine(null);
        }
        lot.setOrigine(dto.getOrigine());
        lot.setDescription(dto.getDescription());
        lot.setEffectifInitial(dto.getEffectifInitial());

        if (dto.getEffectifActuel() == null) {
            lot.setEffectifActuel(dto.getEffectifInitial());
        } else if (dto.getEffectifActuel() < 0) {
            return "redirect:/lots/form?error=effectifActuelInvalide";
        } else {
            lot.setEffectifActuel(dto.getEffectifActuel());
        }

        if (dto.getStatut() == null || dto.getStatut().isBlank()) {
            lot.setStatut("ACTIF");
        } else {
            lot.setStatut(dto.getStatut());
        }

        Race race = raceRepository.findById(dto.getRaceId())
                .orElseThrow(() -> new IllegalArgumentException("Race non trouvée"));
        lot.setRace(race);

        if (dto.getLotParentId() != null) {
            LotPorc lotParent = lotPorcRepository.findById(dto.getLotParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Lot parent non trouvé"));
            lot.setLotParent(lotParent);
        }

        lotPorcRepository.save(lot);

        return "redirect:/lots";
    }

    public String modifierLot(Long id, LotPorcDTO dto) {
        LotPorc lot = lotPorcRepository.findById(id)
                .orElse(null);

        if (lot == null) {
            return "redirect:/lots?error=notfound";
        }

        lot.setCodeLot(dto.getCodeLot());
        lot.setDateCreation(dto.getDateCreation());
        lot.setSexe(dto.getSexe());
        lot.setObjectif(dto.getObjectif());

        lotPorcRepository.save(lot);

        return "redirect:/lots";
    }

    public LotDetailDTO getDetailLot(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot non trouve"));
        LotDetailDTO detailDTO = new LotDetailDTO();
        detailDTO.setId(lot.getId());
        detailDTO.setCodeLot(lot.getCodeLot());
        detailDTO.setDateCreation(lot.getDateCreation());
        detailDTO.setSexe(lot.getSexe());
        detailDTO.setObjectif(lot.getObjectif());
        detailDTO.setOrigine(lot.getOrigine());
        detailDTO.setEffectifInitial(lot.getEffectifInitial());
        return detailDTO;
    }

    public String archiverLot(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId)
                .orElse(null);

        if (lot == null) {
            return "redirect:/lots?error=notfound";
        }
        lot.setStatut("archive");

        lotPorcRepository.save(lot);

        return "redirect:/lots";
    }

    public boolean existeCodeLot(String codeLot) {
        return lotPorcRepository.existsByCodeLot(codeLot);
    }

    public boolean verifierLotActif(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new IllegalArgumentException("Lot non trouve"));
        return "actif".equals(lot.getStatut());
    }
}