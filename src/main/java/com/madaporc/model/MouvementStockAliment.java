package com.madaporc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "mouvements_stock_aliment")
@Getter
@Setter
public class MouvementStockAliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @ManyToOne
    @JoinColumn(name = "type_mouvement_stock_id", nullable = false)
    private TypeMouvementStock typeMouvementStock;

    @Column(name = "quantite_kg", nullable = false)
    private BigDecimal quantiteKg;

    @Column(name = "prix_total")
    private BigDecimal prixTotal;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(length = 255)
    private String motif;
}