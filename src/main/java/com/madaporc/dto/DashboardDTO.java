package com.madaporc.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.madaporc.model.Ingredient;
import com.madaporc.model.Vaccination;

public class DashboardDTO {

    private long lotsActifs;
    private int totalPorcs;
    private long groupesActifs;
    private long misesBasProches;
    private BigDecimal tauxAptitudeGlobale = BigDecimal.ZERO;
    private BigDecimal tauxFertiliteObserve = BigDecimal.ZERO;
    private BigDecimal ventesMois = BigDecimal.ZERO;
    private BigDecimal depensesMois = BigDecimal.ZERO;
    private BigDecimal beneficeNet = BigDecimal.ZERO;
    private List<Ingredient> stocksFaibles = new ArrayList<>();
    private List<Vaccination> vaccinationsAVenir = new ArrayList<>();

    public long getLotsActifs() {
        return lotsActifs;
    }

    public void setLotsActifs(long lotsActifs) {
        this.lotsActifs = lotsActifs;
    }

    public int getTotalPorcs() {
        return totalPorcs;
    }

    public void setTotalPorcs(int totalPorcs) {
        this.totalPorcs = totalPorcs;
    }

    public long getGroupesActifs() {
        return groupesActifs;
    }

    public void setGroupesActifs(long groupesActifs) {
        this.groupesActifs = groupesActifs;
    }

    public long getMisesBasProches() {
        return misesBasProches;
    }

    public void setMisesBasProches(long misesBasProches) {
        this.misesBasProches = misesBasProches;
    }

    public BigDecimal getTauxAptitudeGlobale() {
        return tauxAptitudeGlobale;
    }

    public void setTauxAptitudeGlobale(BigDecimal tauxAptitudeGlobale) {
        this.tauxAptitudeGlobale = tauxAptitudeGlobale;
    }

    public BigDecimal getTauxFertiliteObserve() {
        return tauxFertiliteObserve;
    }

    public void setTauxFertiliteObserve(BigDecimal tauxFertiliteObserve) {
        this.tauxFertiliteObserve = tauxFertiliteObserve;
    }

    public BigDecimal getVentesMois() {
        return ventesMois;
    }

    public void setVentesMois(BigDecimal ventesMois) {
        this.ventesMois = ventesMois;
    }

    public BigDecimal getDepensesMois() {
        return depensesMois;
    }

    public void setDepensesMois(BigDecimal depensesMois) {
        this.depensesMois = depensesMois;
    }

    public BigDecimal getBeneficeNet() {
        return beneficeNet;
    }

    public void setBeneficeNet(BigDecimal beneficeNet) {
        this.beneficeNet = beneficeNet;
    }

    public List<Ingredient> getStocksFaibles() {
        return stocksFaibles;
    }

    public void setStocksFaibles(List<Ingredient> stocksFaibles) {
        this.stocksFaibles = stocksFaibles;
    }

    public List<Vaccination> getVaccinationsAVenir() {
        return vaccinationsAVenir;
    }

    public void setVaccinationsAVenir(List<Vaccination> vaccinationsAVenir) {
        this.vaccinationsAVenir = vaccinationsAVenir;
    }
}
