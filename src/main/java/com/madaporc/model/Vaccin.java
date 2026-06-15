package com.madaporc.model;

    import jakarta.persistence.*;
import java.math.BigDecimal;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "vaccins")
    public class Vaccin {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "libelle")
    private String libelle;

    @Column(name = "prix")
    private BigDecimal prix;

    @Column(name = "delai_rappel_jours")
    private Integer delaiRappelJours;

    @Column(name = "description")
    private String description;

    @Column(name = "actif")
    private Boolean actif;
    }
