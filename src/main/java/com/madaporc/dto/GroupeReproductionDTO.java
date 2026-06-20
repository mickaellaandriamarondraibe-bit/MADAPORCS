package com.madaporc.dto;

import java.time.LocalDate;

public class GroupeReproductionDTO {

    private Long id;

    private String codeGroupe;

    private Long lotFemelleId;
    private String codeLotFemelle;

    private Long lotMaleId;
    private String codeLotMale;

    private Integer nombreFemellesConcernees;
    private Integer nombreMalesUtilises;

    private LocalDate dateSaillie;
    private LocalDate datePrevueMiseBas;

    private String statut;

    private String observation;

    public GroupeReproductionDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodeGroupe() {
        return codeGroupe;
    }

    public void setCodeGroupe(String codeGroupe) {
        this.codeGroupe = codeGroupe;
    }

    public Long getLotFemelleId() {
        return lotFemelleId;
    }

    public void setLotFemelleId(Long lotFemelleId) {
        this.lotFemelleId = lotFemelleId;
    }

    public String getCodeLotFemelle() {
        return codeLotFemelle;
    }

    public void setCodeLotFemelle(String codeLotFemelle) {
        this.codeLotFemelle = codeLotFemelle;
    }

    public Long getLotMaleId() {
        return lotMaleId;
    }

    public void setLotMaleId(Long lotMaleId) {
        this.lotMaleId = lotMaleId;
    }

    public String getCodeLotMale() {
        return codeLotMale;
    }

    public void setCodeLotMale(String codeLotMale) {
        this.codeLotMale = codeLotMale;
    }

    public Integer getNombreFemellesConcernees() {
        return nombreFemellesConcernees;
    }

    public void setNombreFemellesConcernees(Integer nombreFemellesConcernees) {
        this.nombreFemellesConcernees = nombreFemellesConcernees;
    }

    public Integer getNombreMalesUtilises() {
        return nombreMalesUtilises;
    }

    public void setNombreMalesUtilises(Integer nombreMalesUtilises) {
        this.nombreMalesUtilises = nombreMalesUtilises;
    }

    public LocalDate getDateSaillie() {
        return dateSaillie;
    }

    public void setDateSaillie(LocalDate dateSaillie) {
        this.dateSaillie = dateSaillie;
    }

    public LocalDate getDatePrevueMiseBas() {
        return datePrevueMiseBas;
    }

    public void setDatePrevueMiseBas(LocalDate datePrevueMiseBas) {
        this.datePrevueMiseBas = datePrevueMiseBas;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

   
}