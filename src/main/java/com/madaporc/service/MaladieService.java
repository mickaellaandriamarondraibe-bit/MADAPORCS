package com.madaporc.service;

import com.madaporc.DTO.MaladieDTO;
import com.madaporc.model.Maladie;
import com.madaporc.repository.MaladieRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des maladies.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MaladieService {

    private final MaladieRepository maladieRepository;

    /**
     * Récupère toutes les maladies actives.
     */
    public List<Maladie> findAllMaladiesActives() {
        return maladieRepository.findByActifTrue();
    }

    /**
     * Récupère toutes les maladies.
     */
    public List<Maladie> findAllMaladies() {
        return maladieRepository.findAll();
    }

    /**
     * Crée une nouvelle maladie.
     */
    public String creer(MaladieDTO dto) {
        if (maladieRepository.findByLibelle(dto.getLibelle()).isPresent()) {
            return "Une maladie avec ce libellé existe déjà";
        }
        Maladie maladie = new Maladie();
        mapperDTOToEntity(dto, maladie);
        maladieRepository.save(maladie);
        return "Maladie créée avec succès";
    }

    /**
     * Modifie une maladie existante.
     */
    public String modifier(Long id, MaladieDTO dto) {
        Maladie maladie = maladieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maladie non trouvée"));
        mapperDTOToEntity(dto, maladie);
        maladie.setDateModification(LocalDateTime.now());
        maladieRepository.save(maladie);
        return "Maladie modifiée avec succès";
    }

    /**
     * Désactive une maladie.
     */
    public String desactiverMaladie(Long id) {
        Maladie maladie = maladieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maladie non trouvée"));
        maladie.setActif(false);
        maladie.setDateModification(LocalDateTime.now());
        maladieRepository.save(maladie);
        return "Maladie désactivée avec succès";
    }

    /**
     * Recherche des maladies par mot-clé.
     */
    public List<Maladie> rechercherMaladies(String motCle) {
        return maladieRepository.rechercherParMotCle(motCle);
    }

    /**
     * Récupère les maladies contagieuses.
     */
    public List<Maladie> findMaladiesContagieuses() {
        return maladieRepository.findMaladiesContagieuses();
    }

    /**
     * Obtient une maladie par ID.
     */
    public Maladie findById(Long id) {
        return maladieRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Maladie non trouvée"));
    }

    /**
     * Compte les maladies actives.
     */
    public long compterMaladiesActives() {
        return maladieRepository.findByActifTrue().size();
    }

    private void mapperDTOToEntity(MaladieDTO dto, Maladie maladie) {
        maladie.setLibelle(dto.getLibelle());
        maladie.setDescription(dto.getDescription());
        maladie.setSymptomes(dto.getSymptomes());
        maladie.setTraitement(dto.getTraitement());
        maladie.setDureTraitementJours(dto.getDureTraitementJours());
        maladie.setTauxMortalitePercent(dto.getTauxMortalitePercent());
        maladie.setContagieux(dto.getContagieux() != null ? dto.getContagieux() : false);
        if (dto.getActif() != null) {
            maladie.setActif(dto.getActif());
        }
    }
}
