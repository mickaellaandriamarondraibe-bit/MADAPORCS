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
import com.madaporc.model.MouvementLotPorc;
import com.madaporc.model.Vaccination;
import com.madaporc.repository.AnalyseReproductionLotRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.MouvementLotPorcRepository;
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
    private final MouvementLotPorcRepository mouvementLotPorcRepository;

    public DashboardService(LotPorcRepository lotPorcRepository,
                            GroupeReproductionRepository groupeReproductionRepository,
                            VenteRepository venteRepository,
                            DepenseRepository depenseRepository,
                            IngredientRepository ingredientRepository,
                            VaccinationRepository vaccinationRepository,
                            AnalyseReproductionLotRepository analyseRepository,
                            MouvementLotPorcRepository mouvementLotPorcRepository) {
        this.lotPorcRepository = lotPorcRepository;
        this.groupeReproductionRepository = groupeReproductionRepository;
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.ingredientRepository = ingredientRepository;
        this.vaccinationRepository = vaccinationRepository;
        this.analyseRepository = analyseRepository;
        this.mouvementLotPorcRepository = mouvementLotPorcRepository;
    }

    // Tableau de bord du mois courant.
    public DashboardDTO getDashboard() {
        return getDashboard(YearMonth.from(LocalDate.now()));
    }

    // Tableau de bord "historique" : reconstruit l'etat de l'elevage a la FIN du mois choisi
    // (plafonne a aujourd'hui). Tout est calcule a cette date de reference.
    public DashboardDTO getDashboard(YearMonth mois) {
        LocalDate aujourdHui = LocalDate.now();
        if (mois == null) {
            mois = YearMonth.from(aujourdHui);
        }
        // Date de reference = fin du mois choisi, sans depasser aujourd'hui (pas de futur).
        LocalDate asOf = mois.atEndOfMonth().isAfter(aujourdHui) ? aujourdHui : mois.atEndOfMonth();

        DashboardDTO dashboard = new DashboardDTO();

        // Etat du cheptel reconstruit a la date de reference (effectif via les mouvements).
        List<LotPorc> lots = lotPorcRepository.findAll();
        List<GroupeReproduction> groupes = groupeReproductionRepository.findAll();
        List<MouvementLotPorc> mouvements = mouvementLotPorcRepository.findAll();
        EtatCheptel etat = etatCheptelAsOf(asOf, lots, groupes, mouvements);
        dashboard.setLotsActifs(etat.lots());
        dashboard.setTotalPorcs(etat.porcs());
        dashboard.setGroupesActifs(etat.groupes());
        dashboard.setMisesBasProches(listerMisesBasProches(asOf).size());

        // Taux d'aptitude / fertilite : valeurs globales (snapshots), non datees.
        dashboard.setTauxAptitudeGlobale(calculerTauxAptitudeGlobal());
        dashboard.setTauxFertiliteObserve(calculerTauxFertiliteGlobal());

        // Finances du mois choisi.
        dashboard.setVentesMois(calculerVentesMois(mois.atDay(1)));
        dashboard.setDepensesMois(calculerDepensesMois(mois.atDay(1)));
        dashboard.setBeneficeNet(dashboard.getVentesMois().subtract(dashboard.getDepensesMois()));

        dashboard.setStocksFaibles(listerStocksFaibles());
        dashboard.setVaccinationsAVenir(listerVaccinationsAVenir(asOf, asOf.plusDays(30)));

        // Graphe d'evolution sur l'annee du mois choisi.
        remplirEvolutionCheptel(dashboard, mois.getYear());

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
        return listerMisesBasProches(LocalDate.now());
    }

    // Mises bas proches par rapport a une date de reference (dans les 5 jours suivants).
    public List<GroupeReproduction> listerMisesBasProches(LocalDate asOf) {
        return groupeReproductionRepository
                .findByDatePrevueMiseBasLessThanEqualAndDateMiseBasReelleIsNullOrderByDatePrevueMiseBasAsc(
                        asOf.plusDays(5));
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
        List<Ingredient> faibles = ingredientRepository.findStocksFaibles();
        return faibles != null ? faibles : new ArrayList<>();
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate dateLimite) {
        return listerVaccinationsAVenir(LocalDate.now(), dateLimite);
    }

    public List<Vaccination> listerVaccinationsAVenir(LocalDate debut, LocalDate dateLimite) {
        List<Vaccination> vaccinations = vaccinationRepository
                .findByDateRappelBetweenOrderByDateRappelAsc(debut, dateLimite);
        return vaccinations != null ? vaccinations : new ArrayList<>();
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

        // Tous les lots (y compris archives) : ils existaient dans les mois passes.
        List<LotPorc> lots = lotPorcRepository.findAll();
        List<GroupeReproduction> groupes = groupeReproductionRepository.findAll();
        List<MouvementLotPorc> mouvements = mouvementLotPorcRepository.findAll();
        LocalDate aujourdHui = LocalDate.now();

        for (int mois = 1; mois <= 12; mois++) {
            YearMonth ym = YearMonth.of(annee, mois);
            moisLabels.add(nomMoisCourt(mois));

            // Un mois entierement dans le futur reste a zero.
            if (ym.atDay(1).isAfter(aujourdHui)) {
                totalPorcsParMois.add(0);
                lotsActifsParMois.add(0L);
                groupesActifsParMois.add(0L);
                continue;
            }

            // Date de reference : fin du mois, ou AUJOURD'HUI pour le mois en cours
            // (sinon le mois courant serait considere comme "futur" et affiche a zero,
            // masquant les lots crees ce mois-ci).
            LocalDate asOf = ym.atEndOfMonth().isAfter(aujourdHui) ? aujourdHui : ym.atEndOfMonth();

            EtatCheptel etat = etatCheptelAsOf(asOf, lots, groupes, mouvements);
            totalPorcsParMois.add(etat.porcs());
            lotsActifsParMois.add(etat.lots());
            groupesActifsParMois.add(etat.groupes());
        }

        dashboard.setMoisLabels(moisLabels);
        dashboard.setTotalPorcsParMois(totalPorcsParMois);
        dashboard.setLotsActifsParMois(lotsActifsParMois);
        dashboard.setGroupesActifsParMois(groupesActifsParMois);
    }

    // Etat du cheptel a une date de reference : effectif total, nb de lots actifs,
    // nb de groupes actifs. L'effectif est reconstruit a partir des mouvements posterieurs.
    private EtatCheptel etatCheptelAsOf(LocalDate asOf, List<LotPorc> lots,
            List<GroupeReproduction> groupes, List<MouvementLotPorc> mouvements) {
        int totalPorcs = 0;
        long nombreLotsActifs = 0;

        for (LotPorc lot : lots) {
            // Lot pas encore cree a cette date (date de creation inconnue => on l'inclut).
            if (lot.getDateCreation() != null && lot.getDateCreation().isAfter(asOf)) {
                continue;
            }
            // Effectif historique = effectif actuel MOINS les mouvements survenus APRES "asOf".
            int effectif = lot.getEffectifActuel() != null ? lot.getEffectifActuel() : 0;
            for (MouvementLotPorc m : mouvements) {
                if (m.getLot() != null && m.getLot().getId().equals(lot.getId())
                        && m.getDateMouvement() != null && m.getDateMouvement().isAfter(asOf)) {
                    effectif -= deltaSigneMouvement(m);
                }
            }
            if (effectif > 0) {
                nombreLotsActifs++;
                totalPorcs += effectif;
            }
        }

        long nombreGroupesActifs = 0;
        for (GroupeReproduction groupe : groupes) {
            if (groupe.getDateSaillie() != null
                    && !groupe.getDateSaillie().isAfter(asOf)
                    && STATUTS_GROUPES_ACTIFS.contains(groupe.getStatut())) {
                nombreGroupesActifs++;
            }
        }

        return new EtatCheptel(totalPorcs, nombreLotsActifs, nombreGroupesActifs);
    }

    private record EtatCheptel(int porcs, long lots, long groupes) {}

    // Variation d'effectif d'un mouvement : + pour une entree, - pour une sortie.
    private int deltaSigneMouvement(MouvementLotPorc m) {
        String type = m.getTypeMouvement();
        int q = m.getQuantite() != null ? m.getQuantite() : 0;
        if ("ENTREE".equals(type) || "NAISSANCE".equals(type) || "TRANSFERT_ENTREE".equals(type)) {
            return q;
        }
        if ("SORTIE".equals(type) || "DECES".equals(type) || "VENTE".equals(type)
                || "TRANSFERT_SORTIE".equals(type)) {
            return -q;
        }
        return 0;
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
