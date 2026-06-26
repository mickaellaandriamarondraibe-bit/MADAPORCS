package com.madaporc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "alertes_reproduction")
public class AlerteReproduction {

    public enum StatutAlerte {
        NON_LUE,
        LUE,
        TRAITEE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groupe_reproduction_id")
    private GroupeReproduction groupeReproduction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id")
    private LotPorc lot;

    @Column(name = "type_alerte", nullable = false, length = 50)
    private String typeAlerte;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "date_alerte", nullable = false)
    private LocalDate dateAlerte;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 30)
    private StatutAlerte statut;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (dateAlerte == null) {
            dateAlerte = LocalDate.now();
        }
        if (statut == null) {
            statut = StatutAlerte.NON_LUE;
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GroupeReproduction getGroupeReproduction() {
        return groupeReproduction;
    }

    public void setGroupeReproduction(GroupeReproduction groupeReproduction) {
        this.groupeReproduction = groupeReproduction;
    }

    public LotPorc getLot() {
        return lot;
    }

    public void setLot(LotPorc lot) {
        this.lot = lot;
    }

    public String getTypeAlerte() {
        return typeAlerte;
    }

    public void setTypeAlerte(String typeAlerte) {
        this.typeAlerte = typeAlerte;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDate getDateAlerte() {
        return dateAlerte;
    }

    public void setDateAlerte(LocalDate dateAlerte) {
        this.dateAlerte = dateAlerte;
    }

    public StatutAlerte getStatut() {
        return statut;
    }

    public void setStatut(StatutAlerte statut) {
        this.statut = statut;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
