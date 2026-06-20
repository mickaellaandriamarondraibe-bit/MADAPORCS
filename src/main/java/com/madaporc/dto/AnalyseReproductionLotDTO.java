package com.madaporc.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class AnalyseReproductionLotDTO {
    private BigInteger id;
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
}