package com.madaporc.repository;

    import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
    import com.madaporc.model.Vente;

    public interface VenteRepository extends JpaRepository<Vente, Long> {

        List<Vente> findAllByOrderByDateVenteDesc();

    List<Vente> findByClientIdOrderByDateVenteDesc(Long clientId);

    List<Vente> findByDateVenteBetween(LocalDateTime debut, LocalDateTime fin);

    @Query("select coalesce(sum(v.montantTotal),0) from Vente v where v.dateVente between :debut and :fin and v.statutVente <> 'Annulée'")
    BigDecimal sumMontantByDateBetween(@Param("debut") LocalDateTime debut, @Param("fin") LocalDateTime fin);
    }
