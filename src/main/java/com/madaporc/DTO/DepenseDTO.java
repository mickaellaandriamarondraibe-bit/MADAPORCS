package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class DepenseDTO {
        private Long id;

    private Long categorieDepenseId;

    private String libelle;

    private BigDecimal montant;

    private LocalDate dateDepense;

    private String description;
    }
