package com.madaporc.model;

import jakarta.persistence.*;

@Entity
@Table(name = "repartitions_reproductives_lots")
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "repartitions_reproductives_lots")
@Getter
@Setter
@NoArgsConstructor
public class RepartitionReproductiveLot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lot_id", nullable = false)
    private LotPorc lot;
    @Column(name = "statut_reproductif", nullable = false, length = 50)
    private String statutReproductif;

    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "date_mise_a_jour", nullable = false)
    private LocalDateTime dateMiseAJour;

    @PrePersist
    public void beforeCreate() {
        if (dateMiseAJour == null) {
            dateMiseAJour = LocalDateTime.now();
        }

        if (quantite == null) {
            quantite = 0;
        }
    }

    @PreUpdate
    public void beforeUpdate() {
        dateMiseAJour = LocalDateTime.now();
    }
}