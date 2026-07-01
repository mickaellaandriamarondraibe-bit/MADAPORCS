package com.madaporc.dto;

import java.time.LocalDate;

public class LotFiltreDTO {
    private String codeLot;
    private Long raceId;
    private String sexe;
    private String objectif;
    private String origine;
    private String statut;
    private LocalDate dateCreationDebut;
    private LocalDate dateCreationFin;

    public LotFiltreDTO() {
    }

    public String getCodeLot() {
        return codeLot;
    }

    public void setCodeLot(String codeLot) {
        this.codeLot = codeLot;
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

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LocalDate getDateCreationDebut() {
        return dateCreationDebut;
    }

    public void setDateCreationDebut(LocalDate dateCreationDebut) {
        this.dateCreationDebut = dateCreationDebut;
    }

    public LocalDate getDateCreationFin() {
        return dateCreationFin;
    }

    public void setDateCreationFin(LocalDate dateCreationFin) {
        this.dateCreationFin = dateCreationFin;
    }
}