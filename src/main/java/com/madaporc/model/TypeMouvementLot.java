package com.madaporc.model;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import java.util.List;

/**
 * Model placeholder pour la table type_mouvements_lots.
 * Les colonnes exactes seront ajoutées pendant le développement du module.
 */
@Getter
@Setter
@Entity
@Table(name = "type_mouvements_lots")
public class TypeMouvementLot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libelle;

    @OneToMany(mappedBy="typeMouvementLotId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MouvementLotPorc> mouvementsLotPorcs;
}
