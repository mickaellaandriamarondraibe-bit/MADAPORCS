package com.madaporc.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotPorcDTO {
    private Long id;
    private String codeLot;
    private LocalDate dateCreation;
    private Long raceId;
    private String sexe;
    private String objectif;
    private String origine;
    private Integer ageMois;
    private Integer effectifInitial;
    private Integer effectifActuel;
    private String statut;
    private Long lotParentId;
    private Long groupeReproductionOrigineId;
    private String description;

}