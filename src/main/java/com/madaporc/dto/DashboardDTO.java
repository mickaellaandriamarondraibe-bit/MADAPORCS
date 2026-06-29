package com.madaporc.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.madaporc.model.Ingredient;
import com.madaporc.model.Vaccination;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DashboardDTO {

    private long lotsActifs;
    private int totalPorcs;
    private long groupesActifs;
    private long misesBasProches;

    private BigDecimal tauxAptitudeGlobale = BigDecimal.ZERO;
    private BigDecimal tauxFertiliteObserve = BigDecimal.ZERO;

    private BigDecimal ventesMois = BigDecimal.ZERO;
    private BigDecimal depensesMois = BigDecimal.ZERO;
    private BigDecimal beneficeNet = BigDecimal.ZERO;

    private List<Ingredient> stocksFaibles = new ArrayList<>();
    private List<Vaccination> vaccinationsAVenir = new ArrayList<>();

    // Pour le graphe historique du cheptel
    private List<String> moisLabels = new ArrayList<>();
    private List<Integer> totalPorcsParMois = new ArrayList<>();
    private List<Long> lotsActifsParMois = new ArrayList<>();
    private List<Long> groupesActifsParMois = new ArrayList<>();

 
    
}