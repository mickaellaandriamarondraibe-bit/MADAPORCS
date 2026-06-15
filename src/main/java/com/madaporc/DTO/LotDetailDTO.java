package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class LotDetailDTO {
    private Long id;

private String codeLot;

private Integer nombreActuel;

private BigDecimal tauxMortalite;

private BigDecimal gmqMoyen;
}
