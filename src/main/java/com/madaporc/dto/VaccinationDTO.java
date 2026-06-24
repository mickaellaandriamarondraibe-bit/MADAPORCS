package com.madaporc.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VaccinationDTO {

    private Long id;

    private Long lotId;

    private Long vaccinId;

    private LocalDate dateVaccination;

    private LocalDate dateRappel;

    private String observation;
}

