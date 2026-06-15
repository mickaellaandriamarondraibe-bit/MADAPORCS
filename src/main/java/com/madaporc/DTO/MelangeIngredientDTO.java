package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class MelangeIngredientDTO {
    private Long id;

private Long melangeId;

private Long ingredientId;

private BigDecimal quantiteKg;

private BigDecimal pourcentage;
}
