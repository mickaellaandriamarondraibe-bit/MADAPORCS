package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO pour la gestion des traitements.
 */
@Getter
@Setter
public class TraitementDTO {
    private Long id;
    private String libelle;
    private String description;
    private Long maladieId;
    private String principe;
    private BigDecimal dosageMl;
    private Integer frequenceJours;
    private BigDecimal prixUnite;
    private Integer nombreJoursTraitement;
    private Boolean actif;
}
