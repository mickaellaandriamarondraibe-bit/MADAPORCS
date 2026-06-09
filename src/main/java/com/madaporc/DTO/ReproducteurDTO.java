package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class ReproducteurDTO {
    private Long id;
    private String codeReproducteur;
    private String nom;
    private Long raceId;
    private Long sexeId;
    private Long statutReproducteurId;
    private LocalDate dateNaissance;
    private LocalDate dateArrivee;
    private BigDecimal prixAchat;
    private BigDecimal poidsKg;
    private String observation;
}
