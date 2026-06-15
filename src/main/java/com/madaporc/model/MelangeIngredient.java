package com.madaporc.model;

    import jakarta.persistence.*;
import java.math.BigDecimal;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "melange_ingredients")
    public class MelangeIngredient {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "melange_id")
    private Long melangeId;

    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "quantite_kg")
    private BigDecimal quantiteKg;

    @Column(name = "pourcentage")
    private BigDecimal pourcentage;
    }
