package com.madaporc.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailVenteDTO {

	private Long lotId;
	private Integer quantite;
	private BigDecimal prixUnitaire;
	private BigDecimal poidsTotal;
}
