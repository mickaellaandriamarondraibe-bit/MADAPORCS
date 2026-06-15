package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "type_mouvements_lots")
public class TypeMouvementLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
    private String libelle;
}
