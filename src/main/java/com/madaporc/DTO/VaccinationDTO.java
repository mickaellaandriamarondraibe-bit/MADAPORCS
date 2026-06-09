package com.madaporc.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class VaccinationDTO {
    private Long id;
    private Long lotPorcId;
    private Long reproducteurId;
    private Long vaccinId;
    private LocalDate dateVaccination;
    private LocalDate dateRappel;
    private String dose;
    private String observation;
}
