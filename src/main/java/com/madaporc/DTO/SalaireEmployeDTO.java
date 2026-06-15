package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class SalaireEmployeDTO {
        private Long id;

    private Long employeId;

    private Integer mois;

    private Integer annee;

    private BigDecimal montantBase;

    private BigDecimal prime;

    private BigDecimal retenue;

    private BigDecimal montantNet;

    private String statutPaiement;

    private LocalDate datePaiement;
    }
