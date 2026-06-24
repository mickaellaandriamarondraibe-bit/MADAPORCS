package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "pesees_lots")
public class PeseeLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @Column(name = "date_pesee", nullable = false)
    private LocalDate datePesee;

    @Column(name = "poids_moyen", nullable = false, precision = 10, scale = 2)
    private BigDecimal poidsMoyen;

    @Column(name = "observation")
    private String observation;
}

