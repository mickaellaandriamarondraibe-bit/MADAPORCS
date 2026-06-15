package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.MouvementStockAliment;

    public interface MouvementStockAlimentRepository extends JpaRepository<MouvementStockAliment, Long> {

        List<MouvementStockAliment> findByIngredientId(Long ingredientId);

    List<MouvementStockAliment> findByTypeMouvementStockId(Long typeMouvementStockId);
    }
