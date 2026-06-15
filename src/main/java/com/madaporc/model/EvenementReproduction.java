package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "evenements_reproduction")
public class EvenementReproduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "femelle_id")
    private Long femelleId;

    @Column(name = "male_id")
    private Long maleId;

    @Column(name = "type_evenement_reproduction_id")
    private Long typeEvenementReproductionId;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "date_evenement")
    private LocalDate dateEvenement;

    @Column(name = "nombre_porcelets_nes")
    private Integer nombrePorceletsNes;

    @Column(name = "nombre_porcelets_morts")
    private Integer nombrePorceletsMorts;

    @Column(name = "nombre_porcelets_vivants")
    private Integer nombrePorceletsVivants;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
