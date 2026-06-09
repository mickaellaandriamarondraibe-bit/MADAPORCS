package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class PaiementDTO {
    private Long id;
    private Long venteId;
    private BigDecimal montant;
    private String modePaiement;
    private String reference;
    private LocalDateTime datePaiement;
}
