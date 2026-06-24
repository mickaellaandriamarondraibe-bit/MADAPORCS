package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "suivis_sanitaires")
public class SuiviSanitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maladie_id")
    private Maladie maladie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traitement_id")
    private Traitement traitement;

    @Column(name = "date_diagnostic", nullable = false)
    private LocalDate dateDiagnostic;

    @Column(name = "date_traitement")
    private LocalDate dateTraitement;

    @Column(name = "date_guerison")
    private LocalDate dateGuerison;

    @Column(name = "observation")
    private String observation;

    // champs affichage JSP
    @Transient
    public Long getLotId() {
        return lot != null ? lot.getId() : null;
    }

    @Transient
    public String getCodeLot() {
        return lot != null ? lot.getCodeLot() : null;
    }

    @Transient
    public String getMaladie() {
        return maladie != null ? maladie.getNom() : null;
    }

    @Transient
    public String getTraitement() {
        return traitement != null ? traitement.getNom() : null;
    }

    public String getStatut() {
        return dateGuerison == null ? "En cours" : "Guéri";
    }
}

