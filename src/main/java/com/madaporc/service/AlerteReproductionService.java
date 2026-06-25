package com.madaporc.service;
import com.madaporc.dto.AnalyseReproductionLotDTO;
import com.madaporc.model.AlerteReproduction;
import com.madaporc.repository.AlerteReproductionRepository;
import com.madaporc.service.StockService;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class AlerteReproductionService {
    private final AlerteReproductionRepository alerteReproductionRepository;
    private final GroupeReproductionMiseBasService groupeReproductionMiseBasService;
    private final AnalyseReproductionService analyseReproductionService;
    private final StockService stockService;

    public AlerteReproductionService(AlerteReproductionRepository alerteReproductionRepository,
                                     GroupeReproductionMiseBasService groupeReproductionMiseBasService,
                                     AnalyseReproductionService analyseReproductionService,
                                     StockService stockService) {
        this.alerteReproductionRepository = alerteReproductionRepository;
        this.groupeReproductionMiseBasService = groupeReproductionMiseBasService;
        this.analyseReproductionService = analyseReproductionService;
        this.stockService = stockService;
    }

    public long countAlerteReproduction() {
        return alerteReproductionRepository.count();
    }

    public void insertAlertes(AlertesReproduction alerte) {
        alerteReproductionRepository.save(alerte);
    }

    @Scheduled(fixedRate = 10000)
    public void getAlertesByNotif(Long groupeId, Long lotId) {
        int jourEstim = 3;
        long jourRestant = groupeReproductionMiseBasService.calculerJoursRestants(groupeId);
        if (jourEstim >= jourRestant) {
            AlerteReproduction alerte = new AlerteReproduction();
            alerte.setGroupeId(groupeId);
            alerte.setLotId(lotId);
            alerte.setType("MISE_BAS_PROCHE");
            alerte.setMessage("Le groupe " + groupeId + " du lot " + lotId + " a une mise bas proche.");
            insertAlertes(alerte);
        } else if (jourRestant < 0) {
            AlerteReproduction alerte = new AlerteReproduction();
            alerte.setGroupeId(groupeId);
            alerte.setLotId(lotId);
            alerte.setType("RETARD_MISE_BAS");
            alerte.setMessage("Le groupe " + groupeId + " du lot " + lotId + " a une mise bas en retard.");
            insertAlertes(alerte);
        }else {
            continue;
        }

        AnalyseReproductionLotDTO analyseDTO =  analyseReproductionService.analyserLot(lotId);
        String casAlertes = genererDecision(analyseDTO);
        if(casAlertes.equals("REFORME_RECOMMANDEE")) {
            AlerteReproduction alerte = new AlerteReproduction();
            alerte.setGroupeId(groupeId);
            alerte.setLotId(lotId);
            alerte.setType("REFORME_RECOMMANDEE");
            alerte.setMessage("Le groupe " + groupeId + " du lot " + lotId + " a une réforme recommandée.");
            insertAlertes(alerte);
        } else if(casAlertes.equals("SURVEILLANCE_LOT")) {
            AlerteReproduction alerte = new AlerteReproduction();
            alerte.setGroupeId(groupeId);
            alerte.setLotId(lotId);
            alerte.setType("SURVEILLANCE_LOT");
            alerte.setMessage("Le groupe " + groupeId + " du lot " + lotId + " nécessite une surveillance.");
            insertAlertes(alerte);
        } else {
            continue;
        }
        List<Ingredient> ingredients = stockService.listStocksFaibles();
        if(ingredients.size() == 0) {
            AlerteReproduction alerte = new AlerteReproduction();
            alerte.setGroupeId(groupeId);
            alerte.setLotId(lotId);
            alerte.setType("STOCK_FAIBLE");
            alerte.setMessage("Le groupe " + groupeId + " du lot " + lotId + " a un stock faible.");
            insertAlertes(alerte);
        }
        
        
    }
    






    // public List<AlerteReproduction> listerAlertesActives(){}
    // public List<AlerteReproduction> listerAlertesActives()
    // public String marquerCommeLue(Long id)
    // public String traiter(Long id)

}

            // 'MISE_BAS_PROCHE',
            // 'RETARD_MISE_BAS',
            // 'SURVEILLANCE_LOT',
            // 'REFORME_RECOMMANDEE',
            // 'STOCK_FAIBLE',
            // 'VACCINATION_A_VENIR'
            
