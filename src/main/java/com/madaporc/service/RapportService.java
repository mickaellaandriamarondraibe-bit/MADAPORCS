package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.DTO.RapportDTO;
import com.madaporc.DTO.RapportFinancierDTO;
import com.madaporc.DTO.RapportPresenceDTO;
import com.madaporc.DTO.RapportProductionDTO;
import com.madaporc.DTO.RapportSanitaireDTO;
import com.madaporc.DTO.RapportStockDTO;
import com.madaporc.model.CycleProduction;
import com.madaporc.model.Depense;
import com.madaporc.model.Ingredient;
import com.madaporc.model.Presence;
import com.madaporc.model.SuiviSanitaire;
import com.madaporc.model.Vente;
import com.madaporc.repository.CycleProductionRepository;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.PresenceRepository;
import com.madaporc.repository.SuiviSanitaireRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class RapportService {

    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;
    private final IngredientRepository ingredientRepository;
    private final SuiviSanitaireRepository suiviSanitaireRepository;
    private final PresenceRepository presenceRepository;
    private final CycleProductionRepository cycleProductionRepository;

    public RapportService(
            VenteRepository venteRepository,
            DepenseRepository depenseRepository,
            IngredientRepository ingredientRepository,
            SuiviSanitaireRepository suiviSanitaireRepository,
            PresenceRepository presenceRepository,
            CycleProductionRepository cycleProductionRepository
    ) {
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
        this.ingredientRepository = ingredientRepository;
        this.suiviSanitaireRepository = suiviSanitaireRepository;
        this.presenceRepository = presenceRepository;
        this.cycleProductionRepository = cycleProductionRepository;
    }


    public RapportDTO genererRapportGlobal(LocalDate debut, LocalDate fin) {
        LocalDate dateDebut = debut == null ? LocalDate.now().withDayOfMonth(1) : debut;
        LocalDate dateFin = fin == null ? LocalDate.now() : fin;

        RapportDTO rapport = new RapportDTO();

        rapport.setFinancier(genererRapportFinancier(dateDebut, dateFin));
        rapport.setSanitaire(genererRapportSanitaire(dateDebut, dateFin));
        rapport.setStock(genererRapportStock());
        rapport.setPresence(genererRapportPresence(dateDebut, dateFin));
        rapport.setProduction(genererRapportProduction(dateDebut, dateFin));

        return rapport;
    }


    public RapportFinancierDTO genererRapportFinancier(LocalDate debut, LocalDate fin) {
        LocalDateTime debutDateTime = debut.atStartOfDay();
        LocalDateTime finDateTime = fin.plusDays(1).atStartOfDay();

        List<Vente> ventes = venteRepository.findByDateVenteBetween(debutDateTime, finDateTime);
        List<Depense> depenses = depenseRepository.findByDateDepenseBetween(debut, fin);

        BigDecimal totalVentes = ventes.stream()
                .map(Vente::getMontantTotal)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDepenses = depenses.stream()
                .map(Depense::getMontant)
                .filter(montant -> montant != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        RapportFinancierDTO dto = new RapportFinancierDTO();

        dto.setTotalVentes(totalVentes);
        dto.setTotalDepenses(totalDepenses);
        dto.setBeneficeNet(totalVentes.subtract(totalDepenses));
        dto.setNombreVentes(ventes.size());
        dto.setNombreDepenses(depenses.size());

        return dto;
    }


    public RapportSanitaireDTO genererRapportSanitaire(LocalDate debut, LocalDate fin) {
        List<SuiviSanitaire> suivis = suiviSanitaireRepository.findByDateDiagnosticBetween(debut, fin);

        long casEnCours = suivis.stream()
                .filter(s -> s.getDateGuerisonReelle() == null)
                .count();

        long casGueris = suivis.stream()
                .filter(s -> s.getDateGuerisonReelle() != null)
                .count();

        RapportSanitaireDTO dto = new RapportSanitaireDTO();

        dto.setNombreCas(suivis.size());
        dto.setCasEnCours(casEnCours);
        dto.setCasGueris(casGueris);

        return dto;
    }


    public RapportStockDTO genererRapportStock() {
        List<Ingredient> ingredients = ingredientRepository.findAll();

        BigDecimal valeurStock = ingredients.stream()
                .map(this::calculerValeurIngredient)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long stocksFaibles = ingredients.stream()
                .filter(this::stockFaible)
                .count();

        RapportStockDTO dto = new RapportStockDTO();

        dto.setNombreIngredients(ingredients.size());
        dto.setValeurStock(valeurStock);
        dto.setStocksFaibles(stocksFaibles);

        return dto;
    }


    public RapportPresenceDTO genererRapportPresence(LocalDate debut, LocalDate fin) {
        List<Presence> presences = presenceRepository.findByDatePresenceBetween(debut, fin);

        long presents = presences.stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Présent")
                        || estStatut(p.getStatutPresence(), "Present"))
                .count();

        long absents = presences.stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Absent"))
                .count();

        long retards = presences.stream()
                .filter(p -> estStatut(p.getStatutPresence(), "Retard"))
                .count();

        RapportPresenceDTO dto = new RapportPresenceDTO();

        dto.setNombrePointages(presences.size());
        dto.setPresents(presents);
        dto.setAbsents(absents);
        dto.setRetards(retards);

        return dto;
    }


    public RapportProductionDTO genererRapportProduction(LocalDate debut, LocalDate fin) {
        List<CycleProduction> cycles = cycleProductionRepository.findByDateDebutBetween(debut, fin);

        int totalNaissances = cycles.stream()
                .map(CycleProduction::getNombreNaissances)
                .filter(nombre -> nombre != null)
                .mapToInt(Integer::intValue)
                .sum();

        int totalPertes = cycles.stream()
                .map(CycleProduction::getNombrePertes)
                .filter(nombre -> nombre != null)
                .mapToInt(Integer::intValue)
                .sum();

        int totalVendables = cycles.stream()
                .map(CycleProduction::getNombreVendables)
                .filter(nombre -> nombre != null)
                .mapToInt(Integer::intValue)
                .sum();

        RapportProductionDTO dto = new RapportProductionDTO();

        dto.setNombreCycles(cycles.size());
        dto.setTotalNaissances(totalNaissances);
        dto.setTotalPertes(totalPertes);
        dto.setTotalVendables(totalVendables);

        return dto;
    }


    private BigDecimal calculerValeurIngredient(Ingredient ingredient) {
        BigDecimal stock = ingredient.getStockActuelKg() == null
                ? BigDecimal.ZERO
                : ingredient.getStockActuelKg();

        BigDecimal prix = ingredient.getPrixKg() == null
                ? BigDecimal.ZERO
                : ingredient.getPrixKg();

        return stock.multiply(prix);
    }


    private boolean stockFaible(Ingredient ingredient) {
        if (ingredient.getStockActuelKg() == null || ingredient.getSeuilMinKg() == null) {
            return false;
        }

        return ingredient.getStockActuelKg().compareTo(ingredient.getSeuilMinKg()) <= 0;
    }


    private boolean estStatut(String valeur, String statut) {
        return valeur != null && valeur.equalsIgnoreCase(statut);
    }
}