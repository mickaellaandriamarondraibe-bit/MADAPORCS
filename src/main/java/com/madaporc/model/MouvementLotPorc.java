package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "mouvements_lots_porcs")
public class MouvementLotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @Column(name = "type_mouvement", nullable = false, length = 50)
    private String typeMouvement;

    @Column(nullable = false)
    private Integer quantite;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDate dateMouvement;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public MouvementLotPorc() {
    }

    public Long getId() {
        return id;
    }

    public LotPorc getLot() {
        return lot;
    }

    public void setLot(LotPorc lot) {
        this.lot = lot;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeMouvement() {
        return typeMouvement;
    }

    public void setTypeMouvement(String typeMouvement) {
        this.typeMouvement = typeMouvement;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDate getDateMouvement() {
        return dateMouvement;
    }

    public void setDateMouvement(LocalDate dateMouvement) {
        this.dateMouvement = dateMouvement;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}