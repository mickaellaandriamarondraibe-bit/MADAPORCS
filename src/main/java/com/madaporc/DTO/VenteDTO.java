package com.madaporc.DTO;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class VenteDTO {
    private Long id;
    private Long clientId;
    private LocalDateTime dateVente;
    private String statutVente;
    private String observation;
    private List<DetailVenteDTO> details;
}
