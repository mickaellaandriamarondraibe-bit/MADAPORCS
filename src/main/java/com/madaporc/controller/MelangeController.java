package com.madaporc.controller;

import com.madaporc.DTO.MelangeDTO;
import com.madaporc.DTO.MelangeIngredientDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.Melange;
import com.madaporc.model.MelangeIngredient;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.MelangeRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MelangeController {

    private final MelangeRepository melangeRepository;
    private final IngredientRepository ingredientRepository;

    public MelangeController(MelangeRepository melangeRepository, IngredientRepository ingredientRepository) {
        this.melangeRepository = melangeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @GetMapping("/melanges")
    public String listMelanges(Model model) {
        model.addAttribute("melanges", melangeRepository.findAll());
        model.addAttribute("allIngredients", ingredientRepository.findAll());

        MelangeDTO dto = new MelangeDTO();
        dto.setIngredients(new ArrayList<>());
        dto.getIngredients().add(new MelangeIngredientDTO());
        model.addAttribute("melange", dto);

        return "ressources/melanges";
    }

    @GetMapping("/melanges/form")
    public String showForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        MelangeDTO dto = new MelangeDTO();
        dto.setIngredients(new ArrayList<>());

        if (id != null) {
            Optional<Melange> melangeOpt = melangeRepository.findById(id);
            if (melangeOpt.isPresent()) {
                Melange m = melangeOpt.get();
                dto.setId(m.getId());
                dto.setLibelle(m.getLibelle());
                dto.setDescription(m.getDescription());

                for (MelangeIngredient mi : m.getIngredients()) {
                    MelangeIngredientDTO miDto = new MelangeIngredientDTO();
                    miDto.setIngredientId(mi.getIngredient().getId());
                    miDto.setQuantiteKg(mi.getQuantiteKg());
                    miDto.setPourcentage(mi.getPourcentage());
                    dto.getIngredients().add(miDto);
                }
            }
        }

        if (dto.getIngredients().isEmpty()) {
            dto.getIngredients().add(new MelangeIngredientDTO());
        }

        model.addAttribute("melange", dto);
        model.addAttribute("melanges", melangeRepository.findAll());
        model.addAttribute("allIngredients", ingredientRepository.findAll());

        return "ressources/melanges";
    }

    @PostMapping("/melanges/save")
    public String saveMelange(@ModelAttribute MelangeDTO dto, Model model) {
        List<MelangeIngredientDTO> lignesValides = new ArrayList<>();
        
        // Nettoyage des lignes vides soumises par erreur
        if (dto.getIngredients() != null) {
            for (MelangeIngredientDTO miDto : dto.getIngredients()) {
                if (miDto.getIngredientId() != null) {
                    lignesValides.add(miDto);
                }
            }
        }

        if (lignesValides.isEmpty()) {
            model.addAttribute("message", "Le mélange doit contenir au moins un ingrédient.");
            return rechargerVueErreur(dto, model);
        }

        List<Long> vus = new ArrayList<>();
        double totalPourcentage = 0.0;
        double poidsTotal = 0.0;

        for (MelangeIngredientDTO miDto : lignesValides) {
            // RÈGLE 2 : Pas de doublons du même ingrédient
            if (vus.contains(miDto.getIngredientId())) {
                model.addAttribute("message", "Un ingrédient ne peut pas être configuré plusieurs fois dans le même mélange.");
                return rechargerVueErreur(dto, model);
            }
            vus.add(miDto.getIngredientId());
            totalPourcentage += (miDto.getPourcentage() != null ? miDto.getPourcentage().doubleValue() : 0.0);
            poidsTotal += (miDto.getQuantiteKg() != null ? miDto.getQuantiteKg().doubleValue() : 0.0);
        }

        if (totalPourcentage > 100.0) {
            model.addAttribute("message", "Le total des pourcentages ne peut pas dépasser 100%. (Actuel : " + totalPourcentage + "%)");
            return rechargerVueErreur(dto, model);
        }

        Melange melange;
        if (dto.getId() != null) {
            melange = melangeRepository.findById(dto.getId()).orElse(new Melange());
            if (melange.getIngredients() != null) {
                melange.getIngredients().clear(); // On purge pour recréer proprement la recette
            } else {
                melange.setIngredients(new ArrayList<>());
            }
        } else {
            melange = new Melange();
            melange.setIngredients(new ArrayList<>());
        }

        melange.setLibelle(dto.getLibelle());
        melange.setDescription(dto.getDescription());

        BigDecimal coutTotalRecette = BigDecimal.ZERO;

        for (MelangeIngredientDTO miDto : lignesValides) {
            Optional<Ingredient> ingOpt = ingredientRepository.findById(miDto.getIngredientId());
            if (ingOpt.isPresent()) {
                Ingredient ing = ingOpt.get();
                MelangeIngredient mi = new MelangeIngredient();
                mi.setMelange(melange);
                mi.setIngredient(ing);
                mi.setQuantiteKg(miDto.getQuantiteKg());
                mi.setPourcentage(miDto.getPourcentage());
                
                melange.getIngredients().add(mi);

                // Calcul du coût total pour cette ligne d'ingrédient
                if (miDto.getQuantiteKg() != null && ing.getPrixKg() != null) {
                    BigDecimal coutLigne = ing.getPrixKg().multiply(miDto.getQuantiteKg());
                    coutTotalRecette = coutTotalRecette.add(coutLigne);
                }
            }
        }

        if (poidsTotal > 0) {
            BigDecimal poidsTotalBd = BigDecimal.valueOf(poidsTotal);
            melange.setCoutRevientKg(coutTotalRecette.divide(poidsTotalBd, 2, RoundingMode.HALF_UP));
        } else {
            melange.setCoutRevientKg(BigDecimal.ZERO);
        }

        melangeRepository.save(melange);

        return "redirect:/melanges";
    }

    @PostMapping("/melanges/delete/{id}")
    public String deleteMelange(@PathVariable Long id) {
        if (melangeRepository.existsById(id)) {
            melangeRepository.deleteById(id);
        }
        return "redirect:/melanges";
    }

    private String rechargerVueErreur(MelangeDTO dto, Model model) {
        model.addAttribute("melange", dto);
        model.addAttribute("melanges", melangeRepository.findAll());
        model.addAttribute("allIngredients", ingredientRepository.findAll());
        return "ressources/melanges";
    }
}