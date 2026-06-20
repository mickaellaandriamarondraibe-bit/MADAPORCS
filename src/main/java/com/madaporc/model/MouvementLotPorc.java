package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mouvement_lot_porc")
public class MouvementLotPorc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
}