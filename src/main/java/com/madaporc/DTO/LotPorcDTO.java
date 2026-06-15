package com.madaporc.DTO;

    import java.math.BigDecimal;
import java.time.LocalDate;
    import lombok.Data;

    @Data
    public class LotPorcDTO {
        private Long id;

    private String codeLot;

    private String typeEntree;

    private Long raceId;

    private Long statutLotId;

    private Integer nombreInitial;

    private Integer nombreActuel;

    private Integer nombreMalesInitial;

    private Integer nombreFemellesInitial;

    private Integer nombreMalesActuel;

    private Integer nombreFemellesActuel;

    private Integer nombreMorts;

    private LocalDate dateNaissanceEstimee;

    private LocalDate dateAchat;

    private BigDecimal prixAchatTotal;

    private BigDecimal poidsMoyenInitialKg;

    private BigDecimal poidsMoyenActuelKg;

    private String observation;
    }
