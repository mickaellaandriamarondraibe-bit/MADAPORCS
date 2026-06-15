package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "statuts_employes")
public class StatutEmploye {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
private String libelle;
}
