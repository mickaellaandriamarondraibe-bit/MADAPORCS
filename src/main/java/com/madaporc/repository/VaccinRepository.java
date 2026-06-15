package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Vaccin;

    public interface VaccinRepository extends JpaRepository<Vaccin, Long> {

        List<Vaccin> findByActifTrue();
    }
