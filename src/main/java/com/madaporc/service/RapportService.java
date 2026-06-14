package com.madaporc.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.madaporc.repository.VenteRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.PresenceRepository;
import com.madaporc.repository.CycleProductionRepository;

import com.madaporc.DTO.RapportDTO;
import com.madaporc.DTO.RapportFinancierDTO;
import com.madaporc.DTO.RapportSanitaireDTO;
import com.madaporc.DTO.RapportStockDTO;
import com.madaporc.DTO.RapportPresenceDTO;
import com.madaporc.DTO.RapportProductionDTO;

@Service
public class RapportService {

    @Autowired
    private VenteRepository venteRepository;

    @Autowired
    private DepenseRepository depenseRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private SuiviSanitaireRepository suiviSanitaireRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private CycleProductionRepository cycleProductionRepository;

    public RapportDTO genererRapportGlobal(
            LocalDate debut,
            LocalDate fin) {

        RapportDTO dto = new RapportDTO();

        dto.setFinancier(
                genererRapportFinancier(debut, fin));

        dto.setSanitaire(
                genererRapportSanitaire(debut, fin));

        dto.setStock(
                genererRapportStock());

        dto.setPresence(
                genererRapportPresence(debut, fin));

        dto.setProduction(
                genererRapportProduction(debut, fin));

        return dto;
    }

    public RapportFinancierDTO genererRapportFinancier(
            LocalDate debut,
            LocalDate fin) {

        RapportFinancierDTO dto = new RapportFinancierDTO();

        // logique

        return dto;
    }

    public RapportSanitaireDTO genererRapportSanitaire(
            LocalDate debut,
            LocalDate fin) {

        RapportSanitaireDTO dto = new RapportSanitaireDTO();

        // logique

        return dto;
    }

    public RapportStockDTO genererRapportStock() {

        RapportStockDTO dto = new RapportStockDTO();

        // logique

        return dto;
    }

    public RapportPresenceDTO genererRapportPresence(
            LocalDate debut,
            LocalDate fin) {

        RapportPresenceDTO dto = new RapportPresenceDTO();

        // logique

        return dto;
    }

    public RapportProductionDTO genererRapportProduction(
            LocalDate debut,
            LocalDate fin) {

        RapportProductionDTO dto = new RapportProductionDTO();

        // logique

        return dto;
    }
}
