package com.madaporc.repository;

    import java.util.List;
    import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.EvenementReproduction;

    public interface EvenementReproductionRepository extends JpaRepository<EvenementReproduction, Long> {

        List<EvenementReproduction> findByTypeEvenementReproductionId(Long typeEvenementReproductionId);

    List<EvenementReproduction> findByFemelleIdOrMaleIdOrderByDateEvenementDesc(Long femelleId, Long maleId);
    }
