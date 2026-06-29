package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.madaporc.dto.ConfirmationMiseBasDTO;
import com.madaporc.dto.GroupeReproductionDetailDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.repository.GroupeReproductionRepository;

import jakarta.transaction.Transactional;

@Service

public class GroupeReproductionMiseBasService {

    private final GroupeReproductionRepository groupeRepository;
    private final RepartitionReproductiveService repartitionReproductiveService;

    public GroupeReproductionMiseBasService(
            GroupeReproductionRepository groupeRepository,
            RepartitionReproductiveService repartitionReproductiveService) {
        this.groupeRepository = groupeRepository;
        this.repartitionReproductiveService = repartitionReproductiveService;
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

    // fonction pour calculer les jours restants jusqu'a mis bas : si aujourd'hui
    // avant date d'accuplement alors forcement il reste 114 jours
    public Long calculerJoursRestants(Long id) {
        GroupeReproduction groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        LocalDate aujourdHui = LocalDate.now();
        LocalDate dateDebutCalcul = aujourdHui.isBefore(groupe.getDateSaillie()) ? groupe.getDateSaillie() : aujourdHui;
        Long jourRestante = ChronoUnit.DAYS.between(
                dateDebutCalcul,
                groupe.getDatePrevueMiseBas());
        return Math.max(jourRestante, 0);
    }

    public int calculerPourcentageEvolution(Long id) {
        GroupeReproduction groupe = groupeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));
        Long periodeGestation = ChronoUnit.DAYS.between( // Souvent 114j
                groupe.getDateSaillie(),
                groupe.getDatePrevueMiseBas());
        Long joursPassee = ChronoUnit.DAYS.between( // Souvent 114j
                groupe.getDateSaillie(),
                LocalDate.now());

        int pourcentage = (int) ((joursPassee * 100) / periodeGestation);
        if (pourcentage < 0) {
            return 0;
        }

        if( pourcentage >100){
            return 100;
        }
        return pourcentage;
    }

    //fontcion pour la confirmation de mise bas , 
    @Transactional
    public GroupeReproductionDetailDTO confirmerMiseBas(Long groupeId, ConfirmationMiseBasDTO dto) {
        GroupeReproduction groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        validerDonneesMiseBas(groupe, dto);
        mettreAJourGroupeApresMiseBas(groupe, dto);

        groupeRepository.save(groupe);

        // Les femelles quittent "En cycle" : mise à jour de la répartition du lot.
        repartitionReproductiveService.mettreAJourApresMiseBas(
                groupe.getLotFemelle().getId(),
                groupe.getNbFemellesMiseBas(),
                groupe.getNombreFemellesConcernees());
        //id null      → INSERT
        //id existe    → UPDATE
        return getDetailGroupe(groupeId);
    }

    private void mettreAJourGroupeApresMiseBas(
            GroupeReproduction groupe,
            ConfirmationMiseBasDTO dto
    ) {
        groupe.setDateMiseBasReelle(dto.getDateMiseBasReelle());
        
        groupe.setNbFemellesGestantes(dto.getNbFemellesGestantes());
        groupe.setNbFemellesNonGestantes(dto.getNbFemellesNonGestantes());
        groupe.setNbFemellesMiseBas(dto.getNbFemellesMiseBas());

        groupe.setNbPorceletsNes(dto.getNbPorceletsNes());
        groupe.setNbPorceletsVivants(dto.getNbPorceletsVivants());
        groupe.setNbPorceletsMorts(dto.getNbPorceletsMorts());

        groupe.setObservation(dto.getObservation());
        groupe.setUpdatedAt(LocalDateTime.now());

        if (dto.getNbFemellesMiseBas() > 0 && dto.getNbPorceletsVivants() > 0) {
            groupe.setStatut("MISE_BAS_CONFIRMEE");
        } else {
            groupe.setStatut("ECHEC");
        }
    }

    //validation des donner envoyer par l'user 
    private void validerDonneesMiseBas(GroupeReproduction groupe, ConfirmationMiseBasDTO dto) {
        if ("MISE_BAS_CONFIRMEE".equals(groupe.getStatut()) || "CLOTURE".equals(groupe.getStatut())) {
            throw new IllegalArgumentException("Ce groupe est déjà confirmé ou clôturé.");
        }
        if (dto.getDateMiseBasReelle().isBefore(groupe.getDateSaillie())) {
            throw new IllegalArgumentException("La date de mise bas ne peut pas être avant la date de saillie.");
        }
        if (dto.getNbFemellesGestantes() + dto.getNbFemellesNonGestantes()
                > groupe.getNombreFemellesConcernees()) {
            throw new IllegalArgumentException("Le total gestantes + non gestantes dépasse les femelles concernées.");
        }
        if (dto.getNbFemellesMiseBas() > dto.getNbFemellesGestantes()) {
            throw new IllegalArgumentException("Les femelles ayant mis bas dépassent les femelles gestantes.");
        }
        if (dto.getNbPorceletsVivants() + dto.getNbPorceletsMorts()
                > dto.getNbPorceletsNes()) {
            throw new IllegalArgumentException("Vivants + morts dépasse le nombre de porcelets nés.");
        }
        if (dto.getNbFemellesMiseBas() == 0 && dto.getNbPorceletsNes() > 0) {
            throw new IllegalArgumentException("Impossible d'avoir des porcelets si aucune femelle n'a mis bas.");
        }
    }

    // Cloture le groupe : seulement apres une mise bas confirmee (ou un echec).
    @Transactional
    public void cloturer(Long groupeId) {
        GroupeReproduction groupe = groupeRepository.findById(groupeId)
                .orElseThrow(() -> new IllegalArgumentException("Groupe de reproduction introuvable."));

        if ("CLOTURE".equals(groupe.getStatut())) {
            throw new IllegalArgumentException("Ce groupe est déjà clôturé.");
        }
        if (!"MISE_BAS_CONFIRMEE".equals(groupe.getStatut()) && !"ECHEC".equals(groupe.getStatut())) {
            throw new IllegalArgumentException("Le groupe doit être confirmé (mise bas) avant la clôture.");
        }

        groupe.setStatut("CLOTURE");
        groupe.setUpdatedAt(LocalDateTime.now());
        groupeRepository.save(groupe);
    }

}
