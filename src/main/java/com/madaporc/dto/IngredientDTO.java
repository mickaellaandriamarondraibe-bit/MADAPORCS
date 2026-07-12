package com.madaporc.dto;

import java.math.BigDecimal;

public class IngredientDTO {
    private Long id;
    private String nom;
    private String unite;
    private BigDecimal stockActuel;
    private BigDecimal seuilAlerte;

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getUnite() {
        return unite;
    }

    public BigDecimal getStockActuel() {
        return stockActuel;
    }

    public BigDecimal getSeuilAlerte() {
        return seuilAlerte;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public void setStockActuel(BigDecimal stockActuel) {
        this.stockActuel = stockActuel;
    }

    public void setSeuilAlerte(BigDecimal seuilAlerte) {
        this.seuilAlerte = seuilAlerte;
    }
}