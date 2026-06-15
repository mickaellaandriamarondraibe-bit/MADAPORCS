package com.madaporc.repository;

    import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Presence;

    public interface PresenceRepository extends JpaRepository<Presence, Long> {

        List<Presence> findByDatePresence(LocalDate datePresence);

    boolean existsByEmployeIdAndDatePresence(Long employeId, LocalDate datePresence);

    List<Presence> findByEmployeIdAndDatePresenceBetween(Long employeId, LocalDate debut, LocalDate fin);

    List<Presence> findByDatePresenceBetween(LocalDate debut, LocalDate fin);

    long countByDatePresence(LocalDate datePresence);
    }
