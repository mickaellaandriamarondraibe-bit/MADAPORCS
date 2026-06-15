package com.madaporc.model;

    import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "pesees_lots")
    public class PeseeLot {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "poids_moyen_kg")
    private BigDecimal poidsMoyenKg;

    @Column(name = "date_pesee")
    private LocalDate datePesee;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    }
