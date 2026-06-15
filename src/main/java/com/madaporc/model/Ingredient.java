package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "ingredients")
public class Ingredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "prix_kg")
    private BigDecimal prixKg;

    @Column(name = "stock_actuel_kg")
    private BigDecimal stockActuelKg;

    @Column(name = "seuil_min_kg")
    private BigDecimal seuilMinKg;

    @Column(name = "unite")
    private String unite;

    @Column(name = "actif")
    private Boolean actif;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
