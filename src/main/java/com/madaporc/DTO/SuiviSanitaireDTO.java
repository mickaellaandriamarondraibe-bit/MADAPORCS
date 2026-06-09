package com.madaporc.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class SuiviSanitaireDTO {
    private Long id;
    private Long lotPorcId;
    private Long reproducteurId;
    private Integer nombrePorcsMalades;
    private Long maladieId;
    private Long traitementId;
    private Long statutSuiviSanitaireId;
    private LocalDate dateDiagnostic;
    private LocalDate dateGuerisonPrevue;
    private LocalDate dateGuerisonReelle;
    private String symptomes;
    private String diagnostic;
    private String observation;
}
