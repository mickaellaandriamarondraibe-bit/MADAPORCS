package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repartitions_reproductives_lots")
public class RepartitionReproductiveLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private LotPorc lot;

    @ManyToOne
    @JoinColumn(name = "statut_reproductif_id")
    private StatutReproductif statutReproductif;

    @Column(name = "quantite")
    private Integer quantite;

    public Long getId() {
        return id;
    }

    public LotPorc getLot() {
        return lot;
    }

    public void setLot(LotPorc lot) {
        this.lot = lot;
    }

    public StatutReproductif getStatutReproductif() {
        return statutReproductif;
    }

    public void setStatutReproductif(StatutReproductif statutReproductif) {
        this.statutReproductif = statutReproductif;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}