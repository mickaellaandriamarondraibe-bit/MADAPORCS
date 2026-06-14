package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class MelangeIngredientDTO {
    private Long ingredientId;
    private BigDecimal quantiteKg;
    private BigDecimal pourcentage;
}
