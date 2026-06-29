package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité JPA pour la gestion des vaccinations appliquées aux animaux.
 */
@Entity
@Table(name = "vaccinations")
@Getter
@Setter
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vaccin_id", nullable = false, foreignKey = @ForeignKey(name = "fk_vaccination_vaccin"))
    private Vaccin vaccin;

    @ManyToOne
    @JoinColumn(name = "lot_id", foreignKey = @ForeignKey(name = "fk_vaccination_lot"))
    private LotPorc lot;

    @ManyToOne
    @JoinColumn(name = "reproducteur_id", foreignKey = @ForeignKey(name = "fk_vaccination_reproducteur"))
    private Reproducteur reproducteur;

    @Column(name = "date_vaccination", nullable = false)
    private LocalDate dateVaccination;

    @Column(name = "date_rappel_prevue")
    private LocalDate dateRappelPrevue;

    @Column(name = "date_rappel_effectuee")
    private LocalDate dateRappelEffectuee;

    @Column(name = "numero_dose")
    private Integer numeroDose = 1;

    @Column(length = 100)
    private String veterinaire;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "statut_rappel")
    @Enumerated(EnumType.STRING)
    private StatutRappel statutRappel = StatutRappel.EN_ATTENTE;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean actif = true;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    @Column(name = "date_modification")
    private LocalDateTime dateModification = LocalDateTime.now();

    public enum StatutRappel {
        EN_ATTENTE,
        EFFECTUE,
        DEPASSE,
        ANNULE
    }
}
