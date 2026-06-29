package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité JPA pour la gestion des traitements appliqués aux animaux.
 */
@Entity
@Table(name = "traitements")
@Getter
@Setter
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "maladie_id", foreignKey = @ForeignKey(name = "fk_traitement_maladie"))
    private Maladie maladie;

    @Column(length = 100)
    private String principe;

    @Column(name = "dosage_ml")
    private BigDecimal dosageMl;

    @Column(name = "frequence_jours")
    private Integer frequenceJours;

    @Column(name = "prix_unite", precision = 10, scale = 2)
    private BigDecimal prixUnite;

    @Column(name = "nombre_jours_traitement")
    private Integer nombreJoursTraitement;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean actif = true;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();
}
