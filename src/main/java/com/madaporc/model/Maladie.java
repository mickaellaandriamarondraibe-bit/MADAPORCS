package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "maladies")
public class Maladie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "description")
    private String description;
}

