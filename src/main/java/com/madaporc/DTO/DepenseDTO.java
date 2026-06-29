package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepenseDTO {
    private Long id;
    private Long categorieId;
    private LocalDate dateDepense;
    private BigDecimal montant;
    private String description;
}
