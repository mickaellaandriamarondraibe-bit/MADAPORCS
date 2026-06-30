package com.madaporc.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "details_vente")
public class DetailVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vente_id", nullable = false)
    private Vente vente;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "poids_total", precision = 12, scale = 2)
    private BigDecimal poidsTotal;

    @Column(name = "prix_unitaire", precision = 12, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(precision = 12, scale = 2)
    private BigDecimal montant;

    public DetailVente() {
    }

    public Long getId() {
        return id;
    }

    public Vente getVente() {
        return vente;
    }

    public void setVente(Vente vente) {
        this.vente = vente;
    }

    public LotPorc getLot() {
        return lot;
    }

    public void setLot(LotPorc lot) {
        this.lot = lot;
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