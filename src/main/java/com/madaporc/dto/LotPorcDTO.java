package com.madaporc.dto;

import java.time.LocalDate;

public class LotPorcDTO {
    private Long id;
    private String codeLot;
    private LocalDate dateCreation;
    private Long raceId;
    private String sexe;
    private String objectif;
    private String origine;
    private Integer effectifInitial;
    private Integer effectifActuel;
    private String statut;
    private Long lotParentId;
    private Long groupeReproductionOrigineId;
    private String description;

    public LotPorcDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeLot() {
        return codeLot;
    }

    public void setCodeLot(String codeLot) {
        this.codeLot = codeLot;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getRaceId() {
        return raceId;
    }

    public void setRaceId(Long raceId) {
        this.raceId = raceId;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Integer getEffectifInitial() {
        return effectifInitial;
    }

    public void setEffectifInitial(Integer effectifInitial) {
        this.effectifInitial = effectifInitial;
    }

    public Integer getEffectifActuel() {
        return effectifActuel;
    }

    public void setEffectifActuel(Integer effectifActuel) {
        this.effectifActuel = effectifActuel;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Long getLotParentId() {
        return lotParentId;
    }

    public void setLotParentId(Long lotParentId) {
        this.lotParentId = lotParentId;
    }

    public Long getGroupeReproductionOrigineId() {
        return groupeReproductionOrigineId;
    }

    public void setGroupeReproductionOrigineId(Long groupeReproductionOrigineId) {
        this.groupeReproductionOrigineId = groupeReproductionOrigineId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}