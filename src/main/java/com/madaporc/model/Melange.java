package com.madaporc.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "melanges")
@Getter
@Setter
public class Melange {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="libelle", nullable = false, unique = true, length = 150)
    private String libelle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "cout_kg", precision = 12, scale = 2)
    private BigDecimal coutRevientKg;

    @Column(name = "statut", length = 50)
    private String statut;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "melange", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MelangeIngredient> ingredients = new ArrayList<>();
    
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}