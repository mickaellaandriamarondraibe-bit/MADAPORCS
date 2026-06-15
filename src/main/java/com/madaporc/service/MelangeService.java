package com.madaporc.service;

import java.math.*;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.stereotype.Service;
import com.madaporc.DTO.*;
import com.madaporc.model.*;
import com.madaporc.repository.*;

@Service
public class MelangeService {
    private final MelangeRepository repo;
    private final MelangeIngredientRepository miRepo;
    private final IngredientRepository ingRepo;

    public MelangeService(MelangeRepository repo, MelangeIngredientRepository miRepo, IngredientRepository ingRepo) {
        this.repo = repo;
        this.miRepo = miRepo;
        this.ingRepo = ingRepo;
    }

    public List<Melange> findAllMelanges() {
        return repo.findAll();
    }

    public String creer(MelangeDTO dto) {
        Melange m = new Melange();
        m.setLibelle(dto.getLibelle());
        m.setDescription(dto.getDescription());
        m.setCreatedAt(LocalDateTime.now());
        m = repo.save(m);
        if (dto.getIngredients() != null)
            for (MelangeIngredientDTO i : dto.getIngredients())
                ajouterIngredientDansMelange(m.getId(), i.getIngredientId(), i.getQuantiteKg(), i.getPourcentage());
        m.setCoutKg(calculerCoutMelange(m.getId()));
        repo.save(m);
        return null;
    }

    public String modifier(Long id, MelangeDTO dto) {
        Melange m = repo.findById(id).orElse(null);
        if (m == null)
            return "Mélange introuvable.";
        m.setLibelle(dto.getLibelle());
        m.setDescription(dto.getDescription());
        repo.save(m);
        return null;
    }

    public String ajouterIngredientDansMelange(Long melangeId, Long ingredientId, BigDecimal q, BigDecimal p) {
        if (miRepo.existsByMelangeIdAndIngredientId(melangeId, ingredientId))
            return "Ingrédient déjà présent.";
        MelangeIngredient mi = new MelangeIngredient();
        mi.setMelangeId(melangeId);
        mi.setIngredientId(ingredientId);
        mi.setQuantiteKg(q);
        mi.setPourcentage(p);
        miRepo.save(mi);
        return null;
    }

    public BigDecimal calculerCoutMelange(Long id) {
        BigDecimal total = BigDecimal.ZERO;
        for (MelangeIngredient mi : miRepo.findByMelangeId(id)) {
            Ingredient ing = ingRepo.findById(mi.getIngredientId()).orElse(null);
            if (ing != null && mi.getQuantiteKg() != null)
                total = total.add(ing.getPrixKg().multiply(mi.getQuantiteKg()));
        }
        return total;
    }

    public String verifierTotalPourcentage(List<MelangeIngredientDTO> ingredients) {
        BigDecimal t = BigDecimal.ZERO;
        if (ingredients != null)
            for (MelangeIngredientDTO i : ingredients)
                if (i.getPourcentage() != null)
                    t = t.add(i.getPourcentage());
        return t.compareTo(BigDecimal.valueOf(100)) > 0 ? "Total pourcentage > 100%." : null;
    }

    public MelangeDetailDTO getDetailMelange(Long id) {
        MelangeDetailDTO d = new MelangeDetailDTO();
        repo.findById(id).ifPresent(m -> {
            d.setId(m.getId());
            d.setLibelle(m.getLibelle());
            d.setCoutKg(m.getCoutKg());
        });
        return d;
    }
}
