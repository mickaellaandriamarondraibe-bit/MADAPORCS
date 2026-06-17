package com.madaporc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.GenerationType;
import java.time.LocalDateTime;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;

/**
 * Model placeholder pour la table mouvements_lots_porcs.
 * Les colonnes exactes seront ajoutées pendant le développement du module.
 */
@Getter
@Setter
@Entity
@Table(name = "mouvements_lots_porcs")
public class MouvementLotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_porc_id", nullable = false)
    private Long lotPorcId;

    @ManyToOne
    @JoinColumn(name = "type_mouvement_lot_id")
    private TypeMouvementLot typeMouvementLotId;

    private Integer quantite;

    @Column(name = "quantite_male")
    private Integer quantiteMale;

    @Column(name = "quantite_femelle")
    private Integer quantiteFemelle;

    private String motif;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "date_mouvement", nullable = false)
    private LocalDateTime dateMouvement;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
