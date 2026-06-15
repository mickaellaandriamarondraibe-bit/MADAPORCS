package com.madaporc.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "statuts_suivi_sanitaire")
public class StatutSuiviSanitaire {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "libelle")
private String libelle;
}
