package com.madaporc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.repository.MouvementLotPorcRepository;

import java.util.List;

import com.madaporc.model.MouvementLotPorc;

import org.springframework.transaction.annotation.Transactional;

import com.madaporc.DTO.MouvementLotDTO;

import java.time.LocalDateTime;

import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.TypeMouvementLotRepository;
import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Race;
import com.madaporc.model.StatutLot;
import com.madaporc.model.TypeMouvementLot;
import com.madaporc.repository.RaceRepository;
import com.madaporc.repository.StatutLotRepository;

/**
 * Service placeholder conforme au PDF.
 * Remplacer progressivement les commentaires par les vraies méthodes.
 */
@Service
public class MouvementLotService {
    @Autowired
    private MouvementLotPorcRepository mouvementLotRepository;
    @Autowired
    private LotPorcRepository lotPorcRepository;
    @Autowired
    private TypeMouvementLotRepository typeMouvementLotRepository;
    @Autowired
    private RaceRepository raceRepository;
    @Autowired
    private StatutLotRepository statutLotRepository;
    @Autowired
    private LotPorcService lotService;

    public MouvementLotPorcRepository getMouvementLotPorcRepository() {
        return mouvementLotRepository;
    }

    @Transactional(readOnly = true)
    public List<MouvementLotPorc> findMouvements(Long lotId) {
        return mouvementLotRepository.findByLotPorcIdOrderByDateMouvementDesc(lotId);
    }

    public MouvementLotDTO convertToDTO(MouvementLotPorc mouvementLotPorc) {
        MouvementLotDTO dto = new MouvementLotDTO();
        dto.setId(mouvementLotPorc.getId());
        dto.setLotPorcId(mouvementLotPorc.getLotPorcId());
        dto.setTypeMouvementLotId(mouvementLotPorc.getTypeMouvementLotId().getId());
        dto.setQuantite(mouvementLotPorc.getQuantite());
        dto.setQuantiteMale(mouvementLotPorc.getQuantiteMale());
        dto.setQuantiteFemelle(mouvementLotPorc.getQuantiteFemelle());
        dto.setMotif(mouvementLotPorc.getMotif());
        dto.setCreatedAt(mouvementLotPorc.getCreatedBy());
        dto.setDateMouvement(mouvementLotPorc.getDateMouvement());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    public MouvementLotPorc convertToEntity(MouvementLotDTO dto) {
        MouvementLotPorc mouvementLotPorc = new MouvementLotPorc();
        mouvementLotPorc.setId(dto.getId());
        mouvementLotPorc.setLotPorcId(dto.getLotPorcId());

        TypeMouvementLot type =
        typeMouvementLotRepository.findById(dto.getTypeMouvementLotId())
        .orElseThrow(() -> new RuntimeException("Type mouvement introuvable"));

        mouvementLotPorc.setTypeMouvementLotId(type);

        mouvementLotPorc.setQuantite(dto.getQuantite());
        mouvementLotPorc.setQuantiteMale(dto.getQuantiteMale());
        mouvementLotPorc.setQuantiteFemelle(dto.getQuantiteFemelle());
        mouvementLotPorc.setMotif(dto.getMotif());
        mouvementLotPorc.setDateMouvement(dto.getDateMouvement());
        mouvementLotPorc.setCreatedAt(dto.getCreatedAt());
        return mouvementLotPorc;
    }

    @Transactional
    public void ajouterMouvement(MouvementLotDTO dto, Long utilisateurId) {
        MouvementLotPorc mvt = convertToEntity(dto);
        mvt.setCreatedBy(utilisateurId);
        mouvementLotRepository.save(mvt);

        TypeMouvementLot typeMvt = typeMouvementLotRepository.findById(dto.getTypeMouvementLotId()).orElse(null);
        LotPorc lotRepo = lotPorcRepository.findById(dto.getLotPorcId()).orElse(null);
        Race race = raceRepository.findById(lotRepo.getRaceId()).orElse(null);
        StatutLot statutLot = statutLotRepository.findById(lotRepo.getStatutLotId()).orElse(null);

        LotPorcDTO lotDTO = new LotPorcDTO();
        lotDTO.setTypeEntree(typeMvt.getLibelle());
        lotDTO.setCodeLot("LOTMVT"+dto.getLotPorcId());
        lotDTO.setRaceId(race.getId());
        lotDTO.setStatutLotId(statutLot.getId());
        lotDTO.setNombreMalesInitial(dto.getQuantiteMale());
        lotDTO.setNombreFemellesInitial(dto.getQuantiteFemelle());
        lotDTO.setNombreInitial(dto.getQuantiteMale() + dto.getQuantiteFemelle());
        lotDTO.setNombreActuel(dto.getQuantiteMale() + dto.getQuantiteFemelle());
        lotDTO.setDateNaissanceEstimee(dto.getDateMouvement());
        lotDTO.setObservation(null);
        lotDTO.setPoidsMoyenInitialKg(null);
        lotDTO.setPrixAchatTotal(null);
        
        if (Long.valueOf(1L).equals(dto.getTypeMouvementLotId())) { 
            lotDTO.setDateAchat(null);
            lotService.creer(lotDTO, utilisateurId);
        } 
        else if (Long.valueOf(2L).equals(dto.getTypeMouvementLotId())) { 
            lotDTO.setDateAchat(dto.getDateMouvement());
            lotService.creer(lotDTO, utilisateurId);
        } 
        else if (Long.valueOf(3L).equals(dto.getTypeMouvementLotId()) || Long.valueOf(4L).equals(dto.getTypeMouvementLotId()) || Long.valueOf(5L).equals(dto.getTypeMouvementLotId())) { 
            LotPorcDTO lotDTOToUpdate = modifier(dto.getLotPorcId(), dto, utilisateurId, dto.getQuantiteMale(), dto.getQuantiteFemelle());
            lotService.modifier(dto.getLotPorcId(), lotDTOToUpdate);
        }
    }

    public LotPorcDTO modifier(Long lotId, MouvementLotDTO dto, Long utilisateurId, Integer quantiteMale, Integer quantiteFemelle) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        
        if (lot == null ) {
            return null;
        }

        LotPorcDTO lotDTO = new LotPorcDTO();
        lotDTO.setId(lot.getId());
        lotDTO.setTypeEntree(lot.getTypeEntree());
        lotDTO.setCodeLot(lot.getCodeLot());
        lotDTO.setRaceId(lot.getRaceId());
        lotDTO.setStatutLotId(lot.getStatutLotId());
        lotDTO.setNombreMalesInitial(lot.getNombreMalesInitial());
        lotDTO.setNombreFemellesInitial(lot.getNombreFemellesInitial());
        lotDTO.setNombreInitial(lot.getNombreInitial());
        lotDTO.setNombreActuel(lot.getNombreActuel() - (quantiteMale + quantiteFemelle));
        lotDTO.setDateNaissanceEstimee(lot.getDateNaissanceEstimee());
        lotDTO.setDateAchat(lot.getDateAchat());
        lotDTO.setPrixAchatTotal(lot.getPrixAchatTotal());
        lotDTO.setPoidsMoyenInitialKg(lot.getPoidsMoyenInitialKg());
        lotDTO.setObservation(lot.getObservation());
        
        return lotDTO;
    }

    public void supprimerMouvement(Long mouvementId) {
        MouvementLotPorc mouvement = mouvementLotRepository.findById(mouvementId).orElse(null);
        if (mouvement != null) {
            mouvementLotRepository.delete(mouvement);
        }
    }

    // String verifierQuantiteDisponible(Long lotId, Integer quantite, String typeMouvement)
    // void mettreAJourEffectifLot(Long lotId, Integer quantite, String typeMouvement)
    // String validerMouvementLot(MouvementLotDTO dto)

}
