package com.madaporc.DTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Data;

@Data
public class LotDetailDTO {
    private Long id;

    private String codeLot;

    private String typeEntree;

    private Long raceId;

    private String raceLibelle;

    private Long statutLotId;

    private String statutLibelle;

    private Integer nombreInitial;

    private Integer nombreActuel;

    private Integer nombreMalesInitial;

    private Integer nombreFemellesInitial;

    private Integer nombreMorts;

    private Integer nombreMalesMorts;

    private Integer nombreFemellesMortes;

    private LocalDate dateNaissanceEstimee;

    private LocalDate dateAchat;

    private BigDecimal prixAchatTotal;

    private BigDecimal poidsMoyenInitialKg;

    private BigDecimal poidsMoyenActuelKg;

    private String observation;

    private BigDecimal tauxMortalite;

    private BigDecimal gmqMoyen;
}
