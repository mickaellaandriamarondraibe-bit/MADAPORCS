package com.madaporc.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
<<<<<<< HEAD
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
=======
import java.time.LocalDateTime;

public class VenteDTO {

    private Long id;

    private Long clientId;

    private String nomClient;

    private LocalDate dateVente;

    private Long LotId;

    private Integer quantite;

    private BigDecimal poidsTotal;

    private BigDecimal prixUnitaire;

    private String statut;

    public VenteDTO() {
    }

    public Long getId() {
        return id;
    }

    public Long getLotId() {
        return LotId;
    }

    public void setLotId(Long lotId) {
        LotId = lotId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public BigDecimal getPoidsTotal() {
        return poidsTotal;
    }

    public void setPoidsTotal(BigDecimal poidsTotal) {
        this.poidsTotal = poidsTotal;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public String getNomClient() {
        return nomClient;
    }

    public void setNomClient(String nomClient) {
        this.nomClient = nomClient;
    }

    public LocalDate getDateVente() {
        return dateVente;
    }

    public void setDateVente(LocalDate dateVente) {
        this.dateVente = dateVente;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

}
>>>>>>> 5e4d8bf (correction)
