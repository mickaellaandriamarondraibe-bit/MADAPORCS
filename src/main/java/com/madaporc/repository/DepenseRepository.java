package com.madaporc.repository;

    import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
    import com.madaporc.model.Depense;

    public interface DepenseRepository extends JpaRepository<Depense, Long> {

        List<Depense> findByDateDepenseBetween(LocalDate debut, LocalDate fin);

    List<Depense> findByCategorieDepenseId(Long categorieDepenseId);

    @Query("select coalesce(sum(d.montant),0) from Depense d where d.dateDepense between :debut and :fin")
    BigDecimal sumMontantByDateBetween(@Param("debut") LocalDate debut, @Param("fin") LocalDate fin);
    }
