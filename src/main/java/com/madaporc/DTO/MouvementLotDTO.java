package com.madaporc.DTO;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class MouvementLotDTO {
    private Long id;

private Long lotPorcId;

private Long typeMouvementLotId;

private Integer quantite;

private Integer quantiteMale;

private Integer quantiteFemelle;

private LocalDateTime dateMouvement;

private String motif;
}
