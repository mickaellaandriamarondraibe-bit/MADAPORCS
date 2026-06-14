package com.madaporc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_porcs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_lot", unique = true, nullable = false, length = 50)
    private String codeLot;

    @Column(name = "type_entree", length = 30)
    private String typeEntree;

    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "statut_lot_id")
    private Long statutLotId;

    @Column(name = "nombre_initial", nullable = false)
    private Integer nombreInitial;

    @Column(name = "nombre_actuel", nullable = false)
    private Integer nombreActuel;

    @Column(name = "nombre_males_initial")
    private Integer nombreMalesInitial;

    @Column(name = "nombre_femelles_initial")
    private Integer nombreFellesInitial;

    @Column(name = "nombre_males_actuel")
    private Integer nombreMalesActuel;

    @Column(name = "nombre_femelles_actuel")
    private Integer nombreFellesActuel;

    @Column(name = "nombre_morts", nullable = false)
    private Integer nombreMorts;

    @Column(name = "date_naissance_estimee")
    private LocalDate dateNaissanceEstimee;

    @Column(name = "date_achat")
    private LocalDate dateAchat;

    @Column(name = "prix_achat_total", precision = 12, scale = 2)
    private BigDecimal prixAchatTotal;

    @Column(name = "poids_moyen_initial_kg", precision = 10, scale = 2)
    private BigDecimal poidsMoyenInitialKg;

    @Column(name = "poids_moyen_actuel_kg", precision = 10, scale = 2)
    private BigDecimal poidsMoyenActuelKg;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;
}