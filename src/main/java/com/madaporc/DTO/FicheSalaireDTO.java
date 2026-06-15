package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class FicheSalaireDTO {
    private Long salaireId;

private String nomEmploye;

private BigDecimal montantNet;
}
