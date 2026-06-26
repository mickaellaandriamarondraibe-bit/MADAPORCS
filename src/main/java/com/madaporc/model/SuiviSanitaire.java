package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "suivis_sanitaires")
public class SuiviSanitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Lot concerné
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    // Maladie détectée
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maladie_id")
    private Maladie maladie;

    // Traitement appliqué
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "traitement_id")
    private Traitement traitement;

    @Column(name = "date_diagnostic", nullable = false)
    private LocalDate dateDiagnostic;

    @Column(name = "date_traitement")
    private LocalDate dateTraitement;

    @Column(name = "date_guerison")
    private LocalDate dateGuerison;

    @Column(name = "observation", columnDefinition = "TEXT")
    private String observation;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}