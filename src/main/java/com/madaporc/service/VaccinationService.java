package com.madaporc.service;

import com.madaporc.dto.VaccinationDTO;
import com.madaporc.model.LotPorc;
import com.madaporc.model.Vaccin;
import com.madaporc.model.Vaccination;
import com.madaporc.repository.LotPorcRepository;
import com.madaporc.repository.VaccinRepository;
import com.madaporc.repository.VaccinationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final LotPorcRepository lotPorcRepository;
    private final VaccinRepository vaccinRepository;

    @Transactional(readOnly = true)
    public List<Vaccination> getAll() {
        return vaccinationRepository.findAllWithDetails();
    }

   @Transactional(readOnly = true)
public VaccinationDTO getDtoById(Long id) {
    Vaccination vaccination = vaccinationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Vaccination introuvable avec l'id : " + id));

    VaccinationDTO dto = new VaccinationDTO();

    dto.setId(vaccination.getId());

    if (vaccination.getLot() != null) {
        dto.setLotId(vaccination.getLot().getId());
    }

    if (vaccination.getVaccin() != null) {
        dto.setVaccinId(vaccination.getVaccin().getId());
    }

    dto.setDateVaccination(vaccination.getDateVaccination());
    dto.setDateRappel(vaccination.getDateRappel());
    dto.setObservation(vaccination.getObservation());

    return dto;
}

    public String enregistrer(VaccinationDTO dto) {
        if (dto.getLotId() == null) {
            return "Le lot est obligatoire.";
        }

        if (dto.getVaccinId() == null) {
            return "Le vaccin est obligatoire.";
        }

        if (dto.getDateVaccination() == null) {
            return "La date de vaccination est obligatoire.";
        }

        if (dto.getDateRappel() != null && dto.getDateRappel().isBefore(dto.getDateVaccination())) {
            return "La date de rappel doit être supérieure ou égale à la date de vaccination.";
        }

        Vaccination vaccination;

        if (dto.getId() == null) {
            vaccination = new Vaccination();
        } else {
            vaccination = vaccinationRepository.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Vaccination introuvable avec l'id : " + dto.getId()));
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElseThrow(() -> new IllegalArgumentException("Lot introuvable avec l'id : " + dto.getLotId()));

        Vaccin vaccin = vaccinRepository.findById(dto.getVaccinId())
                .orElseThrow(() -> new IllegalArgumentException("Vaccin introuvable avec l'id : " + dto.getVaccinId()));

        vaccination.setLot(lot);
        vaccination.setVaccin(vaccin);
        vaccination.setDateVaccination(dto.getDateVaccination());
        vaccination.setDateRappel(dto.getDateRappel());
        vaccination.setObservation(dto.getObservation());

        vaccinationRepository.save(vaccination);

        return null;
    }
}