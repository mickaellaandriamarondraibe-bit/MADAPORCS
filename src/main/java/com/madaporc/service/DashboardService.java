package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.DashboardDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.Vaccination;
import com.madaporc.repository.CycleProductionRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.ReproducteurRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.VaccinationRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class DashboardService {

    private final LotPorcRepository lotPorcRepository;
    private final CycleProductionRepository cycleProductionRepository;
    private final ReproducteurRepository reproducteurRepository;
    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;
    private final IngredientRepository ingredientRepository;
    private final VaccinationRepository vaccinationRepository;

    public DashboardService(LotPorcRepository lotPorcRepository, CycleProductionRepository cycleProductionRepository,
            ReproducteurRepository reproducteurRepository, SuiviSanitaireRepository suiviSanitaireRepository,
            VenteRepository venteRepository, DepenseRepository depenseRepository,
            IngredientRepository ingredientRepository, VaccinationRepository vaccinationRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.cycleProductionRepository = cycleProductionRepository;
        this.reproducteurRepository = reproducteurRepository;
        this.suiviSanitaireRepository = suiviSanitaireRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.ingredientRepository = ingredientRepository;
        this.vaccinationRepository = vaccinationRepository;
    }

    public DashboardDTO getDashboard(LocalDate debut, LocalDate fin) {
        DashboardDTO dto = new DashboardDTO();
        dto.setLotsActifs(compterLotsActifs());
        dto.setTotalPorcs(calculerTotalPorcsActifs());
        dto.setPorcsVendables(calculerPorcsVendables());
        dto.setReproducteursActifs(compterReproducteursActifs());
        dto.setCasSanitairesEnCours(compterCasSanitairesEnCours());
        dto.setVentesMois(calculerVentesMois(LocalDate.now()));
        dto.setDepensesMois(calculerDepensesMois(LocalDate.now()));
        dto.setBeneficeNet(calculerBeneficeNet(debut, fin));
        return dto;
    }

    public long compterLotsActifs() {
        return lotPorcRepository.findAll().stream().filter(l -> l.getArchivedAt() == null).count();
    }

    public int calculerTotalPorcsActifs() {
        Integer total = lotPorcRepository.sumNombreActuelActifs();
        return total == null ? 0 : total;
    }

    public int calculerPorcsVendables() {
        Integer total = cycleProductionRepository.sumNombreVendablesActifs();
        return total == null ? 0 : total;
    }

    public long compterReproducteursActifs() {
        return reproducteurRepository.findAll().stream().filter(r -> r.getArchivedAt() == null).count();
    }

    public long compterCasSanitairesEnCours() {
        return suiviSanitaireRepository.findAll().stream().filter(s -> s.getDateGuerisonReelle() == null).count();
    }

    public BigDecimal calculerVentesMois(LocalDate mois) {
        LocalDate debut = mois.withDayOfMonth(1);
        LocalDate fin = mois.withDayOfMonth(mois.lengthOfMonth());
        return venteRepository.sumMontantByDateBetween(LocalDateTime.of(debut, LocalTime.MIN),
                LocalDateTime.of(fin, LocalTime.MAX));
    }

    public BigDecimal calculerDepensesMois(LocalDate mois) {
        return depenseRepository.sumMontantByDateBetween(mois.withDayOfMonth(1),
                mois.withDayOfMonth(mois.lengthOfMonth()));
    }

    public BigDecimal calculerBeneficeNet(LocalDate debut, LocalDate fin) {
        BigDecimal ventes = venteRepository.sumMontantByDateBetween(LocalDateTime.of(debut, LocalTime.MIN),
                LocalDateTime.of(fin, LocalTime.MAX));
        BigDecimal depenses = depenseRepository.sumMontantByDateBetween(debut, fin);
        return ventes.subtract(depenses);
    }

    public List<Ingredient> listerStocksFaibles() {
        return ingredientRepository.findStocksFaibles();
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate dateLimite) {
        return vaccinationRepository.findByDateRappelBetween(LocalDate.now(), dateLimite);
    }
}
