package com.madaporc.repository;

    import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Utilisateur;

    public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {

        Optional<Utilisateur> findByEmail(String email);

    boolean existsByEmail(String email);
    }
