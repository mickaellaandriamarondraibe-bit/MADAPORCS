package com.madaporc.DTO;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la gestion des vaccinations.
 */
@Getter
@Setter
public class VaccinationDTO {
    private Long id;
    private Long vaccinId;
    private Long lotId;
    private Long reproducteurId;
    private LocalDate dateVaccination;
    private LocalDate dateRappelPrevue;
    private LocalDate dateRappelEffectuee;
    private Integer numeroDose;
    private String veterinaire;
    private String notes;
    private String statutRappel;
    private Boolean actif;
}
