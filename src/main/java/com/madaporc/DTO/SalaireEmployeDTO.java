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
public class SalaireEmployeDTO {
    private Long id;
    private Long employeId;
    private Integer mois;
    private Integer annee;
    private BigDecimal montantBase;
    private BigDecimal prime;
    private BigDecimal retenue;
    private BigDecimal montantNet;
    private LocalDate datePaiement;
    private String statutPaiement;
}
