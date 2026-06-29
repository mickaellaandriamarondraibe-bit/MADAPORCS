package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la gestion des maladies.
 */
@Getter
@Setter
public class MaladieDTO {
    private Long id;
    private String libelle;
    private String description;
    private String symptomes;
    private String traitement;
    private Integer dureTraitementJours;
    private Double tauxMortalitePercent;
    private Boolean contagieux;
    private Boolean actif;
}
