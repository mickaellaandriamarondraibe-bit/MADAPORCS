package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.SalaireEmploye;

    public interface SalaireEmployeRepository extends JpaRepository<SalaireEmploye, Long> {

        List<SalaireEmploye> findByMoisAndAnnee(Integer mois, Integer annee);

    boolean existsByEmployeIdAndMoisAndAnnee(Long employeId, Integer mois, Integer annee);
    }
