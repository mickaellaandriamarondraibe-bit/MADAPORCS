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
        List<LotPorc> lots = new ArrayList<>();
        if (filtre.getCodeLot() != null) {
            lots.addAll(lotPorcRepository.findByCodeLotContainingIgnoreCase(filtre.getCodeLot()));
        }

        if (filtre.getSexe() != null) {
            lots.addAll(lotPorcRepository.findBySexe(filtre.getSexe()));
        }

        if(filtre.getObjectif() != null) {
            lots.addAll(lotPorcRepository.findByObjectif(filtre.getObjectif()));
        }

        if(filtre.getStatut() != null) {
            lots.addAll(lotPorcRepository.findByStatut(filtre.getStatut()));
        }

        if (filtre.getDateCreationDebut() != null && filtre.getDateCreationFin() != null) {
            lots.addAll(lotPorcRepository.findByDateCreationBetween(filtre.getDateCreationDebut(), filtre.getDateCreationFin()));
        }
    
        return lots;
    }

    public void prepareFormModel(Model model, Long id) {
        LotPorc lot = lotPorcRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Lot non trouve"));
        model.addAttribute("lot", lot);
    }

    public String creerLot(LotPorcDTO dto) {
        LotPorc lot = new LotPorc();

        if(existeCodeLot(dto.getCodeLot())) {
            return "redirect:/lots/form?error=codeLotExiste";
        }

        lot.setCodeLot(dto.getCodeLot());
        lot.setDateCreation(dto.getDateCreation());
        lot.setSexe(dto.getSexe());
        lot.setObjectif(dto.getObjectif());
        lot.setOrigine(dto.getOrigine());
        lot.setStatut(dto.getStatut());
        if(dto.getEffectifInitial() == null || dto.getEffectifInitial() <= 0) {
            return "redirect:/lots/form?error=effectifInitialInvalide";
        } else {
            lot.setEffectifInitial(dto.getEffectifInitial());
        }
        
        if(dto.getEffectifActuel() == null || dto.getEffectifActuel() < 0) {
            return "redirect:/lots/form?error=effectifActuelInvalide";
        } else {
            lot.setEffectifActuel(dto.getEffectifActuel());
        }

        Race race = raceRepository.findById(dto.getRaceId()).orElseThrow(() -> new IllegalArgumentException("Race non trouve"));
        lot.setRace(race);
        lot.setDateCreation(dto.getDateCreation());
        
        lotPorcRepository.save(lot);

        return "redirect:/lots";
    }

    public String modifierLot(Long id, LotPorcDTO dto) {
        boolean exists = lotPorcRepository.existsById(id);
        if (!exists) {
            return "redirect:/lots?error=notfound";
        }

        lotPorcRepository.updateById(id, dto.getCodeLot(), dto.getDateCreation(), dto.getSexe(), dto.getObjectif());

        return "redirect:/lots";
    }

    public LotDetailDTO getDetailLot(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Lot non trouve"));
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
        boolean exists = lotPorcRepository.existsById(lotId);
        if (!exists) {
            return "redirect:/lots?error=notfound";
        }
   
        lotPorcRepository.updateByStatut(lotId, "archive");
        return "redirect:/lots";
    }

    public boolean existeCodeLot(String codeLot) {
        return lotPorcRepository.existsByCodeLot(codeLot);
    }

    public boolean verifierLotActif(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElseThrow(() -> new IllegalArgumentException("Lot non trouve"));
        return "actif".equals(lot.getStatut());
    }

    public List<LotPorc> getAllActifs() {
        return lotPorcRepository.findByStatut("actif");
    }
}