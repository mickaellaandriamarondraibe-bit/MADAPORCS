# Cahier des charges - MADAPORC (version projet fini)

Systeme intelligent de suivi, de tracabilite et d'aide a la decision reproductive pour l'elevage porcin.

Ce document decrit le projet **tel qu'il a ete effectivement realise**. Il remplace la version initiale (conservee dans `docs/Cahier des Charges MADAPORC.pdf`) et reflete les choix d'implementation retenus par l'equipe. Les ecarts avec la version initiale sont recapitules en fin de document.

---

## 1. Presentation

MADAPORC est une application web de gestion d'une exploitation porcine. Au-dela de l'enregistrement administratif, elle integre un module d'analyse reproductive qui evalue automatiquement l'aptitude des femelles a la reproduction et assiste l'eleveur dans ses decisions (mise en reproduction, reforme).

Le systeme assure la tracabilite des lots, le suivi sanitaire, le suivi commercial et financier, la gestion des stocks d'aliments, et fournit des indicateurs de pilotage sur un tableau de bord.

## 2. Architecture technique

| Element | Choix |
| ------- | ----- |
| Langage / framework | Java, Spring Boot (MVC) |
| Vues | JSP + JSTL |
| Base de donnees | PostgreSQL |
| Securite | Session applicative + interceptor (`AuthInterceptor`) ; CSRF desactive (choix assume) |
| Schema / donnees | `schema.sql` + `data.sql` ; `spring.jpa.hibernate.ddl-auto=update` |
| Notifications | SSE (Server-Sent Events) + e-mail (SMTP optionnel) |
| Exports | CSV (Excel) et PDF (openhtmltopdf) |

L'architecture est en couches : `controller` (24) -> `service` (30) -> `repository` (JPA) -> `model` (entites). Les vues JSP sont rendues cote serveur avec un layout commun (`header`, `sidebar`, `footer`).

## 3. Roles et securite

- Deux roles : **ADMIN** et **GESTIONNAIRE**.
- Toute page interne exige une session ouverte (sinon redirection vers la page de connexion).
- Modules reserves a l'ADMIN : Utilisateurs, Depenses, Import/Export, Exports. Acces refuse (403) pour un GESTIONNAIRE, et entrees masquees dans le menu.
- Les entrees masquees ne sont pas seulement cachees : elles sont bloquees cote serveur par l'interceptor.

## 4. Modules fonctionnels

### 4.1 Utilisateurs (ADMIN)
Connexion, deconnexion, liste des utilisateurs, creation/modification, activation/desactivation d'un compte, gestion des roles. Un compte desactive ne peut plus se connecter. Email unique (les doublons sont refuses).

### 4.2 Cheptel - Lots de porcs
- Creation, modification, archivage, consultation detaillee, historique du lot.
- Code de lot **genere automatiquement** (`LOT-M-xxx` / `LOT-F-xxx` selon le sexe et l'identifiant).
- Origine `ACHAT` ou `NAISSANCE`. Pour un lot ACHAT, le **prix d'achat est obligatoire** et cree automatiquement une **depense** (prix x effectif) categorie "ACHAT ANIMAUX".
- Suivi de l'effectif initial et actuel ; le lot alimente le tableau de bord (lots actifs, porcs actifs).

### 4.3 Mouvements de lots
Types : ENTREE, NAISSANCE, TRANSFERT, DECES, VENTE. Chaque mouvement met a jour l'effectif du lot en consequence. Historique consultable par lot. Les deces alimentent le calendrier et le taux de mortalite.

### 4.4 Pesees
Enregistrement d'une pesee (poids moyen, date, observation), historique trie, suivi de croissance. Controle : la date de pesee ne peut pas preceder la date de creation du lot.

### 4.5 Reproduction - Groupes
La reproduction est geree **par groupes** (une partie d'un lot femelle mise en reproduction avec un lot male), et non par reproducteur individuel.
- Creation d'un groupe : selection lot femelle / lot male, nombre de femelles concernees, date de saillie, duree de gestation ; la **date prevue de mise bas est calculee** automatiquement.
- Controles a la creation : sexe des lots, statut ACTIF, absence de maladie en cours, delai de re-saillie, et surtout **disponibilite reproductive reelle** des femelles (voir 4.8).
- Confirmation de la mise bas : saisie des femelles gestantes, femelles ayant mis bas, porcelets vivants et morts, et repartition des vivants par sexe. Les champs deductibles (porcelets nes, femelles non gestantes, porcelets males) sont **calcules automatiquement**. Regle de coherence : femelles + males = porcelets vivants.
- La mise bas cree automatiquement le(s) **lot(s) de naissance** (un lot femelle et/ou un lot male) relies au lot mere et au groupe d'origine.
- Cloture d'un groupe : sort des groupes actifs.

### 4.6 Reproduction - Analyse reproductive (coeur du projet)
Pour chaque lot femelle, le systeme calcule et affiche :
- l'age reproductif reel (age a l'achat + mois ecoules) ;
- la repartition des femelles par statut reproductif ;
- le taux d'aptitude et le taux recommande ;
- le taux de fertilite observe (gestantes / saillies) ;
- une **recommandation automatique** : Apte a la reproduction / A surveiller / Reforme recommandee.

Le classement s'appuie sur les bornes d'age par race (`parametres_reproduction_race`) et sur l'historique des groupes.

### 4.7 Reproduction - Alertes
Alertes de mise bas proche ou en retard, generees automatiquement (tache planifiee). Marquage "lue" / "traitee" ; une alerte traitee sort des alertes actives. Un **badge** dans le menu indique le nombre d'alertes non traitees (sur toutes les pages).

### 4.8 Repartition reproductive (mecanisme interne)
Chaque lot femelle possede une repartition par statut : PRETE_JAMAIS_SAILLIE, DEJA_REPRODUCTRICE_APTE, EN_CYCLE, A_SURVEILLER, A_RETIRER_REPRODUCTION. Seules les femelles PRETE ou DEJA_APTE sont "disponibles" pour un nouveau groupe. La repartition est initialisee a la creation du lot et mise a jour a chaque saillie / mise bas.

### 4.9 Sante
- **Vaccins** : ajout, modification, desactivation.
- **Vaccinations** : enregistrement (lot, vaccin, dates), historique, gestion des rappels. Une vaccination genere une depense (categorie SANTE) si un cout est renseigne.
- **Suivis sanitaires** : diagnostic, traitement, guerison ; remontent dans le rapport sanitaire.

### 4.10 Commerce
- **Clients** : gestion des acheteurs (CRUD), telephone obligatoire et uniquement numerique, unicite du telephone, historique d'achats affiche sur la fiche.
- **Ventes** : creation en BROUILLON, ajout de plusieurs lignes (lot, quantite, prix), total recalcule en direct. Validation (statut VALIDEE, decrement de l'effectif). Annulation (statut ANNULEE, reintegration de l'effectif). Refus d'un prix unitaire a 0 et d'une quantite superieure au stock. Recu PDF telechargeable. La modification respecte l'identifiant (pas de doublon).

### 4.11 Finance - Depenses (ADMIN)
Enregistrement d'une depense (categorie optionnelle, montant, date), categorisation, total du mois. Refus d'un montant <= 0 avec message specifique. Les depenses alimentent le calcul du benefice.

### 4.12 Stocks
- **Ingredients** : gestion, seuil d'alerte, stock. La creation cree un mouvement d'entree initial.
- **Mouvements de stock** : entrees / sorties. Une sortie superieure au stock est refusee. Un mouvement passant sous le seuil declenche une alerte de stock faible.

### 4.13 Tableau de bord
Indicateurs : nombre de lots actifs et de porcs actifs, groupes actifs et mises bas proches, ventes du mois, depenses du mois, benefice net (rouge si negatif), bandeau d'alertes prioritaires, tableaux "mises bas a venir" et "alertes stock", et graphiques (Finances, Reproduction, Cheptel sur 12 mois). Etats vides geres proprement.

### 4.14 Rapports
Generation de PDF : sanitaire, financier, commercial, reproduction. Export global Excel (CSV).

### 4.15 Import / Export (ADMIN)
- **Import CSV** des modules Clients, Ingredients, Lots, Vaccinations. L'import **passe par la logique metier** (memes validations et effets de bord que la saisie manuelle) : par exemple, importer un lot ACHAT cree la depense correspondante et initialise la repartition reproductive. Les codes auto-generes ne sont pas demandes ; les references (race, code lot, vaccin) servent a relier les enregistrements. Un **rapport ligne par ligne** indique les lignes importees et celles rejetees avec la raison.
- **Export** Excel (CSV) et PDF par module.
- **Historique** des imports/exports date et trace.

## 5. Base de donnees (tables reelles)

| Domaine | Tables |
| ------- | ------ |
| Securite | utilisateurs, roles |
| Cheptel | lots_porcs, mouvements_lots_porcs, pesees_lots, races, parametres_reproduction_race |
| Reproduction | groupes_reproduction, statuts_reproductifs, repartitions_reproductives_lots, analyses reproductives (par lot), alertes_reproduction |
| Sante | vaccins, vaccinations, maladies, traitements, suivis_sanitaires |
| Commerce | clients, ventes, details_vente |
| Finance | depenses, categories_depenses |
| Stocks | ingredients, mouvements_stock_aliment |
| Tracabilite | imports_exports |

## 6. Fonctionnalites additionnelles (au-dela du besoin initial)

Ces fonctionnalites ont ete ajoutees par l'equipe et ne figuraient pas dans le cahier des charges initial :

- **Notifications temps reel (SSE)** avec toasts, sur toutes les pages.
- **Calendrier des evenements** (mises bas prevues, depenses, ventes validees, deces) avec navigation par mois ; clic sur un evenement -> page de detail (reserve a l'ADMIN).
- **Systeme d'alertes de reproduction** automatique (mises bas proches / en retard) avec **e-mail recapitulatif journalier** (un seul envoi par jour) et badge de menu.
- **Menu lateral repliable (accordeon)** ; la section de la page courante s'ouvre automatiquement.
- **Filtre de recherche live** des listes + bouton "Rechercher".
- **Recu PDF** de vente.
- **Gestion d'erreurs globale** : toute exception donne une page d'erreur propre (pas d'ecran blanc), y compris pour les saisies de type invalide.
- **Import via la logique metier** avec rapport de rejets ligne par ligne.
- **Depense d'achat automatique** a la creation/import d'un lot ACHAT, synchronisee a la modification du prix.

## 7. Ecarts assumes avec le cahier des charges initial

| Sujet initial | Choix retenu dans le projet fini |
| ------------- | -------------------------------- |
| Reproducteurs individuels (truies, verrats) + cycles de production | Reproduction geree **par groupes** (lot femelle x lot male) et analyse au niveau du **lot**. Modele batch, plus adapte a une conduite en bandes. |
| Module Gestion du Personnel (employes, presences) | **Non implemente** (hors perimetre retenu). |
| Import "reproducteurs" | Remplace par l'import des **lots** et des **vaccinations** (references par code). |
| Export "Excel" | Fourni au format **CSV** (ouvrable dans Excel) via telechargement direct. |

## 8. Criteres de conformite (projet fini)

Le systeme est considere conforme a la presente specification lorsque :
- la tracabilite complete d'un lot est possible (mouvements, pesees, mises bas, ventes) ;
- les indicateurs reproductifs sont calcules automatiquement et une recommandation (Apte / A surveiller / Reforme) est fournie par lot ;
- les femelles a surveiller / a reformer sont detectees ;
- les exports CSV et PDF fonctionnent ;
- les imports CSV fonctionnent avec rapport des rejets ;
- les rapports sont generes correctement ;
- aucune action utilisateur ne provoque d'ecran blanc (gestion d'erreurs globale).
