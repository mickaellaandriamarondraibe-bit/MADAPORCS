package com.madaporc.DTO;

import java.time.LocalDateTime;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class MouvementLotDTO {
    private Long id;
    private Long lotPorcId;
    private Long typeMouvementLotId;
    private Integer quantite;
    private LocalDateTime dateMouvement;
    private String motif;
}
