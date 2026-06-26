package com.madaporc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SuiviSanitaireDTO {

    private Long id;

    private Long lotId;

    private Long maladieId;

    private Long traitementId;

    private LocalDate dateDiagnostic;

    private LocalDate dateTraitement;

    private LocalDate dateGuerison;

    private String observation;
}