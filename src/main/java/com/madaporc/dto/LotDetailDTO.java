package com.madaporc.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LotDetailDTO {
    private Long id;
    private String codeLot;
    private LocalDate dateCreation;
    private Long raceId;
    private String raceNom;
    private String sexe;
    private String objectif;
    private String origine;
    private Integer effectifInitial;
    private Integer effectifActuel;
    private String statut;
    private Long lotParentId;
    private String codeLotParent;
    private Long groupeReproductionOrigineId;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LotDetailDTO() {
    }
}