package com.madaporc.service;
import com.madaporc.model.AlerteReproduction;
import com.madaporc.repository.AlerteReproductionRepository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final AlerteReproductionRepository alerteReproductionRepository;

    public DashboardService(AlerteReproductionRepository alerteReproductionRepository) {
        this.alerteReproductionRepository = alerteReproductionRepository;
    }

     // public DashboardDTO getDashboard(LocalDate debut, LocalDate fin)
        // public long compterLotsActifs()
        // public int calculerTotalPorcsActifs()
        // public long compterGroupesReproductionActifs()
        // public long compterMisesBasProches()
        // public BigDecimal calculerTauxAptitudeGlobal()
        // public BigDecimal calculerTauxFertiliteGlobal()
        // public BigDecimal calculerVentesMois(LocalDate mois)
        // public BigDecimal calculerDepensesMois(LocalDate mois)
        // public BigDecimal calculerBeneficeNet(LocalDate debut, LocalDate fin)
        // public List<Ingredient> listerStocksFaibles()
        // public List<Vaccination> listerVaccinationsAVenir(LocalDate dateLimite)
}