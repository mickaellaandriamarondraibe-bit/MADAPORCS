package com.madaporc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

/**
 * Model placeholder pour la table pesees_lots.
 * Les colonnes exactes seront ajoutées pendant le développement du module.
 */
@Getter
@Setter
@Entity
@Table(name="pesees_lots")
public class PeseeLot {
    private Long id;
    private Long lotPorcId;
    private Double poidsMoyenKg;
    private LocalDate datePesee;
    private String observation;
    private LocalDate createdBy;
    private LocalDate createdAt;
}
