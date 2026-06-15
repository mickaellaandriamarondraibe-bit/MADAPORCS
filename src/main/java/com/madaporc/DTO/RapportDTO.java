package com.madaporc.DTO;


import lombok.Data;

@Data
public class RapportDTO {
    private RapportFinancierDTO financier;

private RapportSanitaireDTO sanitaire;

private RapportStockDTO stock;

private RapportPresenceDTO presence;

private RapportProductionDTO production;
}
