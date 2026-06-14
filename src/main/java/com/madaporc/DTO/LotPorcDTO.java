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
public class LotPorcDTO {
    private Long id;
    private String typeEntree;
    private String codeLot;
    private Long raceId;
    private Long statutLotId;
    private Integer nombreMalesInitial;
    private Integer nombreFemellesInitial;
    private Integer nombreInitial;
    private Integer nombreActuel;
    private LocalDate dateNaissanceEstimee;
    private LocalDate dateAchat;
    private BigDecimal prixAchatTotal;
    private BigDecimal poidsMoyenInitialKg;
    private String observation;
}
