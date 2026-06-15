package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "distributions_aliment")
public class DistributionAliment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "melange_id")
    private Long melangeId;

    @Column(name = "date_distribution")
    private LocalDate dateDistribution;

    @Column(name = "quantite_kg")
    private BigDecimal quantiteKg;

    @Column(name = "cout_total")
    private BigDecimal coutTotal;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
