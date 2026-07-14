# Scenario de demonstration - soutenance MADAPORC

Jeu de donnees et enchainement a derouler devant le jury. Chaque etape
verifie une exigence du plan de projet.

## 0. Preparation
- Base `madaporc` chargee (schema.sql + data.sql).
- Se connecter en administrateur.
- Verifier le dashboard : KPIs, graphes (cheptel, finances, reproduction).

## 1. Cheptel - creation de lots
1. Lots > Nouveau lot : creer **LOT-F-001** (femelle, reproduction, Large White).
   - A la creation, redirection automatique vers la saisie du poids de depart.
2. Creer **LOT-M-001** (male, reproduction).
3. Liste des lots :
   - **Tri** : cliquer sur l'en-tete "Effectif" (croissant / decroissant).
   - **Intervalle** : filtrer les lots dont l'effectif est entre deux bornes.
   - **Recherche** : taper un code dans la barre de recherche.

## 2. Reproduction
1. Reproduction > Nouveau groupe : creer **GR-001** (LOT-F-001 x LOT-M-001).
2. Detail du groupe : la barre de progression change de couleur selon les
   jours restants (vert, orange <= 15 j, rouge <= 5 j).
3. Confirmer la mise bas : renseigner nes / vivants / morts et la repartition
   male / femelle. Les lots de naissance (**LOT-N-...**) sont crees
   automatiquement (un par sexe).

## 3. Commerce - vente
1. Clients > Nouveau client.
2. Ventes > Nouvelle vente : selectionner un lot, quantite, prix.
   - Valider la vente : l'effectif du lot diminue.
3. Liste des ventes :
   - **Intervalle Date** et **Intervalle Montant** pour filtrer.
   - **Tri** par montant ou par date.
   - **Recu PDF** d'une vente.

## 4. Finance
1. Depenses > Nouvelle depense (categorie, montant, date).
   - Filtre serveur par periode / categorie + intervalle Montant cote liste.
2. Rapports : generer les rapports (reproduction, commercial, sanitaire,
   financier) en PDF.

## 5. Sante
1. Vaccinations : programmer une vaccination, marquer un rappel.

## 6. Import / Export
1. Export : lots, ventes, groupes, analyse (CSV/Excel).
2. Import : importer des clients / ingredients / vaccinations depuis un
   fichier ; un rapport liste les lignes importees et les lignes rejetees.

## 7. Administration
1. Utilisateurs : creer un gestionnaire, puis le desactiver.
   - La session du compte desactive est fermee immediatement (redirige au login).
   - Le dernier administrateur actif ne peut pas etre desactive.

## Fonctions transversales couvertes
- Recherche texte multi-colonnes sur toutes les listes.
- Recherche par intervalle (chiffres et dates) : ventes, depenses, lots.
- Tri croissant / decroissant par colonne sur toutes les listes `.tbl`.
- Pagination cote navigateur.
- Graphes dashboard (histogramme, courbe, camembert).
- Export CSV/Excel et rapports PDF.
- Import avec detection des doublons et rapport de rejets.
