package com.madaporc.service;

import com.madaporc.model.Maladie;
import com.madaporc.repository.MaladieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MaladieService {

    private final MaladieRepository maladieRepository;

    @Transactional(readOnly = true)
    public List<Maladie> getAll() {
        return maladieRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Maladie getById(Long id) {
        return maladieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Maladie introuvable avec l'id : " + id));
    }

    public String enregistrer(Maladie maladie) {
        if (maladie == null) {
            return "Les informations de la maladie sont obligatoires.";
        }

        if (maladie.getNom() == null || maladie.getNom().trim().isEmpty()) {
            return "Le nom de la maladie est obligatoire.";
        }

        maladie.setNom(maladie.getNom().trim());

        if (maladie.getId() == null) {
            if (maladieRepository.existsByNomIgnoreCase(maladie.getNom())) {
                return "Une maladie avec ce nom existe déjà.";
            }
        } else {
            if (maladieRepository.existsByNomIgnoreCaseAndIdNot(maladie.getNom(), maladie.getId())) {
                return "Une autre maladie avec ce nom existe déjà.";
            }
        }

        maladieRepository.save(maladie);
        return null;
    }

    public String supprimer(Long id) {
        Maladie maladie = getById(id);
        maladieRepository.delete(maladie);
        return null;
    }
}