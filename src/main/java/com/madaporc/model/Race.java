package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name= "race")
public class Race {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}