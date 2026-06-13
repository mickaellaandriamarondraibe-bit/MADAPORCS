package com.madaporc.controller;

import com.madaporc.DTO.MouvementStockDTO;
import com.madaporc.model.Ingredient;
import com.madaporc.model.MouvementStockAliment;
import com.madaporc.model.TypeMouvementStock;
import com.madaporc.repository.IngredientRepository;
import com.madaporc.repository.MouvementStockAlimentRepository;
import com.madaporc.repository.TypeMouvementStockRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class MouvementStockController {

    private final MouvementStockAlimentRepository mouvementStockRepository;
    private final IngredientRepository ingredientRepository;
    private final TypeMouvementStockRepository typeMouvementStockRepository;

    public MouvementStockController(
            MouvementStockAlimentRepository mouvementStockRepository,
            IngredientRepository ingredientRepository,
            TypeMouvementStockRepository typeMouvementStockRepository) {

        this.mouvementStockRepository = mouvementStockRepository;
        this.ingredientRepository = ingredientRepository;
        this.typeMouvementStockRepository = typeMouvementStockRepository;
    }

    @GetMapping("/stocks/mouvements")
    public String listMouvements(Model model) {
        model.addAttribute("mouvements", mouvementStockRepository.findAll());
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("types", typeMouvementStockRepository.findAll());

        MouvementStockDTO dto = new MouvementStockDTO();
        dto.setDateMouvement(LocalDateTime.now());
        model.addAttribute("mouvement", dto);

        return "ressources/mouvementsStock";
    }

    @GetMapping("/stocks/mouvements/form")
    public String showForm(@RequestParam(value = "id", required = false) Long id, Model model) {
        MouvementStockDTO dto = new MouvementStockDTO();

        if (id != null) {
            Optional<MouvementStockAliment> mouvementOpt = mouvementStockRepository.findById(id);
            if (mouvementOpt.isPresent()) {
                MouvementStockAliment m = mouvementOpt.get();
                dto.setId(m.getId());
                dto.setIngredientId(m.getIngredient().getId());
                dto.setTypeMouvementStockId(m.getTypeMouvementStock().getId());
                dto.setQuantiteKg(m.getQuantiteKg());
                dto.setPrixTotal(m.getPrixTotal());
                dto.setDateMouvement(m.getDateMouvement());
                dto.setMotif(m.getMotif());
            }
        } else {
            dto.setDateMouvement(LocalDateTime.now());
        }

        model.addAttribute("mouvement", dto);
        model.addAttribute("ingredients", ingredientRepository.findAll());
        model.addAttribute("types", typeMouvementStockRepository.findAll());

        model.addAttribute("mouvements", mouvementStockRepository.findAll());

        return "ressources/mouvementsStock";
    }

    @PostMapping("/stocks/mouvements/save")
    public String saveMouvement(@ModelAttribute MouvementStockDTO dto, Model model) {

        Optional<Ingredient> ingredientOpt = ingredientRepository.findById(dto.getIngredientId());
        Optional<TypeMouvementStock> typeOpt = typeMouvementStockRepository.findById(dto.getTypeMouvementStockId());

        if (ingredientOpt.isEmpty() || typeOpt.isEmpty()) {
            model.addAttribute("message", "Ingredient ou type de mouvement introuvable.");
            model.addAttribute("mouvement", dto);
            model.addAttribute("ingredients", ingredientRepository.findAll());
            model.addAttribute("types", typeMouvementStockRepository.findAll());
            
            // CORRECTION 2 (Suite) : Si on retourne la vue à cause d'une erreur, il faut aussi le tableau  model.addAttribute("mouvements", mouvementStockRepository.findAll());

            return "ressources/mouvementsStock";
        }

        MouvementStockAliment mouvement;

        if (dto.getId() != null) {
            mouvement = mouvementStockRepository.findById(dto.getId()).orElse(new MouvementStockAliment());
        } else {
            mouvement = new MouvementStockAliment();
        }

        mouvement.setIngredient(ingredientOpt.get());
        mouvement.setTypeMouvementStock(typeOpt.get());
        mouvement.setQuantiteKg(dto.getQuantiteKg());
        mouvement.setPrixTotal(dto.getPrixTotal());
        mouvement.setDateMouvement(dto.getDateMouvement() != null ? dto.getDateMouvement() : LocalDateTime.now());
        mouvement.setMotif(dto.getMotif());

        mouvementStockRepository.save(mouvement);

        return "redirect:/stocks/mouvements";
    }

    @PostMapping("/stocks/mouvements/delete/{id}")
    public String deleteMouvement(@PathVariable Long id) {

        if (mouvementStockRepository.existsById(id)) {
            mouvementStockRepository.deleteById(id);
        }

        return "redirect:/stocks/mouvements";
    }
}