package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class MouvementStockDTO {
    private Long id;
    private Long ingredientId;
    private Long typeMouvementStockId;
    private BigDecimal quantiteKg;
    private BigDecimal prixTotal;
    private LocalDateTime dateMouvement;
    private String motif;
}
