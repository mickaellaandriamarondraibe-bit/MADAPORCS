package com.madaporc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VenteDTO {

	private Long id;
	private Long clientId;
	private LocalDate dateVente;
	private BigDecimal montantTotal;
	private String statut;
	private List<DetailVenteDTO> lignes = new ArrayList<>();
}
