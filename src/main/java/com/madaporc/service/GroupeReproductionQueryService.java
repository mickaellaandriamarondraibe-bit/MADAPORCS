package com.madaporc.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.madaporc.model.GroupeReproduction;
import com.madaporc.repository.GroupeReproductionRepository;

@Service
public class GroupeReproductionQueryService {

    private final GroupeReproductionRepository repository;

    public GroupeReproductionQueryService(
            GroupeReproductionRepository repository) {

        this.repository = repository;
    }

    public List<GroupeReproduction> findAll() {
        return repository.findAll();
    }

    public GroupeReproduction findById(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Groupe introuvable"));
    }
}