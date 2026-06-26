package com.madaporc.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vaccinations")
public class Vaccination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaccin_id", nullable = false)
    private Vaccin vaccin;

    @Column(name = "date_vaccination", nullable = false)
    private LocalDate dateVaccination;

    @Column(name = "date_rappel")
    private LocalDate dateRappel;

    @Column(columnDefinition = "TEXT")
    private String observation;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
