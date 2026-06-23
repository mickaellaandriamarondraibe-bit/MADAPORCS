package com.madaporc.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.repository.GroupeReproductionRepository;

@Service

public class GroupeReproductionMiseBasService {

    private final GroupeReproductionRepository groupeRepository;

    public GroupeReproductionMiseBasService(GroupeReproductionRepository groupeRepository) {
        this.groupeRepository = groupeRepository;
    }

    // Fonction pour afficher tout les details d'un groupe
    public GroupeReproductionDetailDTO getDetailGroupe(Long id) {
        GroupeReproduction groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        GroupeReproductionDetailDTO dto = new GroupeReproductionDetailDTO();
        dto.setId(groupe.getId());
        dto.setCodeGroupe(groupe.getCodeGroupe());
        dto.setLotFemelleId(groupe.getLotFemelle().getId());
        dto.setLotMaleId(groupe.getLotMale().getId());
        dto.setNombreFemellesConcernees(groupe.getNombreFemellesConcernees());
        dto.setNombreMalesUtilises(groupe.getNombreMalesUtilises());
        dto.setDateSaillie(groupe.getDateSaillie());
        dto.setDureeGestationJours(groupe.getDureeGestationJours());
        dto.setDatePrevueMiseBas(groupe.getDatePrevueMiseBas());
        dto.setDateMiseBasReelle(groupe.getDateMiseBasReelle());
        dto.setNbFemellesGestantes(groupe.getNbFemellesGestantes());
        dto.setNbFemellesNonGestantes(groupe.getNbFemellesNonGestantes());
        dto.setNbFemellesMiseBas(groupe.getNbFemellesMiseBas());
        dto.setNbPorceletsNes(groupe.getNbPorceletsNes());
        dto.setNbPorceletsVivants(groupe.getNbPorceletsVivants());
        dto.setNbPorceletsMorts(groupe.getNbPorceletsMorts());
        dto.setStatut(groupe.getStatut());
        dto.setObservation(groupe.getObservation());
        dto.setCreatedAt(groupe.getCreatedAt());
        dto.setUpdatedAt(groupe.getUpdatedAt());

        return dto;
    }

    // fonction pour calculer les jours restants jusqu'a mis bas : si aujourd'hui avant date d'accuplement alors forcement il reste 114 jours 
    public Long calculerJoursRestants(Long id) {
        GroupeReproduction groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        LocalDate aujourdHui = LocalDate.now();
        LocalDate dateDebutCalcul = aujourdHui.isBefore(groupe.getDateSaillie()) ? groupe.getDateSaillie() : aujourdHui;
        Long jourRestante = ChronoUnit.DAYS.between(
            dateDebutCalcul,
            groupe.getDatePrevueMiseBas()
        );
        return Math.max(jourRestante, 0);
    }

    public int calculerPourcentageEvolution(Long id){
        GroupeReproduction groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));
        Long periodeGestation = ChronoUnit.DAYS.between( //Souvent 114j
            groupe.getDateSaillie(),
            groupe.getDatePrevueMiseBas()
        ); 
        Long joursPassee =  ChronoUnit.DAYS.between( //Souvent 114j
            groupe.getDateSaillie(),
            LocalDate.now()
        ); 

        int pourcentage = (int) ((joursPassee * 100) / periodeGestation);

      return 0;
    }


}
