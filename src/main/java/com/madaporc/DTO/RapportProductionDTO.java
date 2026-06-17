package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RapportProductionDTO {
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Integer totalNaissances;
    private Integer totalPertes;
    private Integer totalVivants;
    private Integer totalVendables;
    private BigDecimal gainPoidsMoyen;

    public RapportProductionDTO() {}

    public RapportProductionDTO(LocalDate dateDebut, LocalDate dateFin,
                                Integer totalNaissances, Integer totalPertes,
                                Integer totalVivants, Integer totalVendables,
                                BigDecimal gainPoidsMoyen) {
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.totalNaissances = totalNaissances;
        this.totalPertes = totalPertes;
        this.totalVivants = totalVivants;
        this.totalVendables = totalVendables;
        this.gainPoidsMoyen = gainPoidsMoyen;
    }

    // Getters et Setters
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    
    public Integer getTotalNaissances() { return totalNaissances; }
    public void setTotalNaissances(Integer totalNaissances) { this.totalNaissances = totalNaissances; }
    
    public Integer getTotalPertes() { return totalPertes; }
    public void setTotalPertes(Integer totalPertes) { this.totalPertes = totalPertes; }
    
    public Integer getTotalVivants() { return totalVivants; }
    public void setTotalVivants(Integer totalVivants) { this.totalVivants = totalVivants; }
    
    public Integer getTotalVendables() { return totalVendables; }
    public void setTotalVendables(Integer totalVendables) { this.totalVendables = totalVendables; }
    
    public BigDecimal getGainPoidsMoyen() { return gainPoidsMoyen; }
    public void setGainPoidsMoyen(BigDecimal gainPoidsMoyen) { this.gainPoidsMoyen = gainPoidsMoyen; }
}