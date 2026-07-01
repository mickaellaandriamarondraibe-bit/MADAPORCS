package com.madaporc.service;

import com.madaporc.model.Traitement;
import com.madaporc.repository.TraitementRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraitementService {

    private final TraitementRepository traitementRepository;

    public TraitementService(TraitementRepository traitementRepository) {
        this.traitementRepository = traitementRepository;
    }


    public List<Traitement> getAll() {
        return traitementRepository.findAll();
    }

    public Traitement getById(Long id) {
        return traitementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Traitement introuvable"));
    }

}

