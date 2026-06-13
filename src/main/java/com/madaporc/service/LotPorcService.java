package com.madaporc.service;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.LotPorcDTO;
import com.madaporc.model.LotPorc;

/**
 * Service placeholder conforme au PDF.
 * Remplacer progressivement les commentaires par les vraies méthodes.
 */
@Service
public class LotPorcService {


    // List<LotPorc> rechercherLots(String code, Long raceId, Long statutId)
    // Optional<LotPorc> findLot(Long id)
    // String archiverLot(Long lotId)
    // boolean verifierLotActif(Long lotId)
    // void prepareLotListModel(Model model, String code, Long raceId, Long statutId)
    // void prepareLotFormModel(Model model, Long id, String typeEntree)
    // String creer(LotPorcDTO dto, Long utilisateurId)
    // String modifier(Long id, LotPorcDTO dto)
    // String verifierCodeUnique(String codeLot, Long idActuel)
    // String validerDonneesLot(LotPorcDTO dto)
    // LotPorc convertirDtoVersEntity(LotPorcDTO dto)

    public LotPorcDTO convertToDTO(LotPorc lotPorc) {
        LotPorcDTO dto = new LotPorcDTO();
        dto.setId(lotPorc.getId());
        dto.setTypeEntree(lotPorc.getTypeEntree());
        dto.setCodeLot(lotPorc.getCodeLot());
        dto.setRaceId(lotPorc.getRace().getId());
        dto.setStatutLotId(lotPorc.getStatutLot().getId());
        dto.setNombreMalesInitial(lotPorc.getNombreMalesInitial());
        dto.setNombreFemellesInitial(lotPorc.getNombreFemellesInitial());
        dto.setNombreInitial(lotPorc.getNombreInitial());
        dto.setNombreActuel(lotPorc.getNombreActuel());
        dto.setDateNaissanceEstimee(lotPorc.getDateNaissanceEstimee());
        dto.setDateAchat(lotPorc.getDateAchat());
        dto.setPrixAchatTotal(lotPorc.getPrixAchatTotal());
        dto.setPoidsMoyenInitialKg(lotPorc.getPoidsMoyenInitialKg());
        dto.setObservation(lotPorc.getObservation());
        return dto;
    }
}
