package com.madaporc.DTO;

public class RapportStockDTO {
    private Integer nombreIngredientsSousSeuil;

    public RapportStockDTO() {}

    public RapportStockDTO(Integer nombreIngredientsSousSeuil) {
        this.nombreIngredientsSousSeuil = nombreIngredientsSousSeuil;
    }

    // Getters et Setters
    public Integer getNombreIngredientsSousSeuil() { return nombreIngredientsSousSeuil; }
    public void setNombreIngredientsSousSeuil(Integer nombreIngredientsSousSeuil) { 
        this.nombreIngredientsSousSeuil = nombreIngredientsSousSeuil; 
    }
}