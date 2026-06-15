package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDateTime;
    import lombok.Data;

    @Data
    public class PaiementDTO {
        private Long id;

    private Long venteId;

    private BigDecimal montant;

    private String modePaiement;

    private String reference;

    private LocalDateTime datePaiement;
    }
