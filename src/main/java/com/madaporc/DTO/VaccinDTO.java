package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la gestion des vaccins.
 */
@Getter
@Setter
public class VaccinDTO {
    private Long id;
    private String libelle;
    private String description;
    private String fabricant;
    private BigDecimal prixDose;
    private Integer delaiRappelJours;
    private Integer ageMinimumJours;
    private Integer ageMaximumJours;
    private Double temperatureStockageMin;
    private Double temperatureStockageMax;
    private Boolean actif;
}
