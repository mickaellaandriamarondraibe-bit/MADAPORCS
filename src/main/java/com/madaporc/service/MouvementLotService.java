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
        dto.setDateMouvement(mouvementLotPorc.getDateMouvement());
        dto.setCreatedAt(LocalDateTime.now());
        return dto;
    }

    public void ajouterMouvement(MouvementLotDTO dto, Long utilisateurId) {
        MouvementLotPorc mvt = new MouvementLotPorc();
        mvt.setLotPorcId(dto.getLotPorcId());
        mvt.setTypeMouvementLotId(dto.getTypeMouvementLotId());
        mvt.setQuantite(dto.getQuantiteMale() + dto.getQuantiteFemelle());
        mvt.setQuantiteMale(dto.getQuantiteMale());
        mvt.setQuantiteFemelle(dto.getQuantiteFemelle());
        mvt.setMotif(dto.getMotif());
        mvt.setDateMouvement(dto.getDateMouvement());
        mvt.setCreatedBy(utilisateurId);
        mvt.setCreated_at(dto.getCreatedAt());
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
        lotDTO.setNombreInitial(dto.getQuantiteMale() + dto.getQuantiteFemelle());
        lotDTO.setDateNaissanceEstimee(dto.getDateMouvement());
        lotDTO.setObservation(null);
        lotDTO.setPoidsMoyenInitialKg(null);
        lotDTO.setPrixAchatTotal(null);
        
        if (dto.getTypeMouvementLotId() == 1L ) { 
            lotDTO.setDateAchat(null);

            LotPorcService lotService = new LotPorcService();
            lotService.creer(lotDTO, utilisateurId);
        } else if (dto.getTypeMouvementLotId() == 2L) { 
            lotDTO.setDateAchat(dto.getDateMouvement());

            LotPorcService lotService = new LotPorcService();
            lotService.creer(lotDTO, utilisateurId);
        } else if (dto.getTypeMouvementLotId() == 3L || dto.getTypeMouvementLotId() == 4L) { 
            LotPorcDTO lotDTOToUpdate = modifier(dto.getLotPorcId(), dto, utilisateurId, dto.getQuantiteMale(), dto.getQuantiteFemelle());
            lotService.modifier(dto.getLotPorcId(), lotDTOToUpdate);
        }
    }

    public LotPorcDTO modifier(Long lotId, MouvementLotDTO dto, Long utilisateurId, Integer quantiteMale, Integer quantiteFemelle) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot != null ) {
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
    }

    // String verifierQuantiteDisponible(Long lotId, Integer quantite, String typeMouvement)
    // void mettreAJourEffectifLot(Long lotId, Integer quantite, String typeMouvement)
    // String validerMouvementLot(MouvementLotDTO dto)

}
