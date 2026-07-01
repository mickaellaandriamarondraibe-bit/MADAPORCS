package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "traitements")
public class Traitement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maladie_id")
    private Maladie maladie;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;
}

