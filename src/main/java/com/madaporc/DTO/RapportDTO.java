package com.madaporc.DTO;

public class RapportDTO {
    private RapportFinancierDTO rapportFinancier;
    private RapportSanitaireDTO rapportSanitaire;
    private RapportStockDTO rapportStock;
    private RapportPresenceDTO rapportPresence;
    private RapportProductionDTO rapportProduction;

    public RapportDTO() {}

    // Getters et Setters
    public RapportFinancierDTO getRapportFinancier() {
        return rapportFinancier;
    }

    public void setRapportFinancier(RapportFinancierDTO rapportFinancier) {
        this.rapportFinancier = rapportFinancier;
    }

    public RapportSanitaireDTO getRapportSanitaire() {
        return rapportSanitaire;
    }

    public void setRapportSanitaire(RapportSanitaireDTO rapportSanitaire) {
        this.rapportSanitaire = rapportSanitaire;
    }

    public RapportStockDTO getRapportStock() {
        return rapportStock;
    }

    public void setRapportStock(RapportStockDTO rapportStock) {
        this.rapportStock = rapportStock;
    }

    public RapportPresenceDTO getRapportPresence() {
        return rapportPresence;
    }

    public void setRapportPresence(RapportPresenceDTO rapportPresence) {
        this.rapportPresence = rapportPresence;
    }

    public RapportProductionDTO getRapportProduction() {
        return rapportProduction;
    }

    public void setRapportProduction(RapportProductionDTO rapportProduction) {
        this.rapportProduction = rapportProduction;
    }
}