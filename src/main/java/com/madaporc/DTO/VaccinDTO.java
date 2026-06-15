package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class VaccinDTO {
    private Long id;

private String libelle;

private BigDecimal prix;

private Integer delaiRappelJours;

private String description;

private Boolean actif;
}
