package com.madaporc.repository;

import com.madaporc.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllByOrderByNomAsc();

    boolean existsByNomIgnoreCase(String nom);
}
