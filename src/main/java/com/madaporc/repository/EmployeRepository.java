package com.madaporc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.madaporc.model.Employe;


public interface EmployeRepository extends JpaRepository<Employe, Long> {
}
