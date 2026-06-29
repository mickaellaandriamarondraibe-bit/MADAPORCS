package com.madaporc.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.model.LotPorc;
import com.madaporc.model.ParametreReproductionRace;
import com.madaporc.model.RepartitionReproductiveLot;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.ParametreReproductionRaceRepository;
import com.madaporc.repository.RepartitionReproductiveLotRepository;

import jakarta.transaction.Transactional;

@Service
public class RepartitionReproductiveService {

    @Autowired
    private RepartitionReproductiveLotRepository repartitionReproductiveLotRepository;

    @Autowired
    private LotPorcRepository lotPorcRepository;

    @Autowired
    private ParametreReproductionRaceRepository parametreReproductionRaceRepository;

    /*
     * Classe les femelles d'un lot selon leur âge.
     *
     * L'âge réel grandit tout seul avec le temps :
     *     âge réel = âge saisi à l'achat + nombre de mois écoulés depuis la création.
     *
     * Règle simple :
     *   - âge inconnu          -> Prête (on suppose apte)
     *   - âge < âge minimum     -> À surveiller (trop jeune)
     *   - âge entre min et max  -> Prête jamais saillie
     *   - âge > âge maximum     -> À retirer (trop vieille)
     *
     * Cette méthode peut être appelée plusieurs fois (création, puis "Régénérer
     * l'analyse") : à chaque fois elle recalcule l'âge et remet à jour le statut.
     */
    @Transactional
    public void initialiserRepartitionLotFemelle(Long lotId) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null) {
            return;
        }

        // Seuls les lots FEMELLE ont une répartition reproductive.
        if (!"FEMELLE".equalsIgnoreCase(lot.getSexe())) {
            return;
        }

        int effectif = lot.getEffectifInitial() != null ? lot.getEffectifInitial() : 0;
        if (effectif <= 0) {
            return;
        }

        // Femelles déjà engagées en reproduction : on ne les reclasse PAS
        // (elles ont été placées là par le module de reproduction).
        int nbEnCycle = getQuantite(lotId, "EN_CYCLE");
        int nbDejaApte = getQuantite(lotId, "DEJA_REPRODUCTRICE_APTE");

        // Femelles "libres" (jamais saillies) que l'on classe selon l'âge.
        int nbLibres = effectif - nbEnCycle - nbDejaApte;
        if (nbLibres < 0) {
            nbLibres = 0;
        }

        // 1) Âge réel (âge à l'achat + mois écoulés).
        Integer ageMois = calculerAgeReel(lot);

        // 2) Bornes d'âge de la race (valeurs par défaut si non renseignées).
        int ageMin = 8;
        int ageMax = 60;
        if (lot.getRace() != null) {
            ParametreReproductionRace param = parametreReproductionRaceRepository
                    .findByRaceId(lot.getRace().getId())
                    .orElse(null);
            if (param != null) {
                if (param.getAgeMinReproductionMois() != null) {
                    ageMin = param.getAgeMinReproductionMois();
                }
                if (param.getAgeMaxReproductionMois() != null) {
                    ageMax = param.getAgeMaxReproductionMois();
                }
            }
        }

        // 3) Choix du statut selon l'âge.
        String statut;
        if (ageMois == null) {
            statut = "PRETE_JAMAIS_SAILLIE";
        } else if (ageMois < ageMin) {
            statut = "A_SURVEILLER";
        } else if (ageMois <= ageMax) {
            statut = "PRETE_JAMAIS_SAILLIE";
        } else {
            statut = "A_RETIRER_REPRODUCTION";
        }

        // 4) On remet à zéro les 3 statuts liés à l'âge,
        //    puis on place toutes les femelles libres dans le bon statut.
        enregistrerRepartition(lot, "PRETE_JAMAIS_SAILLIE", 0);
        enregistrerRepartition(lot, "A_SURVEILLER", 0);
        enregistrerRepartition(lot, "A_RETIRER_REPRODUCTION", 0);
        enregistrerRepartition(lot, statut, nbLibres);

        // On s'assure que les lignes EN_CYCLE et DEJA_REPRODUCTRICE_APTE existent
        // (à 0 au départ). C'est important : la création d'un groupe ajoute les
        // femelles dans EN_CYCLE, et ce code a besoin que la ligne existe déjà.
        enregistrerRepartition(lot, "EN_CYCLE", nbEnCycle);
        enregistrerRepartition(lot, "DEJA_REPRODUCTRICE_APTE", nbDejaApte);
    }

    /*
     * Calcule l'âge réel du lot en mois.
     * On part de l'âge saisi à l'achat et on ajoute les mois passés depuis la création.
     */
    private Integer calculerAgeReel(LotPorc lot) {
        int moisEcoules = 0;
        if (lot.getDateCreation() != null) {
            moisEcoules = (int) ChronoUnit.MONTHS.between(lot.getDateCreation(), LocalDate.now());
            if (moisEcoules < 0) {
                moisEcoules = 0;
            }
        }

        Integer ageAchat = lot.getAgeMois();
        if (ageAchat != null) {
            // Lot acheté : âge saisi + mois écoulés.
            return ageAchat + moisEcoules;
        }

        // Pas d'âge saisi (ex: lot né sur l'exploitation) : âge = mois depuis la création.
        if (lot.getDateCreation() != null) {
            return moisEcoules;
        }

        return null;
    }

    /*
     * Crée (ou met à jour) la ligne de répartition d'un lot pour un statut donné.
     */
    @Transactional
    public void mettreAJourRepartition(Long lotId, String statut, Integer quantite) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null) {
            return;
        }
        enregistrerRepartition(lot, statut, quantite != null ? quantite : 0);
    }

    // Après une mise bas : les femelles quittent "En cycle".
    // Celles qui ont mis bas -> "Déjà reproductrices aptes".
    // Les autres (échec) -> "À surveiller".
    @Transactional
    public void mettreAJourApresMiseBas(Long lotId, int nbMiseBas, int nbConcernees) {
        LotPorc lot = lotPorcRepository.findById(lotId).orElse(null);
        if (lot == null) {
            return;
        }

        int enCycle = getQuantite(lotId, "EN_CYCLE");
        int dejaApte = getQuantite(lotId, "DEJA_REPRODUCTRICE_APTE");
        int aSurveiller = getQuantite(lotId, "A_SURVEILLER");

        int echec = nbConcernees - nbMiseBas;
        if (echec < 0) {
            echec = 0;
        }

        enregistrerRepartition(lot, "EN_CYCLE", Math.max(enCycle - nbConcernees, 0));
        enregistrerRepartition(lot, "DEJA_REPRODUCTRICE_APTE", dejaApte + nbMiseBas);
        enregistrerRepartition(lot, "A_SURVEILLER", aSurveiller + echec);
    }

    private void enregistrerRepartition(LotPorc lot, String statut, int quantite) {
        RepartitionReproductiveLot repartition = repartitionReproductiveLotRepository
                .findByLotIdAndStatutReproductif(lot.getId(), statut)
                .orElse(new RepartitionReproductiveLot());

        repartition.setLot(lot);
        repartition.setStatutReproductif(statut);
        repartition.setQuantite(quantite);

        repartitionReproductiveLotRepository.save(repartition);
    }

    private int getQuantite(Long lotId, String statut) {
        Integer quantite = repartitionReproductiveLotRepository
                .findQuantiteByLotIdAndStatutCode(lotId, statut);
        return quantite != null ? quantite : 0;
    }

    @Transactional
    public List<RepartitionReproductiveLot> getRepartitionByLot(Long lotId) {
        return repartitionReproductiveLotRepository.findByLotId(lotId);
    }

    public RepartitionReproductiveLotRepository getRepartitionReproductiveLotRepository() {
        return repartitionReproductiveLotRepository;
    }
}
