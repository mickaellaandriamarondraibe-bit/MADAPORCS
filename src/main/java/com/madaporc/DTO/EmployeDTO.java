package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
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
