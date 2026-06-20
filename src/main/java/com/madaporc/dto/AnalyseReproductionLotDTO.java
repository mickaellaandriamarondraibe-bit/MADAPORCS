package com.madaporc.dto;

import java.time.LocalDateTime;

public class AnalyseReproductionLotDTO {
    private Long id;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotPorc() {
        return lotPorc;
    }

    public void setLotPorc(String lotPorc) {
        this.lotPorc = lotPorc;
    }

    public LocalDateTime getDateAnalyse() {
        return dateAnalyse;
    }

    public void setDateAnalyse(LocalDateTime dateAnalyse) {
        this.dateAnalyse = dateAnalyse;
    }

    public Integer getNbPretesJamaisSaillies() {
        return nbPretesJamaisSaillies;
    }

    public void setNbPretesJamaisSaillies(Integer nbPretesJamaisSaillies) {
        this.nbPretesJamaisSaillies = nbPretesJamaisSaillies;
    }

    public Integer getNbDejaReproductricesAptes() {
        return nbDejaReproductricesAptes;
    }

    public void setNbDejaReproductricesAptes(Integer nbDejaReproductricesAptes) {
        this.nbDejaReproductricesAptes = nbDejaReproductricesAptes;
    }

    public Integer getNbEnCycle() {
        return nbEnCycle;
    }

    public void setNbEnCycle(Integer nbEnCycle) {
        this.nbEnCycle = nbEnCycle;
    }

    public Integer getNbASurveiller() {
        return nbASurveiller;
    }

    public void setNbASurveiller(Integer nbASurveiller) {
        this.nbASurveiller = nbASurveiller;
    }

    public Integer getNbARetirerReproduction() {
        return nbARetirerReproduction;
    }

    public void setNbARetirerReproduction(Integer nbARetirerReproduction) {
        this.nbARetirerReproduction = nbARetirerReproduction;
    }

    public Integer getNbFemellesTotal() {
        return nbFemellesTotal;
    }

    public void setNbFemellesTotal(Integer nbFemellesTotal) {
        this.nbFemellesTotal = nbFemellesTotal;
    }

    public Integer getNbFemellesSailliesTotal() {
        return nbFemellesSailliesTotal;
    }

    public void setNbFemellesSailliesTotal(Integer nbFemellesSailliesTotal) {
        this.nbFemellesSailliesTotal = nbFemellesSailliesTotal;
    }

    public Integer getNbFemellesGestantesTotal() {
        return nbFemellesGestantesTotal;
    }

    public void setNbFemellesGestantesTotal(Integer nbFemellesGestantesTotal) {
        this.nbFemellesGestantesTotal = nbFemellesGestantesTotal;
    }

    public Double getTauxAptitudeGlobal() {
        return tauxAptitudeGlobal;
    }

    public void setTauxAptitudeGlobal(Double tauxAptitudeGlobal) {
        this.tauxAptitudeGlobal = tauxAptitudeGlobal;
    }

    public Double getTauxRecommande() {
        return tauxRecommande;
    }

    public void setTauxRecommande(Double tauxRecommande) {
        this.tauxRecommande = tauxRecommande;
    }

    public Double getTauxFertiliteObserve() {
        return tauxFertiliteObserve;
    }

    public void setTauxFertiliteObserve(Double tauxFertiliteObserve) {
        this.tauxFertiliteObserve = tauxFertiliteObserve;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public AnalyseReproductionLotDTO() {
    }
}