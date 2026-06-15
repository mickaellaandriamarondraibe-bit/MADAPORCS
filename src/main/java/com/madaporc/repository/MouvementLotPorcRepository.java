package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.MouvementLotPorc;

    public interface MouvementLotPorcRepository extends JpaRepository<MouvementLotPorc, Long> {

        List<MouvementLotPorc> findByLotPorcIdOrderByDateMouvementDesc(Long lotPorcId);
    }
