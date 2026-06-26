package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.dto.DashboardDTO;
import com.madaporc.model.GroupeReproduction;
import com.madaporc.model.Ingredient;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Vaccination;
import com.madaporc.repository.AnalyseReproductionLotRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.VaccinationRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class DashboardService {

    private static final List<String> STATUTS_GROUPES_ACTIFS = List.of("SAILLIE", "EN_GESTATION", "MISE_BAS_PROCHE");

    private final LotPorcRepository lotPorcRepository;
    private final GroupeReproductionRepository groupeReproductionRepository;
    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;
    private final IngredientRepository ingredientRepository;
    private final VaccinationRepository vaccinationRepository;
    private final AnalyseReproductionLotRepository analyseRepository;

    public DashboardService(LotPorcRepository lotPorcRepository,
                            GroupeReproductionRepository groupeReproductionRepository,
                            VenteRepository venteRepository,
                            DepenseRepository depenseRepository,
                            IngredientRepository ingredientRepository,
                            VaccinationRepository vaccinationRepository,
                            AnalyseReproductionLotRepository analyseRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.groupeReproductionRepository = groupeReproductionRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.ingredientRepository = ingredientRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.analyseRepository = analyseRepository;
    }

    public DashboardDTO getDashboard() {
        LocalDate aujourdHui = LocalDate.now();
        YearMonth moisCourant = YearMonth.from(aujourdHui);

        DashboardDTO dashboard = new DashboardDTO();
        dashboard.setLotsActifs(compterLotsActifs());
        dashboard.setTotalPorcs(calculerTotalPorcsActifs());
        dashboard.setGroupesActifs(compterGroupesReproductionActifs());
        dashboard.setMisesBasProches(compterMisesBasProches());
        dashboard.setTauxAptitudeGlobale(calculerTauxAptitudeGlobal());
        dashboard.setTauxFertiliteObserve(calculerTauxFertiliteGlobal());
        dashboard.setVentesMois(calculerVentesMois(moisCourant.atDay(1)));
        dashboard.setDepensesMois(calculerDepensesMois(moisCourant.atDay(1)));
        dashboard.setBeneficeNet(dashboard.getVentesMois().subtract(dashboard.getDepensesMois()));
        dashboard.setStocksFaibles(listerStocksFaibles());
        dashboard.setVaccinationsAVenir(listerVaccinationsAVenir(aujourdHui.plusDays(30)));
        return dashboard;
    }

    public long compterLotsActifs() {
        return lotPorcRepository.countByStatut("ACTIF");
    }

    public int calculerTotalPorcsActifs() {
        return lotPorcRepository.findByStatut("ACTIF").stream()
                .map(LotPorc::getEffectifActuel)
                .filter(effectif -> effectif != null)
                .mapToInt(Integer::intValue)
                .sum();
    }

    public long compterGroupesReproductionActifs() {
        return groupeReproductionRepository.countByStatutIn(STATUTS_GROUPES_ACTIFS);
    }

    public long compterMisesBasProches() {
        return listerMisesBasProches().size();
    }

    public List<GroupeReproduction> listerMisesBasProches() {
        LocalDate aujourdHui = LocalDate.now();
        return groupeReproductionRepository
                .findByDatePrevueMiseBasBetweenAndDateMiseBasReelleIsNullOrderByDatePrevueMiseBasAsc(
                        aujourdHui,
                        aujourdHui.plusDays(5));
    }

    public BigDecimal calculerTauxAptitudeGlobal() {
        return convertirTaux(analyseRepository.moyenneTauxAptitudeGlobale());
    }

    public BigDecimal calculerTauxFertiliteGlobal() {
        return convertirTaux(analyseRepository.moyenneTauxFertiliteObserve());
    }

    public BigDecimal calculerVentesMois(LocalDate mois) {
        YearMonth periode = YearMonth.from(mois);
        return venteRepository.sommeVentesValideesEntre(periode.atDay(1), periode.atEndOfMonth());
    }

    public BigDecimal calculerDepensesMois(LocalDate mois) {
        YearMonth periode = YearMonth.from(mois);
        return depenseRepository.sommeDepensesEntre(periode.atDay(1), periode.atEndOfMonth());
    }

    public BigDecimal calculerBeneficeNet(LocalDate debut, LocalDate fin) {
        BigDecimal ventes = venteRepository.sommeVentesValideesEntre(debut, fin);
        BigDecimal depenses = depenseRepository.sommeDepensesEntre(debut, fin);
        return ventes.subtract(depenses);
    }

    public List<Ingredient> listerStocksFaibles() {
        return ingredientRepository.findStocksFaibles();
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate dateLimite) {
        return vaccinationRepository.findByDateRappelBetweenOrderByDateRappelAsc(LocalDate.now(), dateLimite);
    }

    private BigDecimal convertirTaux(Double valeur) {
        if (valeur == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(valeur).setScale(2, RoundingMode.HALF_UP);
    }
}
