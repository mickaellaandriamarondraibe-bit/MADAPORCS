package com.madaporc.service;

import com.madaporc.DTO.VaccinationDTO;
import com.madaporc.model.Vaccination;
import com.madaporc.model.Vaccin;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Reproducteur;
import com.madaporc.repository.VaccinationRepository;
import com.madaporc.repository.VaccinRepository;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.ReproducteurRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des vaccinations.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final VaccinRepository vaccinRepository;
    private final LotPorcRepository lotPorcRepository;
    private final ReproducteurRepository reproducteurRepository;

    /**
     * Récupère toutes les vaccinations actives.
     */
    public List<Vaccination> findAllVaccinationsActives() {
        return vaccinationRepository.findAll().stream()
            .filter(Vaccination::getActif)
            .toList();
    }

    /**
     * Récupère toutes les vaccinations.
     */
    public List<Vaccination> findAllVaccinations() {
        return vaccinationRepository.findAll();
    }

    /**
     * Crée une nouvelle vaccination.
     */
    public String creer(VaccinationDTO dto) {
        Vaccination vaccination = new Vaccination();
        mapperDTOToEntity(dto, vaccination);
        vaccination.setDateVaccination(LocalDate.now());
        vaccinationRepository.save(vaccination);
        return "Vaccination créée avec succès";
    }

    /**
     * Modifie une vaccination existante.
     */
    public String modifier(Long id, VaccinationDTO dto) {
        Vaccination vaccination = vaccinationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccination non trouvée"));
        mapperDTOToEntity(dto, vaccination);
        vaccination.setDateModification(LocalDateTime.now());
        vaccinationRepository.save(vaccination);
        return "Vaccination modifiée avec succès";
    }

    /**
     * Désactive une vaccination.
     */
    public String desactiverVaccination(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccination non trouvée"));
        vaccination.setActif(false);
        vaccination.setDateModification(LocalDateTime.now());
        vaccinationRepository.save(vaccination);
        return "Vaccination désactivée avec succès";
    }

    /**
     * Récupère les vaccinations pour un lot.
     */
    public List<Vaccination> findVaccinationsByLot(Long lotId) {
        return vaccinationRepository.findByLotId(lotId);
    }

    /**
     * Récupère les vaccinations pour un reproducteur.
     */
    public List<Vaccination> findVaccinationsByReproducteur(Long reproducteurId) {
        return vaccinationRepository.findByReproducteurId(reproducteurId);
    }

    /**
     * Récupère les vaccinations pour un vaccin.
     */
    public List<Vaccination> findVaccinationsByVaccin(Long vaccinId) {
        return vaccinationRepository.findByVaccinId(vaccinId);
    }

    /**
     * Obtient une vaccination par ID.
     */
    public Vaccination findById(Long id) {
        return vaccinationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Vaccination non trouvée"));
    }

    /**
     * Enregistre un rappel de vaccination.
     */
    public String enregistrerRappel(Long vaccinationId) {
        Vaccination vaccination = findById(vaccinationId);
        vaccination.setDateRappelEffectuee(LocalDate.now());
        vaccination.setStatutRappel(Vaccination.StatutRappel.EFFECTUE);
        vaccination.setDateModification(LocalDateTime.now());
        vaccinationRepository.save(vaccination);
        return "Rappel enregistré avec succès";
    }

    /**
     * Récupère les rappels critiques à faire.
     */
    public List<Vaccination> findRappelsCritiques(LocalDate dateLimite) {
        return vaccinationRepository.findRappelsCritiques(dateLimite);
    }

    /**
     * Compte les vaccinations effectuées.
     */
    public long compterVaccinationsEffectuees() {
        return vaccinationRepository.findAll().stream()
            .filter(Vaccination::getActif)
            .count();
    }

    private void mapperDTOToEntity(VaccinationDTO dto, Vaccination vaccination) {
        if (dto.getVaccinId() != null) {
            Vaccin vaccin = vaccinRepository.findById(dto.getVaccinId())
                .orElseThrow(() -> new RuntimeException("Vaccin non trouvé"));
            vaccination.setVaccin(vaccin);
        }
        
        if (dto.getLotId() != null) {
            LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElseThrow(() -> new RuntimeException("Lot non trouvé"));
            vaccination.setLot(lot);
        }
        
        if (dto.getReproducteurId() != null) {
            Reproducteur reproducteur = reproducteurRepository.findById(dto.getReproducteurId())
                .orElseThrow(() -> new RuntimeException("Reproducteur non trouvé"));
            vaccination.setReproducteur(reproducteur);
        }
        
        if (dto.getDateVaccination() != null) {
            vaccination.setDateVaccination(dto.getDateVaccination());
        }
        
        vaccination.setDateRappelPrevue(dto.getDateRappelPrevue());
        vaccination.setDateRappelEffectuee(dto.getDateRappelEffectuee());
        vaccination.setNumeroDose(dto.getNumeroDose() != null ? dto.getNumeroDose() : 1);
        vaccination.setVeterinaire(dto.getVeterinaire());
        vaccination.setNotes(dto.getNotes());
        
        if (dto.getStatutRappel() != null) {
            try {
                vaccination.setStatutRappel(Vaccination.StatutRappel.valueOf(dto.getStatutRappel()));
            } catch (IllegalArgumentException e) {
                vaccination.setStatutRappel(Vaccination.StatutRappel.EN_ATTENTE);
            }
        }
        
        if (dto.getActif() != null) {
            vaccination.setActif(dto.getActif());
        }
    }
}
