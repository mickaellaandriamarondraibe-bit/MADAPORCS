package model;

import java.math.BigDecimal;

import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

import com.madaporc.model.LotPorc;

@Entity
@Table(name="analyses_reproduction_lots")
public class AnalyseReproductionLot {

    @Id
    private BigDecimal id;

    @Column(name="lot_id")
    @ManyToOne
    @JoinColumn(name="id")
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

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public LotPorc getLotPorc() {
        return lotPorc;
    }

    public void setLotPorc(LotPorc lotPorc) {
        this.lotPorc = lotPorc;
    }

    public LocalDateTime getDateAnalyse() {
        return dateAnalyse;
    }

    public void setDateAnalyse(LocalDateTime dateAnalyse) {
        this.dateAnalyse = dateAnalyse;
    }

    public Integer getNbPretesJamaisSaillies() {
        return nbPretesJamaisSaillies;
    }

    public void setNbPretesJamaisSaillies(Integer nbPretesJamaisSaillies) {
        this.nbPretesJamaisSaillies = nbPretesJamaisSaillies;
    }

    public Integer getNbDejaReproductricesAptes() {
        return nbDejaReproductricesAptes;
    }

    public void setNbDejaReproductricesAptes(Integer nbDejaReproductricesAptes) {
        this.nbDejaReproductricesAptes = nbDejaReproductricesAptes;
    }

    public Integer getNbEnCycle() {
        return nbEnCycle;
    }

    public void setNbEnCycle(Integer nbEnCycle) {
        this.nbEnCycle = nbEnCycle;
    }

    public Integer getNbASurveiller() {
        return nbASurveiller;
    }

    public void setNbASurveiller(Integer nbASurveiller) {
        this.nbASurveiller = nbASurveiller;
    }

    public Integer getNbARetirerReproduction() {
        return nbARetirerReproduction;
    }

    public void setNbARetirerReproduction(Integer nbARetirerReproduction) {
        this.nbARetirerReproduction = nbARetirerReproduction;
    }

    public Integer getNbFemellesTotal() {
        return nbFemellesTotal;
    }

    public void setNbFemellesTotal(Integer nbFemellesTotal) {
        this.nbFemellesTotal = nbFemellesTotal;
    }

    public Integer getNbFemellesSailliesTotal() {
        return nbFemellesSailliesTotal;
    }

    public void setNbFemellesSailliesTotal(Integer nbFemellesSailliesTotal) {
        this.nbFemellesSailliesTotal = nbFemellesSailliesTotal;
    }

    public Integer getNbFemellesGestantesTotal() {
        return nbFemellesGestantesTotal;
    }

    public void setNbFemellesGestantesTotal(Integer nbFemellesGestantesTotal) {
        this.nbFemellesGestantesTotal = nbFemellesGestantesTotal;
    }

    public Double getTauxAptitudeGlobal() {
        return tauxAptitudeGlobal;
    }

    public void setTauxAptitudeGlobal(Double tauxAptitudeGlobal) {
        this.tauxAptitudeGlobal = tauxAptitudeGlobal;
    }

    public Double getTauxRecommande() {
        return tauxRecommande;
    }

    public void setTauxRecommande(Double tauxRecommande) {
        this.tauxRecommande = tauxRecommande;
    }

    public Double getTauxFertiliteObserve() {
        return tauxFertiliteObserve;
    }

    public void setTauxFertiliteObserve(Double tauxFertiliteObserve) {
        this.tauxFertiliteObserve = tauxFertiliteObserve;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}