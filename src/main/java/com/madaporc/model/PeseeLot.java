package com.madaporc.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity placeholder pour PeseeLot.
 * À compléter avec les colonnes de la base de données.
 */
@Getter
@Setter
@Entity
@Table(name = "pesee_lots")
public class PeseeLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
