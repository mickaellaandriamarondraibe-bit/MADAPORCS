package com.madaporc.model;

    import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    @Entity
    @Table(name = "ventes")
    public class Vente {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name = "client_id")
    private Long clientId;

    @Column(name = "date_vente")
    private LocalDateTime dateVente;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Column(name = "statut_vente")
    private String statutVente;

    @Column(name = "observation")
    private String observation;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
    }
