package com.madaporc.dto;

import java.math.BigDecimal;

public class MouvementStockDTO {
    private Long ingredientId;
    private String typeMouvement;
    private BigDecimal quantite;

    // Montant dépensé pour une entrée en stock (achat d'ingrédient).
    private BigDecimal montant;

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public BigDecimal getQuantite() {
        return quantite;
    }

    public void setQuantite(BigDecimal quantite) {
        this.quantite = quantite;
    }
}
