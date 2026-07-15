# Données de démonstration MADAPORC — Soutenance

Données à saisir directement dans l'application, dans l'ordre (chaque écran dépend du précédent).

Date de référence : **2026-07-15**.

## À savoir avant de commencer

- Le **code lot** est généré automatiquement (LOT-F-xxx / LOT-M-xxx) — pas de saisie.
- La colonne **Date d'entrée** correspond au champ date du formulaire (date de création / entrée du lot).
- Objectif ne propose que **Reproduction** ou **Engraissement**.
- Origine **Achat** rend obligatoires **Âge** et **Prix d'achat**. Le prix est demandé **par tête** : la dépense créée automatiquement = prix par tête x effectif initial.
- Les **alertes se génèrent automatiquement** dès qu'un groupe a une mise bas proche ou dépassée (visibles en ~5 s) — rien à saisir.

---

## 1. Lots de porcs

Cheptel > Lots de porcs > Nouveau lot

| Date d'entrée | Race | Sexe | Objectif | Origine | Âge (mois) | Prix d'achat / tête (Ar) | Effectif initial | Description |
|---|---|---|---|---|---|---|---|---|
| 2025-06-10 | Large White | Femelle | Reproduction | Achat | 14 | 500 000 | 6 | Truies reproductrices |
| 2025-09-18 | Landrace | Femelle | Reproduction | Achat | 16 | 500 000 | 5 | Truies reproductrices |
| 2025-06-30 | Duroc | Mâle | Reproduction | Achat | 20 | 1 000 000 | 2 | Verrats reproducteurs |
| 2026-02-15 | Pietrain | Mâle | Engraissement | Achat | 5 | 150 000 | 13 | Lot d'engraissement |
| 2026-04-16 | Landrace | Femelle | Engraissement | Naissance | — | — | 10 | Jeunes issues du sevrage |

Dépenses d'achat générées automatiquement (prix par tête x effectif) : Large White 3 000 000, Landrace 2 500 000, Duroc 2 000 000, Pietrain 1 950 000.

---

## 2. Clients

Commerce > Clients

Déjà créés au démarrage (Boucherie Centrale, Marché Ambohipo). S'ils manquent :

| Nom | Téléphone | Adresse |
|---|---|---|
| Boucherie Centrale | 0341234567 | Antananarivo |
| Marché Ambohipo | 0329876543 | Ambohipo |

---

## 3. Groupes de reproduction

Reproduction > Groupes > Nouveau

Ce sont eux qui déclenchent les alertes (visibles sous Reproduction > Alertes).

| Lot femelle | Lot mâle | Nb femelles | Nb mâles | Date saillie | Durée gestation | Observation | Effet |
|---|---|---|---|---|---|---|---|
| Truies Large White | Verrats Duroc | 6 | 1 | 2026-03-26 | 114 | Gestation en cours | Mise bas prévue 2026-07-18 -> alerte Mise bas proche |
| Truies Landrace | Verrats Duroc | 5 | 1 | 2026-03-21 | 114 | À contrôler | Mise bas prévue 2026-07-13 -> alerte Retard mise bas |

---

## 4. Ventes

Commerce > Ventes > Nouvelle vente

| Client | Date vente | Lot | Quantité | Prix unitaire (Ar) | Total |
|---|---|---|---|---|---|
| Boucherie Centrale | 2026-07-09 | Engraissement Pietrain | 6 | 700 000 | 4 200 000 |
| Marché Ambohipo | 2026-07-13 | Femelles Landrace (engrais.) | 4 | 550 000 | 2 200 000 |

---

## 5. Dépenses

Stocks & Finance > Dépenses > Nouvelle

L'achat des 4 lots ci-dessus a déjà créé 4 dépenses. Ajouter celles-ci pour compléter :

| Catégorie | Date | Montant (Ar) | Description |
|---|---|---|---|
| Personnel | 2026-06-15 | 600 000 | Salaire ouvrier |
| Maintenance | 2026-06-05 | 250 000 | Réparation des enclos |
| Alimentation | 2026-06-25 | 450 000 | Complément alimentaire |

---

## 6. Stock aliment

Stocks & Finance > Mouvements stock > Nouveau

Ingrédients déjà présents (Maïs, Son de riz, Tourteau). Ajouter des entrées :

| Ingrédient | Type | Quantité | Montant (Ar) | Effet |
|---|---|---|---|---|
| Maïs | Entrée | 300 | 900 000 | |
| Son de riz | Entrée | 120 | 360 000 | |
| Tourteau | Entrée | 15 | 75 000 | Reste sous le seuil (20) -> stock faible |

---

## 7. Vaccinations

Santé > Vaccinations > Nouvelle

| Lot | Vaccin | Date vaccination | Date rappel | Coût (Ar) | Observation |
|---|---|---|---|---|---|
| Truies Large White | Parvovirose | 2026-05-16 | 2026-07-19 | 40 000 | Rappel à venir |
| Engraissement Pietrain | Peste porcine | 2026-05-31 | 2026-08-04 | 60 000 | — |

---

## 8. Suivis sanitaires

Santé > Suivis sanitaires > Nouveau

| Lot | Maladie | Traitement | Diagnostic | Traitement | Guérison | Observation |
|---|---|---|---|---|---|---|
| Engraissement Pietrain | Diarrhée | Réhydratation et traitement vétérinaire | 2026-06-20 | 2026-06-21 | 2026-06-25 | Guéri |
| Femelles Landrace | Toux | Traitement respiratoire | 2026-07-09 | 2026-07-10 | — | En cours |

---

## 9. Destinataires des alertes

Administration > Destinataires alertes

| Adresse e-mail |
|---|
| responsable.elevage@madaporc.local |

---

## 10. Analyse reproductive

Reproduction > Analyse reproductive

Aucune saisie : choisir **Truies Large White**, cliquer **Générer l'analyse**. La répartition et les taux se calculent automatiquement.
