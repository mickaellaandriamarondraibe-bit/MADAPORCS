package com.madaporc.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class EvenementReproductionDTO {
    private Long id;
    private Long femelleId;
    private Long maleId;
    private Long typeEvenementReproductionId;
    private Long lotPorcId;
    private LocalDate dateEvenement;
    private Integer nombrePorceletsNes;
    private Integer nombrePorceletsMorts;
    private Integer nombrePorceletsVivants;
    private String observation;
}
