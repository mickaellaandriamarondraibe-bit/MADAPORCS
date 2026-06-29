package com.madaporc.service;

import com.madaporc.DTO.VaccinDTO;
import com.madaporc.model.Vaccin;
import com.madaporc.repository.VaccinRepository;
import java.math.BigDecimal;
import java.util.List;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des vaccins.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VaccinService {

    private final VaccinRepository vaccinRepository;

    /**
     * Récupère tous les vaccins actifs.
     */
    public List<Vaccin> findAllVaccinsActifs() {
        return vaccinRepository.findByActifTrue();
    }

    /**
     * Récupère tous les vaccins.
     */
    public List<Vaccin> findAllVaccins() {
        return vaccinRepository.findAll();
    }

    /**
     * Crée un nouveau vaccin.
     */
    public String creer(VaccinDTO dto) {
        if (vaccinRepository.findByLibelle(dto.getLibelle()).isPresent()) {
            return "Un vaccin avec ce libellé existe déjà";
        }
        Vaccin vaccin = new Vaccin();
        mapperDTOToEntity(dto, vaccin);
        vaccinRepository.save(vaccin);
        return "Vaccin créé avec succès";
    }

    /**
     * Modifie un vaccin existant.
     */
    public String modifier(Long id, VaccinDTO dto) {
        Vaccin vaccin = vaccinRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccin non trouvé"));
        mapperDTOToEntity(dto, vaccin);
        vaccin.setDateModification(LocalDateTime.now());
        vaccinRepository.save(vaccin);
        return "Vaccin modifié avec succès";
    }

    /**
     * Désactive un vaccin.
     */
    public String desactiverVaccin(Long id) {
        Vaccin vaccin = vaccinRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccin non trouvé"));
        vaccin.setActif(false);
        vaccin.setDateModification(LocalDateTime.now());
        vaccinRepository.save(vaccin);
        return "Vaccin désactivé avec succès";
    }

    /**
     * Compte les vaccins actifs.
     */
    public long compterVaccinsActifs() {
        return vaccinRepository.findByActifTrue().size();
    }

    /**
     * Calcule le budget total des vaccins.
     */
    public BigDecimal calculerBudgetVaccins() {
        return vaccinRepository.findByActifTrue().stream()
            .map(Vaccin::getPrixDose)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Recherche des vaccins par mot-clé.
     */
    public List<Vaccin> rechercherVaccins(String motCle) {
        return vaccinRepository.rechercherParMotCle(motCle);
    }

    /**
     * Obtient un vaccin par ID.
     */
    public Vaccin findById(Long id) {
        return vaccinRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccin non trouvé"));
    }

    private void mapperDTOToEntity(VaccinDTO dto, Vaccin vaccin) {
        vaccin.setLibelle(dto.getLibelle());
        vaccin.setDescription(dto.getDescription());
        vaccin.setFabricant(dto.getFabricant());
        vaccin.setPrixDose(dto.getPrixDose());
        vaccin.setDelaiRappelJours(dto.getDelaiRappelJours());
        vaccin.setAgeMinimumJours(dto.getAgeMinimumJours());
        vaccin.setAgeMaximumJours(dto.getAgeMaximumJours());
        vaccin.setTemperatureStockageMin(dto.getTemperatureStockageMin());
        vaccin.setTemperatureStockageMax(dto.getTemperatureStockageMax());
        if (dto.getActif() != null) {
            vaccin.setActif(dto.getActif());
        }
    }
}
