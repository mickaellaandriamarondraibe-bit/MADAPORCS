package com.madaporc.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "alertes_reproduction")
public class AlerteReproduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @ManyToOne
    @JoinColumn(name = "groupe_reproduction_id")
    private GroupeReproduction groupeReproduction;

    @ManyToOne
    @JoinColumn(name = "lot_id")
    private Lot lot;
    
    @Column(name = "type_alerte")
    private String typeAlerte;

    @Column(name = "message")
    private String message;

    @Column(name = "date_alerte")
    private LocalDateTime date_alerte;

    @Column(name = "statut")
    private String statut;

    public AlerteReproduction() {
    }

    public AlerteReproduction(GroupeReproduction groupeReproduction, Lot lot, String typeAlerte, String message,
        LocalDateTime date_alerte, String statut) {
        this.groupeReproduction = groupeReproduction;
        this.lot = lot;
        this.typeAlerte = typeAlerte;
        this.message = message;
        this.date_alerte = date_alerte;
        this.statut = statut;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GroupeReproduction getGroupeReproduction() {
        return groupeReproduction;
    }

    public void setGroupeReproduction(GroupeReproduction groupeReproduction) {
        this.groupeReproduction = groupeReproduction;
    }

    public Lot getLot() {
        return lot;
    }

    public void setLot(Lot lot) {
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

    public LocalDateTime getDate_alerte() {
        return date_alerte;
    }

    public void setDate_alerte(LocalDateTime date_alerte) {
        this.date_alerte = date_alerte;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
}