package com.madaporc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class GroupeReproductionDetailDTO {

    private Long id;

    private String codeGroupe;

    private Long lotFemelleId;
    private String codeLotFemelle;

    private Long lotMaleId;
    private String codeLotMale;

    private Integer nombreFemellesConcernees;
    private Integer nombreMalesUtilises;

    private LocalDate dateSaillie;
    private Integer dureeGestationJours;

    private LocalDate datePrevueMiseBas;
    private LocalDate dateMiseBasReelle;

    private Integer nbFemellesGestantes;
    private Integer nbFemellesNonGestantes;
    private Integer nbFemellesMiseBas;

    private Integer nbPorceletsNes;
    private Integer nbPorceletsVivants;
    private Integer nbPorceletsMorts;

    private String statut;

    private String observation;

    private String createur;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public GroupeReproductionDetailDTO() {
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

    public Integer getDureeGestationJours() {
        return dureeGestationJours;
    }

    public void setDureeGestationJours(Integer dureeGestationJours) {
        this.dureeGestationJours = dureeGestationJours;
    }

    public LocalDate getDatePrevueMiseBas() {
        return datePrevueMiseBas;
    }

    public void setDatePrevueMiseBas(LocalDate datePrevueMiseBas) {
        this.datePrevueMiseBas = datePrevueMiseBas;
    }

    public LocalDate getDateMiseBasReelle() {
        return dateMiseBasReelle;
    }

    public void setDateMiseBasReelle(LocalDate dateMiseBasReelle) {
        this.dateMiseBasReelle = dateMiseBasReelle;
    }

    public Integer getNbFemellesGestantes() {
        return nbFemellesGestantes;
    }

    public void setNbFemellesGestantes(Integer nbFemellesGestantes) {
        this.nbFemellesGestantes = nbFemellesGestantes;
    }

    public Integer getNbFemellesNonGestantes() {
        return nbFemellesNonGestantes;
    }

    public void setNbFemellesNonGestantes(Integer nbFemellesNonGestantes) {
        this.nbFemellesNonGestantes = nbFemellesNonGestantes;
    }

    public Integer getNbFemellesMiseBas() {
        return nbFemellesMiseBas;
    }

    public void setNbFemellesMiseBas(Integer nbFemellesMiseBas) {
        this.nbFemellesMiseBas = nbFemellesMiseBas;
    }

    public Integer getNbPorceletsNes() {
        return nbPorceletsNes;
    }

    public void setNbPorceletsNes(Integer nbPorceletsNes) {
        this.nbPorceletsNes = nbPorceletsNes;
    }

    public Integer getNbPorceletsVivants() {
        return nbPorceletsVivants;
    }

    public void setNbPorceletsVivants(Integer nbPorceletsVivants) {
        this.nbPorceletsVivants = nbPorceletsVivants;
    }

    public Integer getNbPorceletsMorts() {
        return nbPorceletsMorts;
    }

    public void setNbPorceletsMorts(Integer nbPorceletsMorts) {
        this.nbPorceletsMorts = nbPorceletsMorts;
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

    public String getCreateur() {
        return createur;
    }

    public void setCreateur(String createur) {
        this.createur = createur;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}