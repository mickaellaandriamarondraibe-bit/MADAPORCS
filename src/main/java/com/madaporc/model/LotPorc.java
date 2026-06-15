package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "lots_porcs")
public class LotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_lot")
    private String codeLot;

    @Column(name = "type_entree")
    private String typeEntree;

    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "statut_lot_id")
    private Long statutLotId;

    @Column(name = "nombre_initial")
    private Integer nombreInitial;

    @Column(name = "nombre_actuel")
    private Integer nombreActuel;

    @Column(name = "nombre_males_initial")
    private Integer nombreMalesInitial;

    @Column(name = "nombre_femelles_initial")
    private Integer nombreFemellesInitial;

    @Column(name = "nombre_males_actuel")
    private Integer nombreMalesActuel;

    @Column(name = "nombre_femelles_actuel")
    private Integer nombreFemellesActuel;

    @Column(name = "nombre_morts")
    private Integer nombreMorts;

    @Column(name = "date_naissance_estimee")
    private LocalDate dateNaissanceEstimee;

    @Column(name = "date_achat")
    private LocalDate dateAchat;

    @Column(name = "prix_achat_total")
    private BigDecimal prixAchatTotal;

    @Column(name = "poids_moyen_initial_kg")
    private BigDecimal poidsMoyenInitialKg;

    @Column(name = "poids_moyen_actuel_kg")
    private BigDecimal poidsMoyenActuelKg;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "archived_at")
    private LocalDateTime archivedAt;
}
