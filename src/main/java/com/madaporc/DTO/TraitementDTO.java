package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TraitementDTO{
    private Long id;
    private Long maladieId;
    private String libelle;
    private BigDecimal prix;
    private Integer dureeGuerisonJours;
    private String description;
}