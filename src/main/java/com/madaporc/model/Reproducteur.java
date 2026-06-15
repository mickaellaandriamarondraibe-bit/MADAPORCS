package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "reproducteurs")
public class Reproducteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_reproducteur")
    private String codeReproducteur;

    @Column(name = "nom")
    private String nom;

    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "sexe_id")
    private Long sexeId;

    @Column(name = "statut_reproducteur_id")
    private Long statutReproducteurId;

    @Column(name = "date_naissance")
    private LocalDate dateNaissance;

    @Column(name = "date_arrivee")
    private LocalDate dateArrivee;

    @Column(name = "prix_achat")
    private BigDecimal prixAchat;

    @Column(name = "poids_kg")
    private BigDecimal poidsKg;

    @Column(name = "nombre_parite")
    private Integer nombreParite;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;
}
