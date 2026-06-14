package com.madaporc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
    private String createdByLibelle;
    private LocalDateTime createdAt;
    private LocalDateTime archivedAt;
    private List<Object> mouvements;
    private List<Object> pesees;
    private List<Object> suivisSanitaires;
    private List<Object> vaccinations;
    private List<Object> distributions;
    private BigDecimal tauxMortalite;
    private BigDecimal gmqMoyen;
}