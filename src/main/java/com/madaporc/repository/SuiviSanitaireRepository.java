package com.madaporc.repository;

    import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.SuiviSanitaire;

    public interface SuiviSanitaireRepository extends JpaRepository<SuiviSanitaire, Long> {

        List<SuiviSanitaire> findByStatutSuiviSanitaireId(Long statutSuiviSanitaireId);

    List<SuiviSanitaire> findByLotPorcId(Long lotPorcId);

    List<SuiviSanitaire> findByReproducteurId(Long reproducteurId);

    List<SuiviSanitaire> findByDateDiagnosticBetween(LocalDate debut, LocalDate fin);
    }
