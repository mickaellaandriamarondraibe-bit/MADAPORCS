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
public class PeseeLotDTO {
    private Long id;
    private Long lotPorcId;
    private BigDecimal poidsMoyenKg;
    private LocalDate datePesee;
    private String observation;
    private Long createdBy;
    private LocalDate createdAt;
}
