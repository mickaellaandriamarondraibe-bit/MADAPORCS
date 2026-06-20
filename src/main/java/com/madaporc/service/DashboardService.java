package com.madaporc.service;
import com.madaporc.model.AlerteReproduction;
import com.madaporc.repository.AlerteReproductionRepository;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    private final AlerteReproductionRepository alerteReproductionRepository;

    public DashboardService(AlerteReproductionRepository alerteReproductionRepository) {
        this.alerteReproductionRepository = alerteReproductionRepository;
    }

    public long countAlerteReproduction() {
        return alerteReproductionRepository.count();
    }
}