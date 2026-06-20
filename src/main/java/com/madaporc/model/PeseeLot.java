package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lot_porc")
public class PeseeLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}