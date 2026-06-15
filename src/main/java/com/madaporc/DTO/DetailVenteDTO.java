package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DetailVenteDTO {
    private Long lotPorcId;

private Integer nombrePorcsVendus;

private BigDecimal poidsTotalKg;

private BigDecimal prixKg;

private BigDecimal prixUnitaire;
}
