package com.madaporc.DTO;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class RapportStockDTO {

    private long nombreIngredients;
    private BigDecimal valeurStock;
    private long stocksFaibles;
}