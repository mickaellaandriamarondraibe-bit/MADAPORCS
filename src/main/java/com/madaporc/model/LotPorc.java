package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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
}