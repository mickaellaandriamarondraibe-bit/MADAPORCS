package com.madaporc.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Data;

@Data
@Entity
@Table(name = "mouvements_lots_porcs")
public class MouvementLotPorc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lot_porc_id")
    private Long lotPorcId;

    @Column(name = "type_mouvement_lot_id")
    private Long typeMouvementLotId;

    @Column(name = "quantite")
    private Integer quantite;

    @Column(name = "quantite_male")
    private Integer quantiteMale;

    @Column(name = "quantite_femelle")
    private Integer quantiteFemelle;

    @Column(name = "date_mouvement")
    private LocalDateTime dateMouvement;

    @Column(name = "motif")
    private String motif;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
