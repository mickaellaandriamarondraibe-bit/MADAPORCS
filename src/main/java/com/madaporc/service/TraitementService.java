package com.madaporc.service;

import com.madaporc.DTO.TraitementDTO;
import com.madaporc.model.Traitement;
import com.madaporc.model.Maladie;
import com.madaporc.repository.TraitementRepository;
import com.madaporc.repository.MaladieRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service pour la gestion des traitements.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TraitementService {

    private final TraitementRepository traitementRepository;
    private final MaladieRepository maladieRepository;

    /**
     * Récupère tous les traitements actifs.
     */
    public List<Traitement> findAllTraitementsActifs() {
        return traitementRepository.findByActifTrue();
    }

    /**
     * Récupère tous les traitements.
     */
    public List<Traitement> findAllTraitements() {
        return traitementRepository.findAll();
    }

    /**
     * Crée un nouveau traitement.
     */
    public String creer(TraitementDTO dto) {
        Traitement traitement = new Traitement();
        mapperDTOToEntity(dto, traitement);
        traitementRepository.save(traitement);
        return "Traitement créé avec succès";
    }

    /**
     * Modifie un traitement existant.
     */
    public String modifier(Long id, TraitementDTO dto) {
        Traitement traitement = traitementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traitement non trouvé"));
        mapperDTOToEntity(dto, traitement);
        traitement.setDateModification(LocalDateTime.now());
        traitementRepository.save(traitement);
        return "Traitement modifié avec succès";
    }

    /**
     * Désactive un traitement.
     */
    public String desactiverTraitement(Long id) {
        Traitement traitement = traitementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traitement non trouvé"));
        traitement.setActif(false);
        traitement.setDateModification(LocalDateTime.now());
        traitementRepository.save(traitement);
        return "Traitement désactivé avec succès";
    }

    /**
     * Recherche des traitements par mot-clé.
     */
    public List<Traitement> rechercherTraitements(String motCle) {
        return traitementRepository.rechercherParMotCle(motCle);
    }

    /**
     * Récupère les traitements pour une maladie donnée.
     */
    public List<Traitement> findTraitementsByMaladie(Long maladieId) {
        return traitementRepository.findByMaladieId(maladieId);
    }

    /**
     * Calcule le coût total des traitements.
     */
    public BigDecimal calculerCoutTotalTraitements() {
        return traitementRepository.findByActifTrue().stream()
            .map(t -> t.getPrixUnite() != null ? t.getPrixUnite() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Obtient un traitement par ID.
     */
    public Traitement findById(Long id) {
        return traitementRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Traitement non trouvé"));
    }

    /**
     * Compte les traitements actifs.
     */
    public long compterTraitementsActifs() {
        return traitementRepository.findByActifTrue().size();
    }

    private void mapperDTOToEntity(TraitementDTO dto, Traitement traitement) {
        traitement.setLibelle(dto.getLibelle());
        traitement.setDescription(dto.getDescription());
        
        if (dto.getMaladieId() != null) {
            Maladie maladie = maladieRepository.findById(dto.getMaladieId())
                .orElseThrow(() -> new RuntimeException("Maladie non trouvée"));
            traitement.setMaladie(maladie);
        }
        
        traitement.setPrincipe(dto.getPrincipe());
        traitement.setDosageMl(dto.getDosageMl());
        traitement.setFrequenceJours(dto.getFrequenceJours());
        traitement.setPrixUnite(dto.getPrixUnite());
        traitement.setNombreJoursTraitement(dto.getNombreJoursTraitement());
        
        if (dto.getActif() != null) {
            traitement.setActif(dto.getActif());
        }
    }
}
