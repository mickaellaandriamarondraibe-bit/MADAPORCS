package com.madaporc.repository;

    import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
    import com.madaporc.model.Client;

    public interface ClientRepository extends JpaRepository<Client, Long> {

        List<Client> findByNomContainingIgnoreCase(String nom);

    List<Client> findByTypeClient(String typeClient);
    }
