package com.madaporc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.Generated;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "groupes_reproduction")
public class GroupeReproduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_groupe")
    private String codeGroupe;

    @ManyToOne
    @JoinColumn(name = "lot_femelle_id")
    private LotPorc lotFemelle;

    @ManyToOne
    @JoinColumn(name = "lot_male_id")
    private LotPorc lotMale;

    @Column(name = "nombre_femelles_concernees")
    private Integer nombreFemellesConcernees;

    @Column(name = "nombre_males_utilises")
    private Integer nombreMalesUtilises;

    @Column(name = "date_saillie")
    private LocalDate dateSaillie;

    @Column(name = "duree_gestation_jours")
    private Integer dureeGestationJours;

    @Column(name = "date_prevue_mise_bas", insertable = false, updatable = false)
    private LocalDate datePrevueMiseBas;

    @Column(name = "date_mise_bas_reelle")
    private LocalDate dateMiseBasReelle;

    @Column(name = "nb_femelles_gestantes")
    private Integer nbFemellesGestantes;

    @Column(name = "nb_femelles_non_gestantes")
    private Integer nbFemellesNonGestantes;

    @Column(name = "nb_femelles_mise_bas")
    private Integer nbFemellesMiseBas;

    @Column(name = "nb_porcelets_nes")
    private Integer nbPorceletsNes;

    @Column(name = "nb_porcelets_vivants")
    private Integer nbPorceletsVivants;

    @Column(name = "nb_porcelets_morts")
    private Integer nbPorceletsMorts;

    @Column(name = "statut")
    private String statut;
    
    @Column(name = "observation")
    private String observation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}