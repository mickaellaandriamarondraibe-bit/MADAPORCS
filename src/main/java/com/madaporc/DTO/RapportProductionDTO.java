package com.madaporc.DTO;

import lombok.Data;

@Data
public class RapportProductionDTO {

    private long nombreCycles;
    private int totalNaissances;
    private int totalPertes;
    private int totalVendables;
}