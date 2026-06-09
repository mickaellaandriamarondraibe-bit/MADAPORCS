package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class EmployeDTO {
    private Long id;
    private String nom;
    private String prenom;
    private String contact;
    private String adresse;
    private Long posteEmployeId;
    private Long statutEmployeId;
    private LocalDate dateEmbauche;
    private BigDecimal salaireBase;
}
