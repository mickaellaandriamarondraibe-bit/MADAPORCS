package com.madaporc.repository;

import com.madaporc.model.Client;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findByNomContainingIgnoreCaseOrEmailContainingIgnoreCaseOrContactContainingIgnoreCase(
            String nom,
            String email,
            String contact
    );

    List<Client> findByTypeClient(String typeClient);
}
