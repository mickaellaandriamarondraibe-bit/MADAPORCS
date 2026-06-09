package com.madaporc.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class CycleProductionDTO {
    private Long id;
    private String codeCycle;
    private Long lotPorcId;
    private LocalDate dateDebut;
    private LocalDate dateFinPrevue;
    private LocalDate dateFinReelle;
    private Integer nombreNaissances;
    private Integer nombrePertes;
    private Integer nombreVivants;
    private Integer nombreVendables;
    private String statutCycle;
    private String observation;
}
