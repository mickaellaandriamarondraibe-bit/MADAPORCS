package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "lots_porcs")
public class LotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_lot", nullable = false, unique = true)
    private String codeLot;

    @Column(name = "date_creation")
    private LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(name = "sexe", nullable = false)
    private String sexe;

    @Column(name = "objectif", nullable = false)
    private String objectif;

    @Column(name = "origine", nullable = false)
    private String origine;

    // Âge du lot en mois, saisi à l'achat (origine ACHAT).
    // Sert à classer les femelles pour l'analyse reproductive.
    @Column(name = "age_mois")
    private Integer ageMois;

    // Prix d'achat unitaire (par animal) pour un lot d'origine ACHAT.
    // Sert a (re)calculer la depense d'achat du lot.
    @Column(name = "prix_achat")
    private BigDecimal prixAchat;

    @Column(name = "effectif_initial", nullable = false)
    private Integer effectifInitial;

    @Column(name = "effectif_actuel", nullable = false)
    private Integer effectifActuel;

    @Column(name = "statut", nullable = false)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "lot_parent_id")
    private LotPorc lotParent;

    @ManyToOne
    @JoinColumn(name = "groupe_reproduction_origine_id")
    private GroupeReproduction groupeReproductionOrigine;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

        @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}