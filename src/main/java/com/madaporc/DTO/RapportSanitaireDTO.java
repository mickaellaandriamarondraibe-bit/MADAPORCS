package com.madaporc.DTO;

import java.time.LocalDate;

public class RapportSanitaireDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombreCasTotal;
    private Integer nombreCasEnCours;
    private Integer nombreCasGueris;

    public RapportSanitaireDTO() {}

    public RapportSanitaireDTO(LocalDate dateDebut, LocalDate dateFin,
                               Integer nombreCasTotal, Integer nombreCasEnCours, Integer nombreCasGueris) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombreCasTotal = nombreCasTotal;
        this.nombreCasEnCours = nombreCasEnCours;
        this.nombreCasGueris = nombreCasGueris;
    }

    // Getters et Setters
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Integer getNombreCasTotal() { return nombreCasTotal; }
    public void setNombreCasTotal(Integer nombreCasTotal) { this.nombreCasTotal = nombreCasTotal; }
    
    public Integer getNombreCasEnCours() { return nombreCasEnCours; }
    public void setNombreCasEnCours(Integer nombreCasEnCours) { this.nombreCasEnCours = nombreCasEnCours; }
    
    public Integer getNombreCasGueris() { return nombreCasGueris; }
    public void setNombreCasGueris(Integer nombreCasGueris) { this.nombreCasGueris = nombreCasGueris; }
}