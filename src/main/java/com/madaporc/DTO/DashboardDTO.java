package com.madaporc.DTO;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class DashboardDTO {
    private long lotsActifs;

private int totalPorcs;

private int porcsVendables;

private long reproducteursActifs;

private long casSanitairesEnCours;

private BigDecimal ventesMois;

private BigDecimal depensesMois;

private BigDecimal beneficeNet;
}
