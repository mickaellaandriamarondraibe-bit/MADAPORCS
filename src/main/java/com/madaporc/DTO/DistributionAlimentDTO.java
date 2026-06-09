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
public class DistributionAlimentDTO {
    private Long id;
    private Long lotPorcId;
    private Long melangeId;
    private LocalDate dateDistribution;
    private BigDecimal quantiteKg;
    private String observation;
}
