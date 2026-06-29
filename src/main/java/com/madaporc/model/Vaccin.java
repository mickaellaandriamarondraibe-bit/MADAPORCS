package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité JPA pour la gestion des vaccins.
 */
@Entity
@Table(name = "vaccins")
@Getter
@Setter
public class Vaccin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String libelle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String fabricant;

    @Column(name = "prix_dose", precision = 10, scale = 2)
    private BigDecimal prixDose;

    @Column(name = "delai_rappel_jours")
    private Integer delaiRappelJours;

    @Column(name = "age_minimum_jours")
    private Integer ageMinimumJours;

    @Column(name = "age_maximum_jours")
    private Integer ageMaximumJours;

    @Column(name = "temperature_stockage_min")
    private Double temperatureStockageMin;

    @Column(name = "temperature_stockage_max")
    private Double temperatureStockageMax;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean actif = true;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();
}
