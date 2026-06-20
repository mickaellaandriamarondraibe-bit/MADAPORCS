package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "lots_porcs")
public class LotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_lot", nullable = false, unique = true, length = 50)
    private String codeLot;

    @Column(name = "date_creation", nullable = false)
    private LocalDate dateCreation;

    @ManyToOne
    @JoinColumn(name = "race_id")
    private Race race;

    @Column(nullable = false, length = 10)
    private String sexe;

    @Column(nullable = false, length = 30)
    private String objectif;

    @Column(nullable = false, length = 30)
    private String origine;

    @Column(name = "effectif_initial", nullable = false)
    private Integer effectifInitial;

    @Column(name = "effectif_actuel", nullable = false)
    private Integer effectifActuel;

    @Column(nullable = false, length = 30)
    private String statut;

    @ManyToOne
    @JoinColumn(name = "lot_parent_id")
    private LotPorc lotParent;

    @Column(name = "groupe_reproduction_origine_id")
    private Long groupeReproductionOrigineId;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public LotPorc() {
    }

    public Long getId() {
        return id;
    }

    public String getCodeLot() {
        return codeLot;
    }

    public void setCodeLot(String codeLot) {
        this.codeLot = codeLot;
    }

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getObjectif() {
        return objectif;
    }

    public void setObjectif(String objectif) {
        this.objectif = objectif;
    }

    public String getOrigine() {
        return origine;
    }

    public void setOrigine(String origine) {
        this.origine = origine;
    }

    public Integer getEffectifInitial() {
        return effectifInitial;
    }

    public void setEffectifInitial(Integer effectifInitial) {
        this.effectifInitial = effectifInitial;
    }

    public Integer getEffectifActuel() {
        return effectifActuel;
    }

    public void setEffectifActuel(Integer effectifActuel) {
        this.effectifActuel = effectifActuel;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public LotPorc getLotParent() {
        return lotParent;
    }

    public void setLotParent(LotPorc lotParent) {
        this.lotParent = lotParent;
    }

    public Long getGroupeReproductionOrigineId() {
        return groupeReproductionOrigineId;
    }

    public void setGroupeReproductionOrigineId(Long groupeReproductionOrigineId) {
        this.groupeReproductionOrigineId = groupeReproductionOrigineId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}