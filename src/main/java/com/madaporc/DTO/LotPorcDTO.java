package com.madaporc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotPorcDTO {

    private Long id;
    private String codeLot;
    private String typeEntree;
    private Long raceId;
    private Long statutLotId;
    private Integer nombreInitial;
    private Integer nombreActuel;
    private Integer nombreMalesInitial;
    private Integer nombreFellesInitial;
    private Integer nombreMalesActuel;
    private Integer nombreFellesActuel;
    private Integer nombreMorts;
    private LocalDate dateNaissanceEstimee;
    private LocalDate dateAchat;
    private BigDecimal prixAchatTotal;
    private BigDecimal poidsMoyenInitialKg;
    private BigDecimal poidsMoyenActuelKg;
    private String observation;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime archivedAt;
}