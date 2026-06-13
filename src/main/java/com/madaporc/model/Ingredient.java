package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ingredients")
@Getter
@Setter
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String libelle;

    @Column(name = "prix_kg")
    private BigDecimal prixKg;

    @Column(name = "stock_actuel_kg")
    private BigDecimal stockActuelKg;

    @Column(name = "seuil_min_kg")
    private BigDecimal seuilMinKg;

    private String unite;

    private Boolean actif;
}