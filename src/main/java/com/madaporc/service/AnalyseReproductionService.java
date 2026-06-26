package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.dto.AnalyseReproductionLotDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.repository.RepartitionReproductiveLotRepository;
import com.madaporc.repository.LotPorcRepository;
import java.time.LocalDateTime;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.model.AnalyseReproductionLot;
import com.madaporc.model.GroupeReproduction;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.madaporc.repository.AnalyseReproductionLotRepository;

@Service
public class AnalyseReproductionService {
    @Autowired
    private RepartitionReproductiveLotRepository repartitionReproductiveLotRepository;
    @Autowired
    private LotPorc lotPorc;
    @Autowired
    private LotPorcRepository lotPorcRepository;
    @Autowired
    private GroupeReproductionRepository groupeReproductionRepository;
    @Autowired
    private AnalyseReproductionLotRepository analyseReproductionLotRepository;

    public LotPorcRepository getLotPorcRepository() {
        return lotPorcRepository;
    }

    public AnalyseReproductionLotDTO analyserDTO (Long lotId) {
        LotPorc lotPorc = lotPorcRepository.findById(lotId).orElse(null);
        Integer nbPretesJamaisSaillies = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "PRETE_JAMAIS_SAILLIE");
        Integer nbDejaReproductricesAptes = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "DEJA_REPRODUCTRICE_APTE");
        Integer nbEnCycle = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "EN_CYCLE");
        Integer nbASurveiller = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "A_SURVEILLER");
        Integer nbARetirerReproduction = repartitionReproductiveLotRepository.findQuantiteByLotIdAndStatutCode(lotId, "A_RETIRER_REPRODUCTION");
        Integer nbFemellesTotal = nbPretesJamaisSaillies + nbDejaReproductricesAptes + nbEnCycle + nbASurveiller + nbARetirerReproduction;
        Integer nbFemellesSailliesTotal = nbPretesJamaisSaillies + nbDejaReproductricesAptes + nbEnCycle;
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

        return analyseReproductionLotDTO;
    }

    public BigDecimal calculerTauxAptitudeGlobal (AnalyseReproductionLotDTO analyse) {
        BigDecimal nbFemellesTotal = BigDecimal.valueOf(analyse.getNbFemellesTotal());
        BigDecimal nbPretes = BigDecimal.valueOf(analyse.getNbPretesJamaisSaillies());
        BigDecimal nbDejaAptes = BigDecimal.valueOf(analyse.getNbDejaReproductricesAptes());
        BigDecimal nbASurveiller = BigDecimal.valueOf(analyse.getNbASurveiller());
        BigDecimal total = nbPretes.add(nbDejaAptes).add(nbASurveiller);

        return total.divide(nbFemellesTotal, 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculerTauxRecommande (AnalyseReproductionLotDTO analyse) {
        BigDecimal nbPretes = BigDecimal.valueOf(analyse.getNbPretesJamaisSaillies());
        BigDecimal nbDejaAptes = BigDecimal.valueOf(analyse.getNbDejaReproductricesAptes());
        BigDecimal total = nbPretes.add(nbDejaAptes);

        return total.divide(BigDecimal.valueOf(analyse.getNbFemellesTotal()), 2, BigDecimal.ROUND_HALF_UP).multiply(BigDecimal.valueOf(100));
    }

    public BigDecimal calculerTauxFertiliteObserve(AnalyseReproductionLotDTO analyse) {
        List<GroupeReproduction> groupes =
                groupeReproductionRepository.findByCodeLot(analyse.getLotPorc());

        BigDecimal nbFemellesSaillies = BigDecimal.ZERO;
        BigDecimal nbFemellesGestantes = BigDecimal.ZERO;

        for (GroupeReproduction groupe : groupes) {

            if (!groupe.getStatut().equals("SAILLIE")) {

                nbFemellesSaillies = nbFemellesSaillies.add(
                        BigDecimal.valueOf(groupe.getNombreFemellesConcernees()));

                nbFemellesGestantes = nbFemellesGestantes.add(
                        BigDecimal.valueOf(groupe.getNbFemellesGestantes()));
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
        BigDecimal tauxRecommande = BigDecimal.valueOf(analyse.getTauxRecommande());
        BigDecimal tauxFertilite = BigDecimal.valueOf(analyse.getTauxFertiliteObserve());

        if (tauxRecommande.compareTo(BigDecimal.valueOf(80)) >= 0
                && tauxFertilite.compareTo(BigDecimal.valueOf(80)) >= 0) {

            return "APTE A LA REPRODUCTION";
        }

        if (tauxRecommande.compareTo(BigDecimal.valueOf(50)) >= 0
                && tauxFertilite.compareTo(BigDecimal.valueOf(60)) >= 0) {

            return "A SURVEILLER";
        }

        return "REFORME RECOMMANDEE";
    }

    @Transactional
    public void enregistrerAnalyse(AnalyseReproductionLotDTO analyse) {
        AnalyseReproductionLot analyseEntity = new AnalyseReproductionLot();
        
        LotPorc lot = lotPorcRepository.findByCodeLot(analyse.getLotPorc())
            .orElseThrow(() -> new RuntimeException("Lot non trouvé: " + analyse.getLotPorc()));
        
        analyseEntity.setLotId(lot.getId());
        analyseEntity.setDateAnalyse(analyse.getDateAnalyse());
        analyseEntity.setNbPretesJamaisSaillies(analyse.getNbPretesJamaisSaillies());
        analyseEntity.setNbDejaReproductricesAptes(analyse.getNbDejaReproductricesAptes());
        analyseEntity.setNbEnCycle(analyse.getNbEnCycle());
        analyseEntity.setNbASurveiller(analyse.getNbASurveiller());
        analyseEntity.setNbARetirerReproduction(analyse.getNbARetirerReproduction());
        analyseEntity.setNbFemellesTotal(analyse.getNbFemellesTotal());
        analyseEntity.setNbFemellesSailliesTotal(analyse.getNbFemellesSailliesTotal());
        analyseEntity.setNbFemellesGestantesTotal(analyse.getNbFemellesGestantesTotal());
        analyseEntity.setTauxAptitudeGlobal(calculerTauxAptitudeGlobal(analyse).doubleValue());
        analyseEntity.setTauxRecommande(calculerTauxRecommande(analyse).doubleValue());
        analyseEntity.setTauxFertiliteObserve(calculerTauxFertiliteObserve(analyse).doubleValue());
        analyseEntity.setDecision(genererDecision(analyse));
        
        analyseReproductionLotRepository.save(analyseEntity);
    }
}