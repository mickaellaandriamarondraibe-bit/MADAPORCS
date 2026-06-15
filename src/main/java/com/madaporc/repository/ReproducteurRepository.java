package com.madaporc.repository;

    import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Reproducteur;

    public interface ReproducteurRepository extends JpaRepository<Reproducteur, Long> {

        List<Reproducteur> findByCodeReproducteurContainingIgnoreCaseOrNomContainingIgnoreCase(String code, String nom);

    Optional<Reproducteur> findByCodeReproducteur(String codeReproducteur);

    boolean existsByCodeReproducteur(String codeReproducteur);

    List<Reproducteur> findBySexeId(Long sexeId);

    long countByStatutReproducteurId(Long statutReproducteurId);
    }
