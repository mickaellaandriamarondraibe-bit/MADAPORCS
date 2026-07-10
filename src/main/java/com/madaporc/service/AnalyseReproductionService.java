package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.madaporc.dto.AnalyseReproductionLotDTO;
import com.madaporc.model.AnalyseReproductionLot;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.AnalyseReproductionLotRepository;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.RepartitionReproductiveLotRepository;

@Service
public class AnalyseReproductionService {

    @Autowired
    private RepartitionReproductiveLotRepository repartitionReproductiveLotRepository;

    @Autowired
    private RepartitionReproductiveService service;

    @Autowired
    private LotPorcRepository lotPorcRepository;

    @Autowired
    private GroupeReproductionRepository groupeReproductionRepository;

    @Autowired
    private AnalyseReproductionLotRepository analyseReproductionLotRepository;

    public LotPorcRepository getLotPorcRepository() {
        return lotPorcRepository;
    }

    public AnalyseReproductionLotDTO analyserDTO(Long lotId) {
        LotPorc lotPorc = lotPorcRepository.findById(lotId)
                .orElseThrow(() -> new RuntimeException("Lot non trouvé : " + lotId));

        if (!"FEMELLE".equals(lotPorc.getSexe())) {
            throw new RuntimeException("L'analyse reproductive concerne seulement les lots femelles.");
        }

        Integer nbPretesJamaisSaillies = valeurZero(
                repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "PRETE_JAMAIS_SAILLIE")
        );

        Integer nbDejaReproductricesAptes = valeurZero(
                repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "DEJA_REPRODUCTRICE_APTE")
        );

        Integer nbEnCycle = valeurZero(
                repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "EN_CYCLE")
        );

        Integer nbASurveiller = valeurZero(
                repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "A_SURVEILLER")
        );

        Integer nbARetirerReproduction = valeurZero(
                repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "A_RETIRER_REPRODUCTION")
        );

        Integer nbFemellesTotal = nbPretesJamaisSaillies
                + nbDejaReproductricesAptes
                + nbEnCycle
                + nbASurveiller
                + nbARetirerReproduction;

        Integer nbFemellesSailliesTotal = nbEnCycle + + nbDejaReproductricesAptes;

        Integer nbFemellesGestantesTotal = nbEnCycle;

        AnalyseReproductionLotDTO analyseReproductionLotDTO = new AnalyseReproductionLotDTO();

        analyseReproductionLotDTO.setLotPorc(lotPorc.getCodeLot());
        analyseReproductionLotDTO.setDateAnalyse(LocalDateTime.now());

        analyseReproductionLotDTO.setNbPretesJamaisSaillies(nbPretesJamaisSaillies);
        analyseReproductionLotDTO.setNbDejaReproductricesAptes(nbDejaReproductricesAptes);
        analyseReproductionLotDTO.setNbEnCycle(nbEnCycle);
        analyseReproductionLotDTO.setNbASurveiller(nbASurveiller);
        analyseReproductionLotDTO.setNbARetirerReproduction(nbARetirerReproduction);

        analyseReproductionLotDTO.setNbFemellesTotal(nbFemellesTotal);
        analyseReproductionLotDTO.setNbFemellesSailliesTotal(nbFemellesSailliesTotal);
        analyseReproductionLotDTO.setNbFemellesGestantesTotal(nbFemellesGestantesTotal);

        BigDecimal tauxAptitudeGlobal = calculerTauxAptitudeGlobal(analyseReproductionLotDTO);
        BigDecimal tauxRecommande = calculerTauxRecommande(analyseReproductionLotDTO);
        BigDecimal tauxFertiliteObserve = calculerTauxFertiliteObserve(analyseReproductionLotDTO);

        analyseReproductionLotDTO.setTauxAptitudeGlobal(tauxAptitudeGlobal.doubleValue());
        analyseReproductionLotDTO.setTauxRecommande(tauxRecommande.doubleValue());
        analyseReproductionLotDTO.setTauxFertiliteObserve(tauxFertiliteObserve.doubleValue());

        analyseReproductionLotDTO.setDecision(genererDecision(analyseReproductionLotDTO));

        return analyseReproductionLotDTO;
    }

    public BigDecimal calculerTauxAptitudeGlobal(AnalyseReproductionLotDTO analyse) {
        BigDecimal nbFemellesTotal = BigDecimal.valueOf(valeurZero(analyse.getNbFemellesTotal()));

        if (nbFemellesTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal nbPretes = BigDecimal.valueOf(valeurZero(analyse.getNbPretesJamaisSaillies()));
        BigDecimal nbDejaAptes = BigDecimal.valueOf(valeurZero(analyse.getNbDejaReproductricesAptes()));
        BigDecimal nbASurveiller = BigDecimal.valueOf(valeurZero(analyse.getNbASurveiller()));

        BigDecimal total = nbPretes.add(nbDejaAptes).add(nbASurveiller);

        return total
                .multiply(BigDecimal.valueOf(100))
                .divide(nbFemellesTotal, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculerTauxRecommande(AnalyseReproductionLotDTO analyse) {
        BigDecimal nbFemellesTotal = BigDecimal.valueOf(valeurZero(analyse.getNbFemellesTotal()));

        if (nbFemellesTotal.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal nbPretes = BigDecimal.valueOf(valeurZero(analyse.getNbPretesJamaisSaillies()));
        BigDecimal nbDejaAptes = BigDecimal.valueOf(valeurZero(analyse.getNbDejaReproductricesAptes()));
        // Les femelles EN_CYCLE sont comptees au denominateur (nbFemellesTotal) : il faut aussi
        // les compter ici, sinon un lot entierement en cycle tombe a 0 % et est classe a tort en reforme.
        BigDecimal nbEnCycle = BigDecimal.valueOf(valeurZero(analyse.getNbEnCycle()));

        BigDecimal total = nbPretes.add(nbDejaAptes).add(nbEnCycle);

        return total
                .multiply(BigDecimal.valueOf(100))
                .divide(nbFemellesTotal, 2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculerTauxFertiliteObserve(AnalyseReproductionLotDTO analyse) {
        LotPorc lot = lotPorcRepository.findByCodeLot(analyse.getLotPorc())
                .orElseThrow(() -> new RuntimeException("Lot non trouvé: " + analyse.getLotPorc()));

        List<GroupeReproduction> groupes =
                groupeReproductionRepository.findByLotFemelleId(lot.getId());

        BigDecimal nbFemellesSaillies = BigDecimal.ZERO;
        BigDecimal nbFemellesGestantes = BigDecimal.ZERO;

        for (GroupeReproduction groupe : groupes) {

            if (!"SAILLIE".equals(groupe.getStatut())) {

                nbFemellesSaillies = nbFemellesSaillies.add(
                        BigDecimal.valueOf(valeurZero(groupe.getNombreFemellesConcernees()))
                );

                nbFemellesGestantes = nbFemellesGestantes.add(
                        BigDecimal.valueOf(valeurZero(groupe.getNbFemellesGestantes()))
                );
            }
        }

        if (nbFemellesSaillies.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return nbFemellesGestantes
                .multiply(BigDecimal.valueOf(100))
                .divide(nbFemellesSaillies, 2, RoundingMode.HALF_UP);
    }

    public String genererDecision(AnalyseReproductionLotDTO analyse) {
        BigDecimal tauxRecommande = BigDecimal.valueOf(valeurZeroDouble(analyse.getTauxRecommande()));
        BigDecimal tauxFertilite = BigDecimal.valueOf(valeurZeroDouble(analyse.getTauxFertiliteObserve()));
        Integer lot = service.calculerAgeReel(lotPorcRepository.findByCodeLot(analyse.getLotPorc())
                .orElseThrow(() -> new RuntimeException("Lot non trouvé: " + analyse.getLotPorc())));

        if (tauxRecommande.compareTo(BigDecimal.valueOf(80)) >= 0
                && tauxFertilite.compareTo(BigDecimal.valueOf(80)) >= 0) {

            return "APTE A LA REPRODUCTION";
        }
        if (tauxRecommande.compareTo(BigDecimal.valueOf(50)) >= 0
                && tauxFertilite.compareTo(BigDecimal.valueOf(60)) >= 0) {

            return "A SURVEILLER , TAUX INFERIEUR A 50 % ";
        }

        if (lot.compareTo(Integer.valueOf(8)) < 0) {
            return "Lot est trop jeune pour être évalué" + "(âge : " + lot + " mois)";
        }

        return "REFORME RECOMMANDEE";
    }

    @Transactional
    public void enregistrerAnalyse(AnalyseReproductionLotDTO analyse) {
        AnalyseReproductionLot analyseEntity = new AnalyseReproductionLot();

        LotPorc lot = lotPorcRepository.findByCodeLot(analyse.getLotPorc())
                .orElseThrow(() -> new RuntimeException("Lot non trouvé: " + analyse.getLotPorc()));

        BigDecimal tauxAptitudeGlobal = calculerTauxAptitudeGlobal(analyse);
        BigDecimal tauxRecommande = calculerTauxRecommande(analyse);
        BigDecimal tauxFertiliteObserve = calculerTauxFertiliteObserve(analyse);

        analyse.setTauxAptitudeGlobal(tauxAptitudeGlobal.doubleValue());
        analyse.setTauxRecommande(tauxRecommande.doubleValue());
        analyse.setTauxFertiliteObserve(tauxFertiliteObserve.doubleValue());
        analyse.setDecision(genererDecision(analyse));

        analyseEntity.setLotPorc(lot);
        if (analyse.getDateAnalyse() == null) {
            analyseEntity.setDateAnalyse(LocalDateTime.now());
        } else {
            analyseEntity.setDateAnalyse(analyse.getDateAnalyse());
        }

        analyseEntity.setNbPretesJamaisSaillies(valeurZero(analyse.getNbPretesJamaisSaillies()));
        analyseEntity.setNbDejaReproductricesAptes(valeurZero(analyse.getNbDejaReproductricesAptes()));
        analyseEntity.setNbEnCycle(valeurZero(analyse.getNbEnCycle()));
        analyseEntity.setNbASurveiller(valeurZero(analyse.getNbASurveiller()));
        analyseEntity.setNbARetirerReproduction(valeurZero(analyse.getNbARetirerReproduction()));
        analyseEntity.setNbFemellesTotal(valeurZero(analyse.getNbFemellesTotal()));
        analyseEntity.setNbFemellesSailliesTotal(valeurZero(analyse.getNbFemellesSailliesTotal()));
        analyseEntity.setNbFemellesGestantesTotal(valeurZero(analyse.getNbFemellesGestantesTotal()));
        analyseEntity.setTauxAptitudeGlobal(tauxAptitudeGlobal.doubleValue());
        analyseEntity.setTauxRecommande(tauxRecommande.doubleValue());
        analyseEntity.setTauxFertiliteObserve(tauxFertiliteObserve.doubleValue());
        analyseEntity.setDecision(analyse.getDecision());

        analyseReproductionLotRepository.save(analyseEntity);
    }

    private Integer valeurZero(Integer valeur) {
        if (valeur == null) {
            return 0;
        }
        return valeur;
    }

    private Double valeurZeroDouble(Double valeur) {
        if (valeur == null) {
            return 0.0;
        }
        return valeur;
    }

    public List<LotPorc> listerLotsPourAnalyse() {
    List<LotPorc> lotsActifs = lotPorcRepository.findByStatut("ACTIF");

    return lotsActifs.stream()
            .filter(lot -> "FEMELLE".equals(lot.getSexe()))
            .filter(lot -> "REPRODUCTION".equals(lot.getObjectif()))
            .toList();
}
}