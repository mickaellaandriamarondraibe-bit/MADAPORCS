package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "depenses")
public class Depense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categorie_depense_id")
    private Long categorieDepenseId;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "montant")
    private BigDecimal montant;

    @Column(name = "date_depense")
    private LocalDate dateDepense;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
