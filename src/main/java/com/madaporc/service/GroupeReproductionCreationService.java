package com.madaporc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.LotPorc;
import com.madaporc.model.ParametreReproductionRace;
import com.madaporc.model.RepartitionReproductiveLot;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.ParametreReproductionRaceRepository;
import com.madaporc.repository.RepartitionReproductiveLotRepository;
import com.madaporc.dto.GroupeReproductionDTO;

@Service
@Transactional
public class GroupeReproductionCreationService {

    private final GroupeReproductionRepository groupeRepository;
    private final LotPorcRepository lotPorcRepository;
    private final RepartitionReproductiveLotRepository repartitionRepository;
    private final ParametreReproductionRaceRepository parametreRepository;

    public GroupeReproductionCreationService(
            GroupeReproductionRepository groupeRepository,
            LotPorcRepository lotPorcRepository,
            RepartitionReproductiveLotRepository repartitionRepository,
            ParametreReproductionRaceRepository parametreRepository) {

        this.groupeRepository = groupeRepository;
        this.lotPorcRepository = lotPorcRepository;
        this.repartitionRepository = repartitionRepository;
        this.parametreRepository = parametreRepository;
    }


    public String creer(GroupeReproductionDTO dto, Long utilisateurId) {
        try {
            // érifications individuelles des lots)
            String erreurFemelle = verifierLotFemelle(dto.getLotFemelleId());
            if (erreurFemelle != null) return erreurFemelle;

            String erreurMale = verifierLotMale(dto.getLotMaleId());
            if (erreurMale != null) return erreurMale;

            // efectif dispo
            String erreurDispo = verifierFemellesDisponibles(dto.getLotFemelleId(), dto.getNombreFemellesConcernees());
            if (erreurDispo != null) return erreurDispo;

            // entite
            LotPorc lotFemelle = lotPorcRepository.findById(dto.getLotFemelleId()).get();
            LotPorc lotMale = lotPorcRepository.findById(dto.getLotMaleId()).get();

            // mise a jour
            mettreAJourRepartitionApresSaillie(dto.getLotFemelleId(), dto.getNombreFemellesConcernees());

            // Calcul de la durée de gestation de la race
            int dureeGestation = 114; 
            if (lotFemelle.getRace() != null) {
                Optional<ParametreReproductionRace> params = parametreRepository.findByRaceId(lotFemelle.getRace().getId());
                if (params.isPresent()) {
                    dureeGestation = params.get().getDureeGestationJours();
                }
            }

            // Calcul de la date prévue de mise bas
            LocalDate datePrevue = calculerDatePrevueMiseBas(dto.getDateSaillie(), dureeGestation);

            //  Sauvegarde du groupe
            GroupeReproduction groupe = new GroupeReproduction();
            groupe.setCodeGroupe("GRP-" + System.currentTimeMillis());
            groupe.setLotFemelle(lotFemelle);
            groupe.setLotMale(lotMale);
            groupe.setNombreFemellesConcernees(dto.getNombreFemellesConcernees());
            groupe.setNombreMalesUtilises(1);
            groupe.setDateSaillie(dto.getDateSaillie());
            groupe.setDureeGestationJours(dureeGestation);
            groupe.setStatut("SAILLIE");
            groupe.setObservation(dto.getObservation() != null ? dto.getObservation() : "Création automatique - Saillie partielle");
            groupe.setCreatedAt(LocalDateTime.now());

            groupeRepository.save(groupe);
            return "SUCCESS";

        } catch (Exception e) {
            return "Erreur lors de la création : " + e.getMessage();
        }
    }

    public String modifier(Long id, GroupeReproductionDTO dto) {
        return "SUCCESS";
    }


    public LocalDate calculerDatePrevueMiseBas(LocalDate dateSaillie, Integer dureeGestation) {
        if (dateSaillie == null) return null;
        int jours = (dureeGestation != null) ? dureeGestation : 114;
        return dateSaillie.plusDays(jours);
    }

    public String verifierLotFemelle(Long lotFemelleId) {
        if (lotFemelleId == null) return "Veuillez sélectionner un lot femelle.";
        Optional<LotPorc> lot = lotPorcRepository.findById(lotFemelleId);
        if (lot.isEmpty()) return "Lot femelle introuvable.";
        if (!"FEMELLE".equals(lot.get().getSexe())) return "Le lot sélectionné doit être de sexe FEMELLE.";
        if (!"ACTIF".equals(lot.get().getStatut())) return "Le lot femelle doit être ACTIF.";
        return null;
    }

    public String verifierLotMale(Long lotMaleId) {
        if (lotMaleId == null) return "Veuillez sélectionner un lot mâle.";
        Optional<LotPorc> lot = lotPorcRepository.findById(lotMaleId);
        if (lot.isEmpty()) return "Lot mâle introuvable.";
        if (!"MALE".equals(lot.get().getSexe())) return "Le lot sélectionné doit être de sexe MALE.";
        if (!"ACTIF".equals(lot.get().getStatut())) return "Le lot mâle doit être ACTIF.";
        return null;
    }

    public String verifierFemellesDisponibles(Long lotFemelleId, Integer nombreFemelles) {
        if (nombreFemelles == null || nombreFemelles <= 0) {
            return "Le nombre de femelles doit être supérieur à 0.";
        }
        List<RepartitionReproductiveLot> repartitions = repartitionRepository.findByLotId(lotFemelleId);
        int totalDisponibles = 0;
        for (RepartitionReproductiveLot r : repartitions) {
            String code = r.getStatutReproductif();
            if ("PRETE_JAMAIS_SAILLIE".equals(code) || "DEJA_REPRODUCTRICE_APTE".equals(code)) {
                totalDisponibles += r.getQuantite();
            }
        }
        if (nombreFemelles > totalDisponibles) {
            return "Nombre de femelles insuffisant. Disponibles : " + totalDisponibles;
        }
        return null;
    }

    public void mettreAJourRepartitionApresSaillie(Long lotId, Integer nombreFemelles) {
        List<RepartitionReproductiveLot> repartitions = repartitionRepository.findByLotId(lotId);
        RepartitionReproductiveLot enCycleRepar = null;
        int restantAEngager = nombreFemelles;

        //On pioche d'abord dans DEJA_REPRODUCTRICE_APTE
        for (RepartitionReproductiveLot r : repartitions) {
            String code = r.getStatutReproductif();
            if ("DEJA_REPRODUCTRICE_APTE".equals(code) && restantAEngager > 0) {
                int aPrelever = Math.min(restantAEngager, r.getQuantite());
                r.setQuantite(r.getQuantite() - aPrelever);
                restantAEngager -= aPrelever;
                repartitionRepository.save(r);
            }
            if ("EN_CYCLE".equals(code)) {
                enCycleRepar = r;
            }
        }

        //Si pas assez, on pioche dans PRETE_JAMAIS_SAILLIE
        if (restantAEngager > 0) {
            for (RepartitionReproductiveLot r : repartitions) {
                String code = r.getStatutReproductif();
                if ("PRETE_JAMAIS_SAILLIE".equals(code)) {
                    int aPrelever = Math.min(restantAEngager, r.getQuantite());
                    r.setQuantite(r.getQuantite() - aPrelever);
                    restantAEngager -= aPrelever;
                    repartitionRepository.save(r);
                }
            }
        }

        // On rajoute le tout dans EN_CYCLE
        if (enCycleRepar != null) {
            enCycleRepar.setQuantite(enCycleRepar.getQuantite() + nombreFemelles);
            repartitionRepository.save(enCycleRepar);
        }
    }
}