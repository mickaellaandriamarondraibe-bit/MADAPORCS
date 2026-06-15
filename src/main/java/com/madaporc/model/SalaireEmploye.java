package com.madaporc.model;

    import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "salaires_employes")
    public class SalaireEmploye {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "employe_id")
    private Long employeId;

    @Column(name = "mois")
    private Integer mois;

    @Column(name = "annee")
    private Integer annee;

    @Column(name = "montant_base")
    private BigDecimal montantBase;

    @Column(name = "prime")
    private BigDecimal prime;

    @Column(name = "retenue")
    private BigDecimal retenue;

    @Column(name = "montant_net")
    private BigDecimal montantNet;

    @Column(name = "statut_paiement")
    private String statutPaiement;

    @Column(name = "date_paiement")
    private LocalDate datePaiement;
    }
