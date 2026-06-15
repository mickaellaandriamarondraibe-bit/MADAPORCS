package com.madaporc.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.madaporc.DTO.MouvementStockDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.MouvementStockAliment;
import com.madaporc.model.TypeMouvementStock;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.MouvementStockAlimentRepository;
import com.madaporc.repository.TypeMouvementStockRepository;

@Service
public class MouvementStockService {

    private final MouvementStockAlimentRepository mouvementStockRepository;
    private final IngredientRepository ingredientRepository;
    private final TypeMouvementStockRepository typeMouvementStockRepository;

    public MouvementStockService(
            MouvementStockAlimentRepository mouvementStockRepository,
            IngredientRepository ingredientRepository,
            TypeMouvementStockRepository typeMouvementStockRepository
    ) {
        this.mouvementStockRepository = mouvementStockRepository;
        this.ingredientRepository = ingredientRepository;
        this.typeMouvementStockRepository = typeMouvementStockRepository;
    }

    public void prepareListeModel(
            Model model,
            Long ingredientId,
            Long typeId,
            String debut,
            String fin
    ) {
        List<MouvementStockAliment> mouvements;

        if (ingredientId != null) {
            mouvements = mouvementStockRepository.findByIngredientId(ingredientId);
        } else if (typeId != null) {
            mouvements = mouvementStockRepository.findByTypeMouvementStockId(typeId);
        } else {
            mouvements = mouvementStockRepository.findAll();
        }

        model.addAttribute("mouvements", mouvements);
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("types", typeMouvementStockRepository.findAll());
    }

    public void prepareFormModel(Model model, Long id) {
        MouvementStockDTO dto = new MouvementStockDTO();

        if (id != null) {
            mouvementStockRepository.findById(id).ifPresent(mouvement -> {
                dto.setId(mouvement.getId());
                dto.setIngredientId(mouvement.getIngredientId());
                dto.setTypeMouvementStockId(mouvement.getTypeMouvementStockId());
                dto.setQuantiteKg(mouvement.getQuantiteKg());
                dto.setPrixTotal(mouvement.getPrixTotal());
                dto.setDateMouvement(mouvement.getDateMouvement());
                dto.setMotif(mouvement.getMotif());
            });
        }

        model.addAttribute("mouvement", dto);
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("types", typeMouvementStockRepository.findAll());
    }

    public Optional<MouvementStockAliment> findById(Long id) {
        return mouvementStockRepository.findById(id);
    }

    public String ajouterMouvement(MouvementStockDTO dto, Long utilisateurId) {
        String erreur = validerMouvementStock(dto);

        if (erreur != null) {
            return erreur;
        }

        erreur = verifierStockAvantSortie(
                dto.getIngredientId(),
                dto.getQuantiteKg(),
                dto.getTypeMouvementStockId()
        );

        if (erreur != null) {
            return erreur;
        }

        MouvementStockAliment mouvement = new MouvementStockAliment();

        mouvement.setId(dto.getId());
        mouvement.setIngredientId(dto.getIngredientId());
        mouvement.setTypeMouvementStockId(dto.getTypeMouvementStockId());
        mouvement.setQuantiteKg(dto.getQuantiteKg());
        mouvement.setPrixTotal(calculerPrixTotal(dto.getIngredientId(), dto.getQuantiteKg()));
        mouvement.setDateMouvement(dto.getDateMouvement() == null ? LocalDateTime.now() : dto.getDateMouvement());
        mouvement.setMotif(dto.getMotif());
        mouvement.setCreatedBy(utilisateurId);
        mouvement.setCreatedAt(LocalDateTime.now());

        mouvementStockRepository.save(mouvement);

        mettreAJourStockIngredient(
                dto.getIngredientId(),
                dto.getQuantiteKg(),
                dto.getTypeMouvementStockId()
        );

        return null;
    }

    public String validerMouvementStock(MouvementStockDTO dto) {
        if (dto == null) {
            return "Mouvement obligatoire.";
        }

        if (dto.getIngredientId() == null) {
            return "Ingrédient obligatoire.";
        }

        if (!ingredientRepository.existsById(dto.getIngredientId())) {
            return "Ingrédient introuvable.";
        }

        if (dto.getTypeMouvementStockId() == null) {
            return "Type de mouvement obligatoire.";
        }

        if (!typeMouvementStockRepository.existsById(dto.getTypeMouvementStockId())) {
            return "Type de mouvement introuvable.";
        }

        if (dto.getQuantiteKg() == null || dto.getQuantiteKg().compareTo(BigDecimal.ZERO) <= 0) {
            return "Quantité invalide.";
        }

        return null;
    }

    public String verifierStockAvantSortie(
            Long ingredientId,
            BigDecimal quantiteKg,
            Long typeMouvementStockId
    ) {
        TypeMouvementStock type = typeMouvementStockRepository.findById(typeMouvementStockId).orElse(null);

        if (type == null || type.getLibelle() == null) {
            return null;
        }

        String libelle = type.getLibelle().toLowerCase();

        boolean sortie = libelle.contains("sortie")
                || libelle.contains("distribution")
                || libelle.contains("perte");

        if (!sortie) {
            return null;
        }

        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient == null) {
            return "Ingrédient introuvable.";
        }

        BigDecimal stockActuel = nz(ingredient.getStockActuelKg());

        if (stockActuel.compareTo(quantiteKg) < 0) {
            return "Stock insuffisant.";
        }

        return null;
    }

    public void mettreAJourStockIngredient(
            Long ingredientId,
            BigDecimal quantiteKg,
            Long typeMouvementStockId
    ) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);
        TypeMouvementStock type = typeMouvementStockRepository.findById(typeMouvementStockId).orElse(null);

        if (ingredient == null || type == null || type.getLibelle() == null) {
            return;
        }

        BigDecimal stockActuel = nz(ingredient.getStockActuelKg());
        String libelle = type.getLibelle().toLowerCase();

        if (libelle.contains("entrée") || libelle.contains("entree") || libelle.contains("achat")) {
            ingredient.setStockActuelKg(stockActuel.add(quantiteKg));
        } else {
            ingredient.setStockActuelKg(stockActuel.subtract(quantiteKg));
        }

        ingredientRepository.save(ingredient);
    }

    public BigDecimal calculerPrixTotal(Long ingredientId, BigDecimal quantiteKg) {
        Ingredient ingredient = ingredientRepository.findById(ingredientId).orElse(null);

        if (ingredient == null) {
            return BigDecimal.ZERO;
        }

        return nz(ingredient.getPrixKg()).multiply(nz(quantiteKg));
    }

    private BigDecimal nz(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}