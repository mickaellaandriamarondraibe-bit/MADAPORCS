package com.madaporc.dto;

import java.time.LocalDateTime;

public class RepartitionReproductiveDTO {
    private Long id;
    private String lotPorc;
    private String statutReproductif;
    private Integer quantite;
    private LocalDateTime dateMiseAJour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLotPorc() {
        return lotPorc;
    }

    public void setLotPorc(String lotPorc) {
        this.lotPorc = lotPorc;
    }

    public String getStatutReproductif() {
        return statutReproductif;
    }

    public void setStatutReproductif(String statutReproductif) {
        this.statutReproductif = statutReproductif;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }

    public LocalDateTime getDateMiseAJour() {
        return dateMiseAJour;
    }

    public void setDateMiseAJour(LocalDateTime dateMiseAJour) {
        this.dateMiseAJour = dateMiseAJour;
    }

    public RepartitionReproductiveDTO() {
    }
}
