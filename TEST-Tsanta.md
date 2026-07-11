## 6. Reproduction - Groupes

| #      | Cas de test                                        | Resultat attendu                                               | Statut | Remarques |
| ------ | -------------------------------------------------- | -------------------------------------------------------------- | ------ | --------- |
| REP-01 | Ouvrir la liste des groupes de reproduction        | Liste affichee                                                 |     ok  |           |
| REP-02 | Creer un groupe (lot femelle, male, dates)         | Groupe cree, date prevue de mise bas calculee                  |    ok    | l'erreur quand j'ai cree une lot de type reproduction mais le mois est inferieur a 8 quand j'ai commencer a cree le groupe ca a pas marcher et ca a donner femelle insuffisant  disponible 0  or normalement c'est une erreur lié a l'insuffisance de mois et non la disponibilite de lot femelle       |
| REP-03 | Formulaire avec donnees manquantes                 | Refus                                        |   ok     |           |
| REP-04 | Ouvrir le detail d'un groupe                       | Informations completes (lots, dates, statut)                   |     ok   |           |
| REP-05 | Ouvrir "Confirmer la mise bas"                     | Formulaire de confirmation affiche                             |   ok     |           |
| REP-06 | Confirmer une mise bas (nombre de porcelets, date) | Mise bas enregistree, lot de naissance cree, statut mis a jour |   ok     |           |
| REP-07 | Cloturer un groupe                                 | Groupe cloture, sort des groupes actifs                        |    ok    |           |
| REP-08 | Verifier l'impact tableau de bord / calendrier     | Mise a jour des mises bas proches et du taux de fertilite      |     ok   |           |
| REP-09 | Details groupe     | Resume de l'informarion de groupe     |     ok   |     affiche une petite incoherence de valeur de champs (corriger)       |

## 7. Reproduction - Analyse reproductive

| #      | Cas de test                              | Resultat attendu                             | Statut | Remarques |
| ------ | ---------------------------------------- | -------------------------------------------- | ------ | --------- |
| ANA-01 | Ouvrir "Analyse reproductive"            | Liste des lots analysables affichee          |     ok   |           |
| ANA-02 | Ouvrir l'analyse d'un lot                | Detail de l'analyse affiche                  |    ok    |           |
| ANA-03 | Generer/mettre a jour l'analyse d'un lot | Analyse (re)calculee, indicateurs mis a jour |    ok    |           |
| ANA-04 | Lot sans donnee suffisante               | Message ou etat vide clair, pas d'erreur     |        |           |

## 8. Reproduction - Alertes

| #      | Cas de test                         | Resultat attendu                                        | Statut | Remarques |
| ------ | ----------------------------------- | ------------------------------------------------------- | ------ | --------- |
| ALE-01 | Ouvrir la liste des alertes         | Alertes affichees (non lues distinguees)                |   ok     |           |
| ALE-02 | Marquer une alerte comme lue        | L'alerte passe en "lue"                                 |    ok    |           |
| ALE-03 | Marquer une alerte comme traitee    | L'alerte passe en "traitee" et sort des alertes actives |    ok    |   pn entre juste dans une page mais il y a pas vraiment une traitement normalement on devrait traiter en ajoutant une boutton         |
| ALE-04 | Verifier le badge du menu "Alertes" | Le compteur correspond au nombre d'alertes non traitees |        |           |
