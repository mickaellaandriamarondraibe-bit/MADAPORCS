package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "statuts_utilisateur")
public class StatutUtilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
private String libelle;
}
