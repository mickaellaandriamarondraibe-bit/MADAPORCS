package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class IngredientDTO {
    private Long id;

private String libelle;

private BigDecimal prixKg;

private BigDecimal stockActuelKg;

private BigDecimal seuilMinKg;

private String unite;

private Boolean actif;
}
