package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.PeseeLot;

    public interface PeseeLotRepository extends JpaRepository<PeseeLot, Long> {

        List<PeseeLot> findByLotPorcIdOrderByDatePeseeAsc(Long lotPorcId);
    }
