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
public class DepenseDTO {
    private Long id;
    private Long categorieDepenseId;
    private String libelle;
    private BigDecimal montant;
    private LocalDate dateDepense;
    private String description;
}
