package com.madaporc.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "traitements")
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "maladie_id")
    private Long maladieId;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    @Column(name = "prix")
    private BigDecimal prix;

    @Column(name = "duree_guerison_jours")
    private Integer dureeGuerisonJours;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "actif")
    private Boolean actif = true;
}