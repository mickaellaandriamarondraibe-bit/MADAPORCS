package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "ratios_croissance")
public class RatioCroissance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "race_id")
    private Long raceId;

    @Column(name = "melange_id")
    private Long melangeId;

    @Column(name = "age_mois_min")
    private Integer ageMoisMin;

    @Column(name = "age_mois_max")
    private Integer ageMoisMax;

    @Column(name = "ration_kg_jour")
    private BigDecimal rationKgJour;

    @Column(name = "gain_poids_estime_kg")
    private BigDecimal gainPoidsEstimeKg;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
