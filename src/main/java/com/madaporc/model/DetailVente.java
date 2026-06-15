package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "details_vente")
public class DetailVente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vente_id")
    private Long venteId;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "nombre_porcs_vendus")
    private Integer nombrePorcsVendus;

    @Column(name = "poids_total_kg")
    private BigDecimal poidsTotalKg;

    @Column(name = "prix_kg")
    private BigDecimal prixKg;

    @Column(name = "prix_unitaire")
    private BigDecimal prixUnitaire;

    @Column(name = "montant")
    private BigDecimal montant;
}
