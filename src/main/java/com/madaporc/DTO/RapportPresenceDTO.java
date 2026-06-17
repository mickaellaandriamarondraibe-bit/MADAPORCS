package com.madaporc.DTO;

import java.time.LocalDate;

public class RapportPresenceDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer nombrePresences;
    private Integer nombreAbsences;
    private Integer nombreRetards;

    public RapportPresenceDTO() {}

    public RapportPresenceDTO(LocalDate dateDebut, LocalDate dateFin,
                              Integer nombrePresences, Integer nombreAbsences, Integer nombreRetards) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.nombrePresences = nombrePresences;
        this.nombreAbsences = nombreAbsences;
        this.nombreRetards = nombreRetards;
    }

    // Getters et Setters
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Integer getNombrePresences() { return nombrePresences; }
    public void setNombrePresences(Integer nombrePresences) { this.nombrePresences = nombrePresences; }
    
    public Integer getNombreAbsences() { return nombreAbsences; }
    public void setNombreAbsences(Integer nombreAbsences) { this.nombreAbsences = nombreAbsences; }
    
    public Integer getNombreRetards() { return nombreRetards; }
    public void setNombreRetards(Integer nombreRetards) { this.nombreRetards = nombreRetards; }
}