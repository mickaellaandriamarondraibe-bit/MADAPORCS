package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "suivis_sanitaires")
public class SuiviSanitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "reproducteur_id")
    private Long reproducteurId;

    @Column(name = "nombre_porcs_malades")
    private Integer nombrePorcsMalades;

    @Column(name = "maladie_id")
    private Long maladieId;

    @Column(name = "traitement_id")
    private Long traitementId;

    @Column(name = "statut_suivi_sanitaire_id")
    private Long statutSuiviSanitaireId;

    @Column(name = "date_diagnostic")
    private LocalDate dateDiagnostic;

    @Column(name = "date_guerison_prevue")
    private LocalDate dateGuerisonPrevue;

    @Column(name = "date_guerison_reelle")
    private LocalDate dateGuerisonReelle;

    @Column(name = "symptomes")
    private String symptomes;

    @Column(name = "diagnostic")
    private String diagnostic;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
