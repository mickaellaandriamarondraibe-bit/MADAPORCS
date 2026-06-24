package com.madaporc.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VaccinDTO {

    private Long id;

    private String nom;

    private String maladieCiblee;

    private String voie;

    private Integer delaiRappel;

    private String description;
}

