package com.madaporc.dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
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

   
}