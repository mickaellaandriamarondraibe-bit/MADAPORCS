package com.madaporc.repository;

    import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
    import com.madaporc.model.CycleProduction;

    public interface CycleProductionRepository extends JpaRepository<CycleProduction, Long> {

        List<CycleProduction> findAllByOrderByDateDebutDesc();

    boolean existsByCodeCycle(String codeCycle);

    List<CycleProduction> findByDateDebutBetween(LocalDate debut, LocalDate fin);

    @Query("select coalesce(sum(c.nombreVendables),0) from CycleProduction c where c.statutCycle <> 'Clôturé'")
    Integer sumNombreVendablesActifs();
    }
