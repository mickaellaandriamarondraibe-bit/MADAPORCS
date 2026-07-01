# 🐷 MADAPORC — Scénario de démonstration (flow)

Ce document est un **parcours guidé** : tu fais une action dans l'interface, puis tu vas
voir **sur quelle page ça a changé** et **grâce à quoi**. Idéal pour une démo ou pour
comprendre comment les données circulent dans l'application.

- **Base de l'application** : http://localhost:8082
- **Connexion** : compte *Administrateur* (déjà présent en base)
- **Astuce** : après chaque action, reviens sur le **Tableau de bord** (`/dashboard`)
  pour voir l'effet. Recharge la page (F5) si besoin.

> 🟢 = fonctionne via l'interface  ·  🔴 = pas encore disponible (contrôleur à finir)

---

## ⚠️ À savoir avant de commencer

| Module | État | Remarque |
|--------|------|----------|
| Lots, Clients, Ingrédients, Mouvements stock | 🟢 | OK |
| Vaccins, Vaccinations, Suivis sanitaires | 🟢 | OK |
| Groupes de reproduction, Analyse reproductive | 🟢 | OK |
| Pesées, Mouvements de lot | 🟢 | OK |
| **Ventes** | 🔴 | `VenteController` vide → menu non fonctionnel |
| **Dépenses** | 🔴 | `DepenseController` vide → menu non fonctionnel |

➡️ Conséquence : la partie **Finances** du dashboard (Ventes / Dépenses / Bénéfice)
**restera à 0** tant que ces deux contrôleurs ne sont pas implémentés. Tout le reste
se remplit via l'interface.

---

## Étape 0 — Point de départ

La base a été vidée de toutes les données métier. Il reste seulement :
le **login**, et les **listes de référence** (catégories de dépenses, races,
statuts reproductifs, paramètres de race).

👉 **Va voir** : `Tableau de bord` → tout est à **0** (lots, porcs, groupes…),
les tableaux « Mises bas proches », « Stocks faibles », « Vaccinations à venir »
sont vides. C'est normal : on part de zéro.

---

## Étape 1 — Créer un lot de porcs 🟢

C'est la **brique de base** : presque tout dépend d'un lot.

1. Menu **Lots de porcs** → bouton **Nouveau lot**
2. Crée un **lot femelle** :
   - Code : `LOT-F-001`
   - Sexe : `FEMELLE`
   - Objectif : `REPRODUCTION`
   - Effectif actuel : `5`
   - Statut : `ACTIF`
   - Race : choisis une race dans la liste
3. Enregistre.
4. Recommence pour un **lot mâle** : `LOT-M-001`, `MALE`, effectif `2`, `ACTIF`.

### ✅ Ce qui a changé grâce à ça

| Va voir cette page | Ce qui apparaît | Pourquoi |
|--------------------|-----------------|----------|
| `Tableau de bord` → KPI **Lots actifs** | passe à **2** | compte les lots au statut `ACTIF` |
| `Tableau de bord` → sous-texte **porcs actifs** | passe à **7** | somme des effectifs des lots actifs (5 + 2) |
| `Tableau de bord` → onglet **Cheptel** | la courbe monte | l'évolution mensuelle utilise la date de création du lot |
| `Lots de porcs` (liste) | tes 2 lots | — |

---

## Étape 2 — Faire un mouvement d'effectif sur un lot 🟢

Pour montrer qu'un lot **vit** (entrées / sorties de porcs).

1. Menu **Lots de porcs** → clique sur `LOT-F-001` (détail)
2. Onglet / section **Mouvements** → enregistre un mouvement
   (ex. une **sortie** de 1 porc, ou une **entrée**).

### ✅ Ce qui a changé

| Va voir | Ce qui change | Pourquoi |
|---------|---------------|----------|
| Détail du lot | l'**effectif actuel** est recalculé | le mouvement augmente/diminue l'effectif |
| `Tableau de bord` → **porcs actifs** | le total suit | il somme les effectifs à jour |

---

## Étape 3 — Créer un client 🟢

1. Menu **Clients** → **Nouveau client**
2. Nom (obligatoire), téléphone, adresse → Enregistre.
3. Clique sur **Détail** puis **Modifier** : les champs sont **pré-remplis**.

### ✅ Ce qui a changé

- `Clients` (liste) : ton client apparaît.
- Il sera **sélectionnable plus tard** dans une vente (quand le module Ventes sera prêt).

---

## Étape 4 — Créer un ingrédient et voir une alerte de stock faible 🟢

1. Menu **Ingrédients** → **Nouvel ingrédient**
   - Nom : `Maïs`
   - Stock actuel : `2`
   - Seuil d'alerte : `10` (mets un seuil **plus haut** que le stock)
   - Unité : `kg`
2. Enregistre.

### ✅ Ce qui a changé grâce au seuil

| Va voir | Ce qui apparaît | Pourquoi |
|---------|-----------------|----------|
| `Tableau de bord` → tableau **Stocks faibles** | `Maïs` avec badge **bas** | stock (2) < seuil (10) |

3. Maintenant menu **Mouvements stock** → enregistre une **entrée** de `20 kg` de Maïs.

| Va voir | Ce qui change |
|---------|---------------|
| `Ingrédients` | stock du Maïs = 22 |
| `Tableau de bord` → **Stocks faibles** | le Maïs **disparaît** (22 > 10) |

---

## Étape 5 — Créer un vaccin puis une vaccination 🟢

1. Menu **Vaccins** → **Nouveau vaccin** (ex. `Peste porcine`). Enregistre.
2. Menu **Vaccinations** → **Nouvelle vaccination**
   - Lot : `LOT-F-001`
   - Vaccin : `Peste porcine`
   - Date de rappel : **dans moins de 30 jours** (ex. dans 10 jours)
3. Enregistre.

### ✅ Ce qui a changé

| Va voir | Ce qui apparaît | Pourquoi |
|---------|-----------------|----------|
| `Tableau de bord` → **Vaccinations à venir** | ta vaccination | rappel prévu dans les 30 prochains jours |

> Si tu mets une date de rappel **lointaine** (> 30 jours), elle n'apparaîtra **pas**
> dans le dashboard : c'est volontaire (on n'alerte que sur le proche).

---

## Étape 6 — Créer un groupe de reproduction 🟢

C'est ici qu'on relie un lot femelle à une saillie.

1. Menu **Groupes** (Reproduction) → **Nouveau groupe**
   - Lot femelle : `LOT-F-001`
   - Nombre de femelles concernées : `4`
   - Date de saillie : une date récente
2. Enregistre. Le code (ex. `GRP-...`) et la **date prévue de mise bas** sont calculés
   automatiquement (date de saillie + durée de gestation de la race).

### ✅ Ce qui a changé

| Va voir | Ce qui apparaît | Pourquoi |
|---------|-----------------|----------|
| `Tableau de bord` → KPI **Groupes actifs** | **+1** | le groupe est au statut `SAILLIE` / `EN_GESTATION` (= actif) |
| `Tableau de bord` → **Mises bas proches** | apparaît **si** la mise bas prévue est dans **≤ 5 jours** | sinon rien (normal) |
| `Groupes` (liste) | ton groupe | — |

> ℹ️ **Important** : un groupe **CLÔTURÉ** ne compte **plus** comme « groupe actif ».
> Donc après la clôture (étape 8), le KPI « Groupes actifs » rebaisse. C'est normal.

---

## Étape 7 — Confirmer la mise bas 🟢

1. Menu **Groupes** → clique sur ton groupe (détail)
2. Bouton **Confirmer la mise bas**
   - Date réelle, nombre de femelles ayant mis bas, porcelets nés (vivants / morts)…
3. Enregistre. Tu peux ensuite **créer le lot naissance** (les porcelets deviennent un nouveau lot).

### ✅ Ce qui a changé

| Va voir | Ce qui change | Pourquoi |
|---------|---------------|----------|
| Détail du groupe | barre **Évolution du cycle**, bloc **Résultat de mise bas** | les données saisies s'affichent |
| `Lots de porcs` | un **nouveau lot** (les porcelets) si tu as créé le lot naissance | lien lot mère → lot naissance |
| `Tableau de bord` → **porcs / lots actifs** | augmentent | le lot naissance est un nouveau lot actif |

---

## Étape 8 — Générer une analyse reproductive 🟢

C'est **ça** qui remplit l'onglet **Reproduction** du dashboard (et pas le groupe !).

1. Menu **Analyse reproductive**
2. Sur la ligne `LOT-F-001` → bouton vert **Générer**
3. (Ensuite **Voir analyse** pour consulter le détail : taux d'aptitude, fertilité, décision.)

### ✅ Ce qui a changé grâce à l'analyse

| Va voir | Ce qui apparaît | Pourquoi |
|---------|-----------------|----------|
| `Tableau de bord` → KPI **Aptitude globale** | un **%** | moyenne des taux d'aptitude des analyses |
| `Tableau de bord` → KPI **Fertilité observée** | un **%** | moyenne des taux de fertilité |
| `Tableau de bord` → onglet **Reproduction** | les **2 barres** se remplissent | même source (table des analyses) |

> 💡 C'est la grande différence à retenir :
> **Groupe de reproduction** = suivi d'une saillie/mise bas (gestion).
> **Analyse reproductive** = calcul de taux sur un lot femelle (statistiques).
> Le dashboard **Reproduction** lit l'**analyse**, pas le groupe.

---

## Étape 9 — Le rapport financier 🟢 (mais dépend des Ventes/Dépenses 🔴)

1. Menu **Rapports** → choisis une période (dates début / fin) → Générer.
2. Le bilan affiche Total ventes, Total dépenses, Bénéfice net, nombre de ventes/dépenses.

> ⚠️ Tant que les modules **Ventes** et **Dépenses** ne sont pas implémentés, ce bilan
> restera à **0** : il n'y a aucune vente validée ni dépense à additionner.

---

## 🔴 Étapes pas encore disponibles (à finir côté code)

Ces actions **n'ont pas d'interface fonctionnelle** aujourd'hui :

- **Créer une vente** (`VenteController` vide) → impossible de remplir
  *Ventes du mois* / *Bénéfice* du dashboard.
- **Créer une dépense** (`DepenseController` vide) → impossible de remplir
  *Dépenses du mois*.

Quand ces deux contrôleurs seront codés, le scénario se complétera ainsi :

1. **Ventes** → créer une vente, ajouter des lignes (lot + quantité + prix), **valider**.
   - 👉 Dashboard : *Ventes du mois* et *Bénéfice net* augmentent
     (⚠️ seules les ventes au statut **VALIDÉE** comptent).
2. **Dépenses** → créer une dépense (catégorie + montant + date).
   - 👉 Dashboard : *Dépenses du mois* augmente, *Bénéfice net* = Ventes − Dépenses.
   - 👉 Onglet **Finances** : les 3 barres deviennent significatives.

---

## 🧭 Récapitulatif : « j'agis ici → ça change là »

| Action (interface) | Effet visible | Page d'effet |
|--------------------|---------------|--------------|
| Créer un lot `ACTIF` | Lots actifs, porcs actifs, courbe cheptel | Tableau de bord |
| Mouvement d'effectif | effectif du lot, porcs actifs | Détail lot + Dashboard |
| Ingrédient sous le seuil | alerte stock faible | Dashboard |
| Mouvement stock (entrée) | stock à jour, alerte disparaît | Ingrédients + Dashboard |
| Vaccination rappel < 30 j | vaccinations à venir | Dashboard |
| Créer un groupe (saillie) | groupes actifs, mises bas proches | Dashboard |
| Confirmer mise bas + lot naissance | nouveau lot, porcs actifs | Lots + Dashboard |
| Générer une analyse | aptitude %, fertilité %, onglet Reproduction | Dashboard |
| (à venir) Valider une vente | ventes du mois, bénéfice | Dashboard / Rapports |
| (à venir) Créer une dépense | dépenses du mois, bénéfice | Dashboard / Rapports |
