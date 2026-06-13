package com.madaporc.model;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "melange_ingredients")
@Getter
@Setter
public class MelangeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "melange_id", nullable = false)
    private Melange melange;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "quantite_kg", precision = 12, scale = 2, nullable = false)
    private BigDecimal quantiteKg;

    @Column(name = "pourcentage", precision = 5, scale = 2, nullable = false)
    private BigDecimal pourcentage;
}