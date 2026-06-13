package com.madaporc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import com.madaporc.model.PeseeLot;
import com.madaporc.repository.PeseeLotRepository;
import com.madaporc.DTO.PeseeLotDTO;
import java.time.LocalDate;

/**
 * Service placeholder conforme au PDF.
 * Remplacer progressivement les commentaires par les vraies méthodes.
 */
@Service
public class PeseeLotService {
    @Autowired
    private PeseeLotRepository peseeLotRepository;

    @Transactional(readOnly = true)
    public List<PeseeLot> findPesees(Long lotId) {
        return peseeLotRepository.findByLotPorcIdOrderByDatePeseeDesc(lotId);
    }

    public PeseeLotDTO convertToDTO(PeseeLot peseeLot, Long utilisateurId) {
        PeseeLotDTO dto = new PeseeLotDTO();
        dto.setId(peseeLot.getId());
        dto.setLotPorcId(peseeLot.getLotPorcId());
        dto.setPoidsMoyenKg(peseeLot.getPoidsMoyenKg());
        dto.setDatePesee(peseeLot.getDatePesee());
        dto.setObservation(peseeLot.getObservation());
        dto.setCreatedBy(utilisateurId);
        dto.setCreatedAt(LocalDate.now()); // ou peseeLot.getCreatedAt() si la date de création est stockée dans l'entité
        return dto;
    }

    public PeseeLot convertToEntity(PeseeLotDTO dto, Long utilisateurId) {
        PeseeLot entity = new PeseeLot();
        entity.setId(dto.getId());
        entity.setLotPorcId(dto.getLotPorcId());
        entity.setPoidsMoyenKg(dto.getPoidsMoyenKg());
        entity.setDatePesee(dto.getDatePesee());
        entity.setObservation(dto.getObservation());
        entity.setCreatedBy(utilisateurId);
        entity.setCreatedAt(LocalDate.now());
        return entity;
    }

    @Transactional
    public void ajouterPesee(PeseeLotDTO dto, Long utilisateurId) {
        PeseeLot peseeLot = convertToEntity(dto, utilisateurId);
        
        if ( peseeLot.getId() != null ) {
            PeseeLot existingPeseeLot = peseeLotRepository.findById(peseeLot.getId()).orElse(null);
            if (existingPeseeLot != null) {
                existingPeseeLot.setPoidsMoyenKg(peseeLot.getPoidsMoyenKg());
                existingPeseeLot.setDatePesee(peseeLot.getDatePesee());
                existingPeseeLot.setObservation(peseeLot.getObservation());
                existingPeseeLot.setCreatedBy(utilisateurId);
                existingPeseeLot.setCreatedAt(LocalDate.now());
                existingPesseLot.setLotPorcId(peseeLot.getLotPorcId());

                peseeLotRepository.save(existingPeseeLot);
            }
        } else {
            peseeLotRepository.save(peseeLot);
        }

    }

    @Transactional
    public void supprimerPesee(Long id) {
        peseeLotRepository.deleteById(id);
    }

     // List<PeseeLot> findPesees(Long lotId)

    // String ajouterPesee(PeseeLotDTO dto, Long utilisateurId)
    // List<PeseeLot> findPesees(Long lotId)
    // BigDecimal calculerEvolutionPoids(Long lotId)
    // void mettreAJourPoidsMoyenActuel(Long lotId, BigDecimal poidsMoyenKg)
    // String validerPesee(PeseeLotDTO dto)

}
