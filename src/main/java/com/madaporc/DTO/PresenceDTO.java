package com.madaporc.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO conforme au PDF - placeholder à compléter si besoin.
 */
@Getter
@Setter
public class PresenceDTO {
    private Long id;
    private Long employeId;
    private LocalDate datePresence;
    private String statutPresence;
    private LocalTime heureArrivee;
    private LocalTime heureDepart;
    private String observation;
}
