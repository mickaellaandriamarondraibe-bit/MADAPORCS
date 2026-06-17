package com.madaporc.service;

import com.madaporc.DTO.RapportDTO;
import com.madaporc.DTO.RapportFinancierDTO;
import com.madaporc.DTO.RapportSanitaireDTO;
import com.madaporc.DTO.RapportStockDTO;
import com.madaporc.DTO.RapportPresenceDTO;
import com.madaporc.DTO.RapportProductionDTO;
import com.madaporc.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public RapportDTO genererRapportGlobal(LocalDate debut, LocalDate fin) {
        RapportDTO rapportGlobal = new RapportDTO();
        rapportGlobal.setRapportFinancier(genererRapportFinancier(debut, fin));
        rapportGlobal.setRapportSanitaire(genererRapportSanitaire(debut, fin));
        rapportGlobal.setRapportStock(genererRapportStock());
        rapportGlobal.setRapportPresence(genererRapportPresence(debut, fin));
        rapportGlobal.setRapportProduction(genererRapportProduction(debut, fin));
        return rapportGlobal;
    }

    public RapportFinancierDTO genererRapportFinancier(LocalDate debut, LocalDate fin) {
        RapportFinancierDTO dto = new RapportFinancierDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(LocalTime.MAX);

        BigDecimal totalVentes = venteRepository.sumMontantByIdDateBetween(debutDateTime, finDateTime);
        BigDecimal totalDepenses = depenseRepository.sumMontantByIdDateBetween(debut, fin);

        dto.setTotalVentes(totalVentes);
        dto.setTotalDepenses(totalDepenses);
        dto.setSolde(totalVentes.subtract(totalDepenses));

        return dto;
    }

    public RapportSanitaireDTO genererRapportSanitaire(LocalDate debut, LocalDate fin) {
        RapportSanitaireDTO dto = new RapportSanitaireDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        Long total = suiviSanitaireRepository.countByDateBetween(debut, fin);
        Long gueris = suiviSanitaireRepository.countByDateBetweenAndStatut(debut, fin, "Gueri");
        Long enCours = suiviSanitaireRepository.countByDateBetweenAndStatut(debut, fin, "En cours");

        dto.setNombreCasTotal(total.intValue());
        dto.setNombreCasGueris(gueris.intValue());
        dto.setNombreCasEnCours(enCours.intValue());

        return dto;
    }

    public RapportStockDTO genererRapportStock() {
        RapportStockDTO dto = new RapportStockDTO();
        
        Long sousSeuil = ingredientRepository.countIngredientsUnderThreshold();
        dto.setNombreIngredientsSousSeuil(sousSeuil.intValue());

        return dto;
    }

    public RapportPresenceDTO genererRapportPresence(LocalDate debut, LocalDate fin) {
        RapportPresenceDTO dto = new RapportPresenceDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        Long presents = presenceRepository.countByDateBetweenAndStatut(debut, fin, "Present");
        Long absents = presenceRepository.countByDateBetweenAndStatut(debut, fin, "Absent");
        Long retards = presenceRepository.countByDateBetweenAndStatut(debut, fin, "Retard");

        dto.setNombrePresences(presents.intValue());
        dto.setNombreAbsences(absents.intValue());
        dto.setNombreRetards(retards.intValue());

        return dto;
    }

    public RapportProductionDTO genererRapportProduction(LocalDate debut, LocalDate fin) {
        RapportProductionDTO dto = new RapportProductionDTO();
        dto.setDateDebut(debut);
        dto.setDateFin(fin);

        Integer totalNaissances = cycleProductionRepository.sumNaissancesByDateBetween(debut, fin);
        Integer totalPertes = cycleProductionRepository.sumPertesByDateBetween(debut, fin);
        Integer totalVivants = cycleProductionRepository.sumVivantsByDateBetween(debut, fin);
        Integer totalVendables = cycleProductionRepository.sumVendablesByDateBetween(debut, fin);

        dto.setTotalNaissances(totalNaissances);
        dto.setTotalPertes(totalPertes);
        dto.setTotalVivants(totalVivants);
        dto.setTotalVendables(totalVendables);
        
        dto.setGainPoidsMoyen(BigDecimal.ZERO);

        return dto;
    }
}