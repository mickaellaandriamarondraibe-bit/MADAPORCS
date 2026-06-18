package com.madaporc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.Employe;


public interface EmployeRepository extends JpaRepository<Employe, Long> {
    List<String>findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
}
