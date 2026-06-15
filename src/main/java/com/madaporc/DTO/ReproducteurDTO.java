package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class ReproducteurDTO {
        private Long id;

    private String codeReproducteur;

    private String nom;

    private Long raceId;

    private Long sexeId;

    private Long statutReproducteurId;

    private LocalDate dateNaissance;

    private LocalDate dateArrivee;

    private BigDecimal prixAchat;

    private BigDecimal poidsKg;

    private Integer nombreParite;

    private String observation;
    }
