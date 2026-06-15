package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.MelangeIngredient;

    public interface MelangeIngredientRepository extends JpaRepository<MelangeIngredient, Long> {

        List<MelangeIngredient> findByMelangeId(Long melangeId);

    boolean existsByMelangeIdAndIngredientId(Long melangeId, Long ingredientId);
    }
