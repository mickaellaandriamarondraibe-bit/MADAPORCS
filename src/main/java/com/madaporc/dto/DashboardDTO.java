package com.madaporc.dto;

public class DashboardDTO {

    private int lotsActifs;
    private int totalPorcs;
    private int groupesReproductionActifs;
    private int misesBasProches;
    private int alertesReproduction;
    private double tauxAptitudeGlobale;
    private double tauxFertiliteObserve;
    private double ventesDuMois;
    private double depensesDuMois;
    private double beneficeNet;
    private int alertesStock;
    private int vaccinationsAVenir;

    public DashboardDTO() {
    }
    public DashboardDTO(int lotsActifs, int totalPorcs, int groupesReproductionActifs, int misesBasProches,
        int alertesReproduction, double tauxAptitudeGlobale, double tauxFertiliteObserve, double ventesDuMois,
            double depensesDuMois, double beneficeNet, int alertesStock, int vaccinationsAVenir) {
        this.lotsActifs = lotsActifs;
        this.totalPorcs = totalPorcs;
        this.groupesReproductionActifs = groupesReproductionActifs;
        this.misesBasProches = misesBasProches;
        this.alertesReproduction = alertesReproduction;
        this.tauxAptitudeGlobale = tauxAptitudeGlobale;
        this.tauxFertiliteObserve = tauxFertiliteObserve;
        this.ventesDuMois = ventesDuMois;
        this.depensesDuMois = depensesDuMois;
        this.beneficeNet = beneficeNet;
        this.alertesStock = alertesStock;
        this.vaccinationsAVenir = vaccinationsAVenir;
    }

    public int getLotsActifs() {
        return lotsActifs;
    }

    public void setLotsActifs(int lotsActifs) {
        this.lotsActifs = lotsActifs;
    }

    public int getTotalPorcs() {
        return totalPorcs;
    }

    public void setTotalPorcs(int totalPorcs) {
        this.totalPorcs = totalPorcs;
    }

    public int getGroupesReproductionActifs() {
        return groupesReproductionActifs;
    }

    public void setGroupesReproductionActifs(int groupesReproductionActifs) {
        this.groupesReproductionActifs = groupesReproductionActifs;
    }

    public int getMisesBasProches() {
        return misesBasProches;
    }

    public void setMisesBasProches(int misesBasProches) {
        this.misesBasProches = misesBasProches;
    }

    public int getAlertesReproduction() {
        return alertesReproduction;
    }

    public void setAlertesReproduction(int alertesReproduction) {
        this.alertesReproduction = alertesReproduction;
    }

    public double getTauxAptitudeGlobale() {
        return tauxAptitudeGlobale;
    }

    public void setTauxAptitudeGlobale(double tauxAptitudeGlobale) {
        this.tauxAptitudeGlobale = tauxAptitudeGlobale;
    }

    public double getTauxFertiliteObserve() {
        return tauxFertiliteObserve;
    }

    public void setTauxFertiliteObserve(double tauxFertiliteObserve) {
        this.tauxFertiliteObserve = tauxFertiliteObserve;
    }

    public double getVentesDuMois() {
        return ventesDuMois;
    }

    public void setVentesDuMois(double ventesDuMois) {
        this.ventesDuMois = ventesDuMois;
    }

    public double getDepensesDuMois() {
        return depensesDuMois;
    }

    public void setDepensesDuMois(double depensesDuMois) {
        this.depensesDuMois = depensesDuMois;
    }

    public double getBeneficeNet() {
        return beneficeNet;
    }

    public void setBeneficeNet(double beneficeNet) {
        this.beneficeNet = beneficeNet;
    }

    public int getAlertesStock() {
        return alertesStock;
    }

    public void setAlertesStock(int alertesStock) {
        this.alertesStock = alertesStock;
    }

    public int getVaccinationsAVenir() {
        return vaccinationsAVenir;
    }
    public void setVaccinationsAVenir(int vaccinationsAVenir) {
        this.vaccinationsAVenir = vaccinationsAVenir;
    }    
}