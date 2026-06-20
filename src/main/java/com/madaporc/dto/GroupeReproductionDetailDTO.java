package com.madaporc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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

    
}