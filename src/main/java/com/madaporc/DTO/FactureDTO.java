package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class FactureDTO {

    private Long id;
    private Long venteId;
    private String numeroFacture;
    private LocalDateTime dateFacture;
    private BigDecimal montantTotal;
    private BigDecimal montantPaye;
    private BigDecimal resteAPayer;
}