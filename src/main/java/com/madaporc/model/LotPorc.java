package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lot_porc")
public class LotPorc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
}

