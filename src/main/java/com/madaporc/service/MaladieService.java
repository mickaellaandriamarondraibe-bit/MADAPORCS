package com.madaporc.service;

import com.madaporc.model.Maladie;
import com.madaporc.repository.MaladieRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MaladieService {

    private final MaladieRepository maladieRepository;

    public MaladieService(MaladieRepository maladieRepository) {
        this.maladieRepository = maladieRepository;
    }


    public List<Maladie> getAll() {
        return maladieRepository.findAll();
    }

    public Maladie getById(Long id) {
        return maladieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maladie introuvable"));
    }

}

