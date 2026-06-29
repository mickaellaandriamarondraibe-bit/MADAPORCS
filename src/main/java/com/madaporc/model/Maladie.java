package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité JPA pour la gestion des maladies chez les porcs.
 */
@Entity
@Table(name = "maladies")
@Getter
@Setter
public class Maladie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String symptomes;

    @Column(length = 255)
    private String traitement;

    @Column(name = "duree_traitement_jours")
    private Integer dureTraitementJours;

    @Column(name = "taux_mortalite_percent")
    private Double tauxMortalitePercent;

    @Column(name = "contagieux", columnDefinition = "BOOLEAN DEFAULT false")
    private Boolean contagieux = false;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean actif = true;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();
}
