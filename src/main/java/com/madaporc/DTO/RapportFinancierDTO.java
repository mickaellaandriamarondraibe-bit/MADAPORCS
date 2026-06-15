package com.madaporc.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RapportFinancierDTO {

    private BigDecimal totalVentes;
    private BigDecimal totalDepenses;
    private BigDecimal beneficeNet;

    private long nombreVentes;
    private long nombreDepenses;
}   