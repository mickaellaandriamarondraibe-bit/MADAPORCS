package com.madaporc.repository;

import com.madaporc.model.Vaccination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationRepository extends JpaRepository<Vaccination, Long> {

    @Query("""
        SELECT v
        FROM Vaccination v
        JOIN FETCH v.lot
        JOIN FETCH v.vaccin
        ORDER BY v.dateVaccination DESC
    """)
    List<Vaccination> findAllWithDetails();

    @Query("""
    SELECT v
    FROM Vaccination v
    JOIN FETCH v.lot
    JOIN FETCH v.vaccin
    WHERE v.dateRappel BETWEEN :debut AND :fin
    ORDER BY v.dateRappel ASC
""")
    List<Vaccination> findByDateRappelBetweenOrderByDateRappelAsc(
        @Param("debut") LocalDate debut,
        @Param("fin") LocalDate fin
    );
}