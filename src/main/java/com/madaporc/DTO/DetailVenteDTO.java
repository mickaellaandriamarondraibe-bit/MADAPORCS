package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class DetailVenteDTO {
    private Long lotPorcId;
    private Integer nombrePorcsVendus;
    private BigDecimal poidsTotalKg;
    private BigDecimal prixKg;
    private BigDecimal prixUnitaire;
}
