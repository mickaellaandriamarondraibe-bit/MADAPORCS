package model;

import java.math.BigDecimal;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import java.time.LocalDateTime;
import com.madaporc.model.LotPorc;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;   

@Entity
@Table(name="repartitions_reproductives_lots")
public class RepartitionReproductiveLot {
    @Id
    private BigDecimal id;

    @Column(name="lot_id")
    @ManyToOne
    @JoinColumn(name="id")
    private LotPorc lotPorc;

    @Column(name="statut_reproductif")
    @ManyToOne
    @JoinColumn(name="code")
    private StatutReproductif statutReproductif;

    @Column(name="quantite")
    private Integer quantite;

    @Column(name="date_mis_a_jour")
    private LocalDateTime dateMiseAJour;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public LotPorc getLotPorc() {
        return lotPorc;
    }

    public void setLotPorc(LotPorc lotPorc) {
        this.lotPorc = lotPorc;
    }

    public StatutReproductif getStatutReproductif() {
        return statutReproductif;
    }

    public void setStatutReproductif(StatutReproductif statutReproductif) {
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

    public RepartitionReproductiveLot() {
    }
}