package com.madaporc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RapportFinancierDTO {

    private BigDecimal totalVentes = BigDecimal.ZERO;
    private BigDecimal totalDepenses = BigDecimal.ZERO;
    private BigDecimal beneficeNet = BigDecimal.ZERO;

    private Long nombreVentes = 0L;
    private Long nombreDepenses = 0L;

    private LocalDate dateDebut;
    private LocalDate dateFin;
}
