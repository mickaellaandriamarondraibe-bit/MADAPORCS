package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.DistributionAliment;

    public interface DistributionAlimentRepository extends JpaRepository<DistributionAliment, Long> {

        List<DistributionAliment> findAllByOrderByDateDistributionDesc();

    List<DistributionAliment> findByLotPorcId(Long lotPorcId);
    }
