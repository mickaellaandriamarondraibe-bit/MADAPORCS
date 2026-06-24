package com.madaporc.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.model.GroupeReproduction;
import com.madaporc.dto.GroupeReproductionDTO;
import com.madaporc.repository.GroupeReproductionRepository;
import com.madaporc.repository.LotPorcRepository;

@Service
public class GroupeReproductionQueryService {

    private final GroupeReproductionRepository repository;
    private final LotPorcRepository lotPorcRepository; 

    public GroupeReproductionQueryService(
            GroupeReproductionRepository repository,
            LotPorcRepository lotPorcRepository) {
        this.repository = repository;
        this.lotPorcRepository = lotPorcRepository;
    }

    public List<GroupeReproduction> findAll() {
        return repository.findAll();
    }

    public GroupeReproduction findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Groupe introuvable"));
    }

 
    public void prepareFormModel(Model model, Long id) {
        
        if (id != null) {
            GroupeReproduction groupe = findById(id);
            GroupeReproductionDTO dto = new GroupeReproductionDTO();
            dto.setLotFemelleId(groupe.getLotFemelle().getId());
            dto.setLotMaleId(groupe.getLotMale().getId());
            dto.setNombreFemellesConcernees(groupe.getNombreFemellesConcernees());
            dto.setDateSaillie(groupe.getDateSaillie());
            dto.setObservation(groupe.getObservation());
            model.addAttribute("groupeReproductionDTO", dto);
        } else if (!model.containsAttribute("groupeReproductionDTO")) {
            model.addAttribute("groupeReproductionDTO", new GroupeReproductionDTO());
        }

        model.addAttribute("lotsFemelles", lotPorcRepository.findBySexeAndStatut("FEMELLE", "ACTIF"));
        model.addAttribute("lotsMales", lotPorcRepository.findBySexeAndStatut("MALE", "ACTIF"));
    }
}