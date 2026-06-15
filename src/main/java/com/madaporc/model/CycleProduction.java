package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "cycles_production")
public class CycleProduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_cycle")
    private String codeCycle;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @Column(name = "date_fin_prevue")
    private LocalDate dateFinPrevue;

    @Column(name = "date_fin_reelle")
    private LocalDate dateFinReelle;

    @Column(name = "nombre_naissances")
    private Integer nombreNaissances;

    @Column(name = "nombre_pertes")
    private Integer nombrePertes;

    @Column(name = "nombre_vivants")
    private Integer nombreVivants;

    @Column(name = "nombre_vendables")
    private Integer nombreVendables;

    @Column(name = "statut_cycle")
    private String statutCycle;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
