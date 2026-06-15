package com.madaporc.repository;

    import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Facture;

    public interface FactureRepository extends JpaRepository<Facture, Long> {

        Optional<Facture> findByVenteId(Long venteId);

    boolean existsByNumeroFacture(String numeroFacture);
    }
