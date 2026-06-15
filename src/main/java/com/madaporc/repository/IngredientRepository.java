package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
    import com.madaporc.model.Ingredient;

    public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

        List<Ingredient> findByLibelleContainingIgnoreCase(String libelle);

    List<Ingredient> findByActifTrue();

    @Query("select i from Ingredient i where i.stockActuelKg <= i.seuilMinKg")
    List<Ingredient> findStocksFaibles();
    }
