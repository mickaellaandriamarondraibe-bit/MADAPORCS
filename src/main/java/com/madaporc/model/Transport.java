package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
@Entity
@Table(name = "transports")
public class Transport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "depense_id")
    private Long depenseId;

    @Column(name = "libelle")
    private String libelle;

    @Column(name = "trajet")
    private String trajet;

    @Column(name = "cout")
    private BigDecimal cout;

    @Column(name = "date_transport")
    private LocalDate dateTransport;
}
