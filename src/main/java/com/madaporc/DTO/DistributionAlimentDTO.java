package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class DistributionAlimentDTO {
        private Long id;

    private Long lotPorcId;

    private Long melangeId;

    private LocalDate dateDistribution;

    private BigDecimal quantiteKg;

    private BigDecimal coutTotal;

    private String observation;
    }
