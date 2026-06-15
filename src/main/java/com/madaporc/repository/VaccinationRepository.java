package com.madaporc.repository;

    import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Vaccination;

    public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

        List<Vaccination> findByLotPorcId(Long lotPorcId);

    List<Vaccination> findByReproducteurId(Long reproducteurId);

    List<Vaccination> findByDateRappelBetween(LocalDate debut, LocalDate fin);

    long countByDateRappelBefore(LocalDate dateLimite);
    }
