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

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final VaccinationRepository vaccinationRepository;
    private final LotPorcRepository lotPorcRepository;
    private final VaccinRepository vaccinRepository;

    public List<Vaccination> getAll() {
        return vaccinationRepository.findAll();
    }

    public VaccinationDTO getDtoById(Long id) {
        Vaccination ent = vaccinationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaccination introuvable"));

        VaccinationDTO dto = new VaccinationDTO();
        dto.setId(ent.getId());
        dto.setLotId(ent.getLotId());
        dto.setVaccinId(ent.getVaccin().getId());
        dto.setDateVaccination(ent.getDateVaccination());
        dto.setDateRappel(ent.getDateRappel());
        dto.setObservation(ent.getObservation());
        return dto;
    }

    public String enregistrer(VaccinationDTO dto) {
        if (dto == null) return "Données invalides.";
        if (dto.getLotId() == null) return "Lot obligatoire.";
        if (dto.getVaccinId() == null) return "Vaccin obligatoire.";
        if (dto.getDateVaccination() == null) return "Date de vaccination obligatoire.";

        if (dto.getDateRappel() != null && dto.getDateRappel().isBefore(dto.getDateVaccination())) {
            return "Date de rappel doit être >= date vaccination.";
        }
        if (dto.getDateVaccination().isAfter(LocalDate.now())) {
            return "Date de vaccination ne peut pas être dans le futur.";
        }

        LotPorc lot = lotPorcRepository.findById(dto.getLotId())
                .orElse(null);
        Vaccin vaccin = vaccinRepository.findById(dto.getVaccinId())
                .orElse(null);
        if (lot == null) return "Lot introuvable.";
        if (vaccin == null) return "Vaccin introuvable.";

        Vaccination ent = (dto.getId() == null)
                ? new Vaccination()
                : vaccinationRepository.findById(dto.getId()).orElse(new Vaccination());

        ent.setLot(lot);
        ent.setVaccin(vaccin);
        ent.setDateVaccination(dto.getDateVaccination());
        ent.setDateRappel(dto.getDateRappel());
        ent.setObservation(dto.getObservation());

        vaccinationRepository.save(ent);
        return null;
    }
}

