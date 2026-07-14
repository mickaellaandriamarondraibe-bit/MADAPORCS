package com.madaporc.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

// Ligne unifiee d'historique/tracabilite : un mouvement de lot OU de stock,
// ramene a un format commun pour la page d'audit "/mouvements".
@Getter
@Setter
public class MouvementAuditDTO {

    private LocalDate date;
    private String categorie;   // "Lot de porcs" / "Stock aliment"
    private String type;        // type brut : ENTREE, DECES, VENTE, NAISSANCE...
    private String sens;        // "ENTREE" / "SORTIE"
    private String cible;       // code du lot / nom de l'ingredient
    private String quantite;    // ex. "5 porcs" / "20 kg"
    private String detail;      // observation / stock apres
}
