package com.madaporc.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.TraitementDTO;
import com.madaporc.model.Traitement;
import com.madaporc.repository.MaladieRepository;
import com.madaporc.repository.TraitementRepository;

@Service
public class TraitementService {

    private final TraitementRepository traitementRepository;
    private final MaladieRepository maladieRepository;

    public TraitementService(
            TraitementRepository traitementRepository,
            MaladieRepository maladieRepository
    ) {
        this.traitementRepository = traitementRepository;
        this.maladieRepository = maladieRepository;
    }

    public List<Traitement> rechercherTraitements(Long maladieId, String motCle) {
        if (maladieId != null) {
            return traitementRepository.findByMaladieId(maladieId);
        }

        if (motCle != null && !motCle.isBlank()) {
            return traitementRepository.findByLibelleContainingIgnoreCase(motCle);
        }

        return traitementRepository.findAll();
    }

    public void prepareTraitementFormModel(Model model, Long id) {
        TraitementDTO dto = new TraitementDTO();

        if (id != null) {
            traitementRepository.findById(id).ifPresent(traitement -> {
                dto.setId(traitement.getId());
                dto.setMaladieId(traitement.getMaladieId());
                dto.setLibelle(traitement.getLibelle());
                dto.setPrix(traitement.getPrix());
                dto.setDureeGuerisonJours(traitement.getDureeGuerisonJours());
                dto.setDescription(traitement.getDescription());
            });
        }

        model.addAttribute("traitement", dto);
        model.addAttribute("maladies", maladieRepository.findAll());
    }

    public String creer(TraitementDTO dto) {
        String erreur = validerTraitement(dto);

        if (erreur != null) {
            return erreur;
        }

        Traitement traitement = convertirDtoVersEntity(dto);

        traitementRepository.save(traitement);

        return null;
    }

    public String modifier(Long id, TraitementDTO dto) {
        if (id == null) {
            return "Identifiant traitement invalide.";
        }

        if (!traitementRepository.existsById(id)) {
            return "Traitement introuvable.";
        }

        String erreur = validerTraitement(dto);

        if (erreur != null) {
            return erreur;
        }

        Traitement traitement = convertirDtoVersEntity(dto);
        traitement.setId(id);

        traitementRepository.save(traitement);

        return null;
    }

    public String desactiverTraitement(Long id) {
        Traitement traitement = traitementRepository.findById(id).orElse(null);

        if (traitement == null) {
            return "Traitement introuvable.";
        }

        traitementRepository.delete(traitement);

        return null;
    }

    public String validerTraitement(TraitementDTO dto) {
        if (dto == null) {
            return "Traitement obligatoire.";
        }

        if (dto.getMaladieId() == null) {
            return "Maladie obligatoire.";
        }

        if (!maladieRepository.existsById(dto.getMaladieId())) {
            return "Maladie introuvable.";
        }

        if (dto.getLibelle() == null || dto.getLibelle().isBlank()) {
            return "Libellé obligatoire.";
        }

        if (dto.getPrix() != null && dto.getPrix().compareTo(BigDecimal.ZERO) < 0) {
            return "Le prix ne peut pas être négatif.";
        }

        if (dto.getDureeGuerisonJours() != null && dto.getDureeGuerisonJours() < 0) {
            return "La durée de guérison ne peut pas être négative.";
        }

        return null;
    }

    private Traitement convertirDtoVersEntity(TraitementDTO dto) {
        Traitement traitement = new Traitement();

        traitement.setId(dto.getId());
        traitement.setMaladieId(dto.getMaladieId());
        traitement.setLibelle(dto.getLibelle());
        traitement.setPrix(dto.getPrix());
        traitement.setDureeGuerisonJours(dto.getDureeGuerisonJours());
        traitement.setDescription(dto.getDescription());

        return traitement;
    }
}
