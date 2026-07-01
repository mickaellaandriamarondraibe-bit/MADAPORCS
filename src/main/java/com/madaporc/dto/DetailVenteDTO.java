package com.madaporc.dto;

import java.math.BigDecimal;

public class DetailVenteDTO {

    private Long id;

    private Long venteId;

    private Long lotId;

    private String codeLot;

    private Integer quantite;

    private BigDecimal poidsTotal;

    private BigDecimal prixUnitaire;

    private BigDecimal montant;

    public DetailVenteDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVenteId() {
        return venteId;
    }

    public void setVenteId(Long venteId) {
        this.venteId = venteId;
    }

    public Long getLotId() {
        return lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    public String getCodeLot() {
        return codeLot;
    }

    public void setCodeLot(String codeLot) {
        this.codeLot = codeLot;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPoidsTotal() {
        return poidsTotal;
    }

    public void setPoidsTotal(BigDecimal poidsTotal) {
        this.poidsTotal = poidsTotal;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getMontant() {
        return montant;
    }

    public void setMontant(BigDecimal montant) {
        this.montant = montant;
    }
}