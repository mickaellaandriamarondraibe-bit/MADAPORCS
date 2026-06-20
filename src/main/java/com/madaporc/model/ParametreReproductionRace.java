package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "parametres_reproduction_race")
public class ParametreReproductionRace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(name = "age_min_reproduction_mois")
    private Integer ageMinReproductionMois;

    @Column(name = "age_max_reproduction_mois")
    private Integer ageMaxReproductionMois;

    @Column(name = "nombre_max_portees")
    private Integer nombreMaxPortees;

    @Column(name = "seuil_fertilite_min")
    private Double seuilFertiliteMin;

    @Column(name = "seuil_survie_min")
    private Double seuilSurvieMin;

    @Column(name = "duree_gestation_jours")
    private Integer dureeGestationJours;

    @Column(name = "jours_alerte_mise_bas")
    private Integer joursAlerteMiseBas;
}