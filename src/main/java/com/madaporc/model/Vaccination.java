package com.madaporc.model;

    import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "vaccinations")
    public class Vaccination {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "reproducteur_id")
    private Long reproducteurId;

    @Column(name = "vaccin_id")
    private Long vaccinId;

    @Column(name = "date_vaccination")
    private LocalDate dateVaccination;

    @Column(name = "date_rappel")
    private LocalDate dateRappel;

    @Column(name = "dose")
    private String dose;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    }
