package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MelangeIngredientDTO {
    private Long ingredientId;
    private BigDecimal quantiteKg;
    private BigDecimal pourcentage;
}