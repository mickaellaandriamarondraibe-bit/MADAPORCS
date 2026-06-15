package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vente_id")
    private Long venteId;

    @Column(name = "numero_facture")
    private String numeroFacture;

    @Column(name = "date_facture")
    private LocalDateTime dateFacture;

    @Column(name = "montant_total")
    private BigDecimal montantTotal;

    @Column(name = "fichier_url")
    private String fichierUrl;
}
