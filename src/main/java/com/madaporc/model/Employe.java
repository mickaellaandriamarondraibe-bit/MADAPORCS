package com.madaporc.model;

import java.math.BigDecimal;
import java.time.LocalDate;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "employes")

public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;

    private String contact;
    private String  adresse;

    @ManyToOne
    @JoinColumn(name = "poste_employe_id")
    private PosteEmploye posteEmploye;
    @ManyToOne
    @JoinColumn(name = "statut_employe_id")
    private StatutEmploye statutEmploye;
    private LocalDate dateEmbauche;
    private BigDecimal salaireBase;

    
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }
    public String getContact() {
        return contact;
    }
    public void setContact(String contact) {
        this.contact = contact;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public PosteEmploye getPosteEmploye() {
        return posteEmploye;
    }
    public void setPosteEmploye(PosteEmploye posteEmploye) {
        this.posteEmploye = posteEmploye;
    }
    public StatutEmploye getStatutEmploye() {
        return statutEmploye;
    }
    public void setStatutEmploye(StatutEmploye statutEmploye) {
        this.statutEmploye = statutEmploye;
    }
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }
    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }
    public BigDecimal getSalaireBase() {
        return salaireBase;
    }
    public void setSalaireBase(BigDecimal salaireBase) {
        this.salaireBase = salaireBase;
    }


}
