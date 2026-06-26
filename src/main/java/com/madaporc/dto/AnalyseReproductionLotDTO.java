package com.madaporc.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyseReproductionLotDTO {

    private Long id;

    private Long lotId;

    // contient le code du lot, exemple : LOT-F-001
    private String lotPorc;

    private LocalDateTime dateAnalyse;

    private Integer nbPretesJamaisSaillies;
    private Integer nbDejaReproductricesAptes;
    private Integer nbEnCycle;
    private Integer nbASurveiller;
    private Integer nbARetirerReproduction;

    private Integer nbFemellesTotal;
    private Integer nbFemellesSailliesTotal;
    private Integer nbFemellesGestantesTotal;

    private Double tauxAptitudeGlobal;
    private Double tauxRecommande;
    private Double tauxFertiliteObserve;

    private String decision;
    private String commentaire;

    public Integer getFemellesDisponibles() {
        int pretes = nbPretesJamaisSaillies != null ? nbPretesJamaisSaillies : 0;
        int dejaAptes = nbDejaReproductricesAptes != null ? nbDejaReproductricesAptes : 0;
        int aSurveiller = nbASurveiller != null ? nbASurveiller : 0;

        return pretes + dejaAptes + aSurveiller;
    }

    public String getCommentaireDecision() {
        if (commentaire != null && !commentaire.isBlank()) {
            return commentaire;
        }

        if ("APTE A LA REPRODUCTION".equals(decision)) {
            return "Le lot présente de bons indicateurs reproductifs.";
        }

        if ("A SURVEILLER".equals(decision)) {
            return "Le lot peut être utilisé, mais une surveillance est recommandée.";
        }

        if ("REFORME RECOMMANDEE".equals(decision)) {
            return "Le lot présente des indicateurs faibles pour la reproduction.";
        }

        return "";
    }
}