package com.madaporc.service;

import com.madaporc.model.AlerteReproduction;
import com.madaporc.repository.AlerteReproductionRepository;

import java.util.List;

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

    public List<AlerteReproduction> listerAlertesActives(){
        return null;
    }

    public void genererAlertesMiseBasProche(){
        
    }

    public String marquerCommeLue(Long id){
        return null;
    }

    public String traiter(Long id){
        return null;
    }
        
}