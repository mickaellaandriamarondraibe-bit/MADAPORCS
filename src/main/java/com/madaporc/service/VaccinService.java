package com.madaporc.service;

import com.madaporc.dto.VaccinDTO;
import com.madaporc.model.Vaccin;
import com.madaporc.repository.VaccinRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinService {

    private final VaccinRepository vaccinRepository;

    public List<Vaccin> getAll() {
        return vaccinRepository.findAll();
    }

    public VaccinDTO getDtoById(Long id) {
        Vaccin v = vaccinRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Vaccin introuvable"));

        VaccinDTO dto = new VaccinDTO();
        dto.setId(v.getId());
        dto.setNom(v.getNom());
        dto.setDescription(v.getDescription());
        // Champs transient non persistés: on laisse tels quels
        dto.setMaladieCiblee(v.getMaladieCiblee());
        dto.setVoie(v.getVoie());
        dto.setDelaiRappel(v.getDelaiRappel());
        return dto;
    }

    public String enregistrer(VaccinDTO dto) {
        if (dto == null) return "Données invalides.";
        if (dto.getNom() == null || dto.getNom().trim().isEmpty()) return "Nom du vaccin obligatoire.";

        Vaccin v = (dto.getId() == null)
                ? new Vaccin()
                : vaccinRepository.findById(dto.getId()).orElse(new Vaccin());

        v.setNom(dto.getNom().trim());
        v.setDescription(dto.getDescription());

        v.setMaladieCiblee(dto.getMaladieCiblee());
        v.setVoie(dto.getVoie());
        v.setDelaiRappel(dto.getDelaiRappel());

        vaccinRepository.save(v);
        return null;
    }
}

