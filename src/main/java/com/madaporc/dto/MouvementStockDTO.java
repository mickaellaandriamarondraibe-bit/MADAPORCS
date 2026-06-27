package com.madaporc.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MouvementStockDTO {
    private Long id;
    private Long ingredientId;
    private String typeMouvement;
    private BigDecimal quantite;
    private LocalDateTime dateMouvement;
    private String observation;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public LocalDateTime getDateMouvement() {
        return dateMouvement;
    }
    public void setDateMouvement(LocalDateTime dateMouvement) {
        this.dateMouvement = dateMouvement;
    }
    public String getObservation() {
        return observation;
    }
    public void setObservation(String observation) {
        this.observation = observation;
    }

    public MouvementStockDTO() {
    }
}
