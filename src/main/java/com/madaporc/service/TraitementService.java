package com.madaporc.service;

import com.madaporc.model.Maladie;
import com.madaporc.model.Traitement;
import com.madaporc.repository.MaladieRepository;
import com.madaporc.repository.TraitementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TraitementService {

    private final TraitementRepository traitementRepository;
    private final MaladieRepository maladieRepository;

    @Transactional(readOnly = true)
    public List<Traitement> getAll() {
        return traitementRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Traitement getById(Long id) {
        return traitementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Traitement introuvable avec l'id : " + id));
    }

    @Transactional(readOnly = true)
    public List<Traitement> getByMaladie(Long maladieId) {
        if (maladieId == null) {
            return getAll();
        }

        return traitementRepository.findByMaladieId(maladieId);
    }

    public String enregistrer(Traitement traitement, Long maladieId) {
        if (traitement == null) {
            return "Les informations du traitement sont obligatoires.";
        }

        if (traitement.getNom() == null || traitement.getNom().trim().isEmpty()) {
            return "Le nom du traitement est obligatoire.";
        }

        traitement.setNom(traitement.getNom().trim());

        Maladie maladie = null;

        if (maladieId != null) {
            maladie = maladieRepository.findById(maladieId)
                    .orElseThrow(() -> new IllegalArgumentException("Maladie introuvable avec l'id : " + maladieId));
        }

        traitement.setMaladie(maladie);

        if (traitement.getId() == null) {
            if (traitementRepository.existsByNomIgnoreCaseAndMaladieId(traitement.getNom(), maladieId)) {
                return "Ce traitement existe déjà pour cette maladie.";
            }
        } else {
            if (traitementRepository.existsByNomIgnoreCaseAndMaladieIdAndIdNot(
                    traitement.getNom(),
                    maladieId,
                    traitement.getId()
            )) {
                return "Un autre traitement avec ce nom existe déjà pour cette maladie.";
            }
        }

        traitementRepository.save(traitement);
        return null;
    }

    public String supprimer(Long id) {
        Traitement traitement = getById(id);
        traitementRepository.delete(traitement);
        return null;
    }
}