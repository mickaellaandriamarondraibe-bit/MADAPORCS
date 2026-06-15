package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "melanges")
public class Melange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "description")
    private String description;

    @Column(name = "cout_kg")
    private BigDecimal coutKg;

    @Column(name = "statut")
    private String statut;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
