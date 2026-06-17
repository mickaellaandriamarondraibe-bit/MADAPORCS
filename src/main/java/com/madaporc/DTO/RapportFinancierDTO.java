package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RapportFinancierDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private BigDecimal totalVentes;
    private BigDecimal totalDepenses;
    private BigDecimal solde;

    public RapportFinancierDTO() {}

    public RapportFinancierDTO(LocalDate dateDebut, LocalDate dateFin, 
                               BigDecimal totalVentes, BigDecimal totalDepenses, BigDecimal solde) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.totalVentes = totalVentes;
        this.totalDepenses = totalDepenses;
        this.solde = solde;
    }

    // Getters et Setters
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public BigDecimal getTotalVentes() { return totalVentes; }
    public void setTotalVentes(BigDecimal totalVentes) { this.totalVentes = totalVentes; }
    
    public BigDecimal getTotalDepenses() { return totalDepenses; }
    public void setTotalDepenses(BigDecimal totalDepenses) { this.totalDepenses = totalDepenses; }
    
    public BigDecimal getSolde() { return solde; }
    public void setSolde(BigDecimal solde) { this.solde = solde; }
}