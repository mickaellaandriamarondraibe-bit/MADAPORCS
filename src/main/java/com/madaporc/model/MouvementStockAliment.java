package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "mouvements_stock_aliment")
public class MouvementStockAliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "type_mouvement_stock_id")
    private Long typeMouvementStockId;

    @Column(name = "quantite_kg")
    private BigDecimal quantiteKg;

    @Column(name = "prix_total")
    private BigDecimal prixTotal;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "motif")
    private String motif;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
