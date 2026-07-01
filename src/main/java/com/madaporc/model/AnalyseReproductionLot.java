package com.madaporc.model;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name="analyses_reproduction_lots")
public class AnalyseReproductionLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lotPorc;

    @Column(name="date_analyse")
    private LocalDateTime dateAnalyse;

    @Column(name="nb_pretes_jamais_saillies")
    private Integer nbPretesJamaisSaillies;

    @Column(name="nb_deja_reproductrices_aptes")
    private Integer nbDejaReproductricesAptes;

    @Column(name="nb_en_cycle")
    private Integer nbEnCycle;

    @Column(name="nb_a_surveiller")
    private Integer nbASurveiller;

    @Column(name="nb_a_retier_reproduction")
    private Integer nbARetirerReproduction;

    @Column(name="nb_femelles_total")
    private Integer nbFemellesTotal;

    @Column(name="nb_femelles_saillies_total")
    private Integer nbFemellesSailliesTotal;

    @Column(name="nb_femelles_gestantes_total")
    private Integer nbFemellesGestantesTotal;

    @Column(name="taux_aptitude_global")
    private Double tauxAptitudeGlobal;

    @Column(name="taux_recommande")
    private Double tauxRecommande;

    @Column(name="taux_fertilite_observe")
    private Double tauxFertiliteObserve;

    private String decision;

    private String commentaire;

    public AnalyseReproductionLot() {
    }

   
}