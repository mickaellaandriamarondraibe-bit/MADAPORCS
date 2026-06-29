package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.madaporc.dto.RapportFiltreDTO;
import com.madaporc.dto.RapportFinancierDTO;
import com.madaporc.repository.DepenseRepository;
import com.madaporc.repository.VenteRepository;

@Service
public class RapportService {

    private final VenteRepository venteRepository;
    private final DepenseRepository depenseRepository;

    public RapportService(VenteRepository venteRepository, DepenseRepository depenseRepository) {
        this.venteRepository = venteRepository;
        this.depenseRepository = depenseRepository;
    }

    // Genere le rapport financier (recettes, depenses, benefice net) sur la periode du filtre.
    public RapportFinancierDTO genererRapport(RapportFiltreDTO filtre) {
        validerFiltre(filtre);

        // Si une date manque, on prend une periode large par defaut.
        LocalDate debut = filtre.getDateDebut() != null ? filtre.getDateDebut() : LocalDate.of(2000, 1, 1);
        LocalDate fin = filtre.getDateFin() != null ? filtre.getDateFin() : LocalDate.now();

        BigDecimal totalVentes = calculerTotalVentes(debut, fin);
        BigDecimal totalDepenses = calculerTotalDepenses(debut, fin);

        RapportFinancierDTO rapport = new RapportFinancierDTO();
        rapport.setDateDebut(filtre.getDateDebut());
        rapport.setDateFin(filtre.getDateFin());
        rapport.setTotalVentes(totalVentes);
        rapport.setTotalDepenses(totalDepenses);
        rapport.setBeneficeNet(calculerBeneficeNet(totalVentes, totalDepenses));
        rapport.setNombreVentes(compterVentes(debut, fin));
        rapport.setNombreDepenses(compterDepenses(debut, fin));
        return rapport;
    }

    // Total des ventes VALIDEE seulement.
    public BigDecimal calculerTotalVentes(LocalDate debut, LocalDate fin) {
        BigDecimal total = venteRepository.sommeVentesValideesEntre(debut, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal calculerTotalDepenses(LocalDate debut, LocalDate fin) {
        BigDecimal total = depenseRepository.sommeDepensesEntre(debut, fin);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal calculerBeneficeNet(BigDecimal totalVentes, BigDecimal totalDepenses) {
        return totalVentes.subtract(totalDepenses);
    }

    public Long compterVentes(LocalDate debut, LocalDate fin) {
        return venteRepository.countByDateVenteBetweenAndStatut(debut, fin, "VALIDEE");
    }

    public Long compterDepenses(LocalDate debut, LocalDate fin) {
        return depenseRepository.countByDateDepenseBetween(debut, fin);
    }

    // Les deux dates sont facultatives ; si les deux existent, debut <= fin.
    public void validerFiltre(RapportFiltreDTO filtre) {
        if (filtre.getDateDebut() != null && filtre.getDateFin() != null
                && filtre.getDateDebut().isAfter(filtre.getDateFin())) {
            throw new IllegalArgumentException("La date de début doit être avant la date de fin.");
        }
    }
}
