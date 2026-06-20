package com.madaporc.service;
import com.madaporc.model.AlerteReproduction;
import com.madaporc.repository.AlerteReproductionRepository;
import org.springframework.stereotype.Service;

@Service
public class AlerteReproductionService {
    private final AlerteReproductionRepository alerteReproductionRepository;

    public AlerteReproductionService(AlerteReproductionRepository alerteReproductionRepository) {
        this.alerteReproductionRepository = alerteReproductionRepository;
    }

    public long countAlerteReproduction() {
        return alerteReproductionRepository.count();
    }
    
}