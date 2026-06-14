package com.madaporc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LotPorcDTO {

    private Long id;
    private String typeEntree;
    private String codeLot;
    private Long raceId;
    private Long statutLotId;
    private Integer nombreMalesInitial;
    private Integer nombreFellesInitial;
    private Integer nombreInitial;
    private Integer nombreActuel;
    private Integer nombreMorts;
    private LocalDate dateNaissanceEstimee;
    private LocalDate dateAchat;
    private BigDecimal prixAchatTotal;
    private BigDecimal poidsMoyenInitialKg;
    private String observation;
}