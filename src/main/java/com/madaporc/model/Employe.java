package com.madaporc.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "employes")
public class Employe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @Column(name = "contact")
    private String contact;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "poste_employe_id")
    private Long posteEmployeId;

    @Column(name = "statut_employe_id")
    private Long statutEmployeId;

    @Column(name = "date_embauche")
    private LocalDate dateEmbauche;

    @Column(name = "salaire_base")
    private BigDecimal salaireBase;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
