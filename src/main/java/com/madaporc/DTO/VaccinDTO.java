package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class VaccinDTO {
    private Long id;
    private String libelle;
    private BigDecimal prix;
    private Integer delaiRappelJours;
    private String description;
    private Boolean actif;
}
