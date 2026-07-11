package com.madaporc.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
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

    private static final List<String> STATUTS_GROUPES_ACTIFS =
            List.of("SAILLIE", "EN_GESTATION", "MISE_BAS_PROCHE");

    private final LotPorcRepository lotPorcRepository;
    private final GroupeReproductionRepository groupeReproductionRepository;
    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;
    private final IngredientRepository ingredientRepository;
    private final VaccinationRepository vaccinationRepository;
    private final AnalyseReproductionLotRepository analyseRepository;
    private final NotificationService notificationService;

    public DashboardService(LotPorcRepository lotPorcRepository,
                            GroupeReproductionRepository groupeReproductionRepository,
                            VenteRepository venteRepository,
                            DepenseRepository depenseRepository,
                            IngredientRepository ingredientRepository,
                            VaccinationRepository vaccinationRepository,
                            AnalyseReproductionLotRepository analyseRepository,
                            NotificationService notificationService) {
        this.lotPorcRepository = lotPorcRepository;
        this.groupeReproductionRepository = groupeReproductionRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.ingredientRepository = ingredientRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.analyseRepository = analyseRepository;
        this.notificationService = notificationService;
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

        remplirEvolutionCheptel(dashboard, aujourdHui.getYear());

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
                .findByDatePrevueMiseBasLessThanEqualAndDateMiseBasReelleIsNullOrderByDatePrevueMiseBasAsc(
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
        BigDecimal total = venteRepository.sommeVentesValideesEntre(periode.atDay(1), periode.atEndOfMonth());

        if (total == null) {
            return BigDecimal.ZERO;
        }

        return total;
    }

    public BigDecimal calculerDepensesMois(LocalDate mois) {
        YearMonth periode = YearMonth.from(mois);
        BigDecimal total = depenseRepository.sommeDepensesEntre(periode.atDay(1), periode.atEndOfMonth());

        if (total == null) {
            return BigDecimal.ZERO;
        }

        return total;
    }

    public BigDecimal calculerBeneficeNet(LocalDate debut, LocalDate fin) {
        BigDecimal ventes = venteRepository.sommeVentesValideesEntre(debut, fin);
        BigDecimal depenses = depenseRepository.sommeDepensesEntre(debut, fin);

        if (ventes == null) {
            ventes = BigDecimal.ZERO;
        }

        if (depenses == null) {
            depenses = BigDecimal.ZERO;
        }

        return ventes.subtract(depenses);
    }

    public List<Ingredient> listerStocksFaibles() {
        
        if (ingredientRepository.findStocksFaibles() == null || ingredientRepository.findStocksFaibles().isEmpty()) {
            return new ArrayList<>();
        }
        notificationService.envoyerNotification("Alerte : Stock faible détecté !");
        return ingredientRepository.findStocksFaibles();
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate dateLimite) {
        if (vaccinationRepository.findByDateRappelBetweenOrderByDateRappelAsc(LocalDate.now(), dateLimite) == null
                || vaccinationRepository.findByDateRappelBetweenOrderByDateRappelAsc(LocalDate.now(), dateLimite)
                        .isEmpty()) {
            return new ArrayList<>();
        }
        notificationService.envoyerNotification("Alerte : Vaccination à venir détectée !");
        return vaccinationRepository.findByDateRappelBetweenOrderByDateRappelAsc(
                LocalDate.now(),
                dateLimite
        );
    }

    private BigDecimal convertirTaux(Double valeur) {
        if (valeur == null) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(valeur).setScale(2, RoundingMode.HALF_UP);
    }

    private void remplirEvolutionCheptel(DashboardDTO dashboard, int annee) {
    List<String> moisLabels = new ArrayList<>();
    List<Integer> totalPorcsParMois = new ArrayList<>();
    List<Long> lotsActifsParMois = new ArrayList<>();
    List<Long> groupesActifsParMois = new ArrayList<>();

    List<LotPorc> lotsActifs = lotPorcRepository.findByStatut("ACTIF");
    List<GroupeReproduction> groupes = groupeReproductionRepository.findAll();

    for (int mois = 1; mois <= 12; mois++) {
        LocalDate finMois = YearMonth.of(annee, mois).atEndOfMonth();

        moisLabels.add(nomMoisCourt(mois));

        int totalPorcs = 0;
        long nombreLotsActifs = 0;
        long nombreGroupesActifs = 0;

        for (LotPorc lot : lotsActifs) {
            if (lot.getDateCreation() != null
                    && !lot.getDateCreation().isAfter(finMois)) {

                nombreLotsActifs++;

                if (lot.getEffectifActuel() != null) {
                    totalPorcs += lot.getEffectifActuel();
                }
            }
        }

        for (GroupeReproduction groupe : groupes) {
            if (groupe.getDateSaillie() != null
                    && !groupe.getDateSaillie().isAfter(finMois)
                    && STATUTS_GROUPES_ACTIFS.contains(groupe.getStatut())) {

                nombreGroupesActifs++;
            }
        }

        totalPorcsParMois.add(totalPorcs);
        lotsActifsParMois.add(nombreLotsActifs);
        groupesActifsParMois.add(nombreGroupesActifs);
    }

    dashboard.setMoisLabels(moisLabels);
    dashboard.setTotalPorcsParMois(totalPorcsParMois);
    dashboard.setLotsActifsParMois(lotsActifsParMois);
    dashboard.setGroupesActifsParMois(groupesActifsParMois);
}

    private String nomMoisCourt(int mois) {
        switch (mois) {
            case 1:
                return "Jan";
            case 2:
                return "Fev";
            case 3:
                return "Mar";
            case 4:
                return "Avr";
            case 5:
                return "Mai";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aou";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return "";
        }
    }
}
