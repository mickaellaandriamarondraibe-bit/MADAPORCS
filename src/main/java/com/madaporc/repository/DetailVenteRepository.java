package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.DetailVente;

    public interface DetailVenteRepository extends JpaRepository<DetailVente, Long> {

        List<DetailVente> findByVenteId(Long venteId);

    List<DetailVente> findByLotPorcId(Long lotPorcId);
    }
