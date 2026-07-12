# Rapport d'audit — Projet MADAPORC

Application Spring Boot / Java (JSP, Spring Data JPA, PostgreSQL) — module de gestion d'élevage porcin (lots, reproduction, ventes, stock d'aliments, sanitaire, finances).

---

## 1. Synthèse exécutive

L'audit du code révèle un projet fonctionnellement riche mais **non prêt pour la production**. Le socle de sécurité est quasi inexistant (Spring Security neutralisé, aucun contrôle de rôle, secrets committés), plusieurs calculs métier centraux sont faux (stock, effectifs, évolution du cheptel, aptitude reproductive), et l'absence généralisée de gestion transactionnelle et de verrouillage expose la base à des incohérences durables sous charge concurrente.

### Répartition par sévérité (après déduplication)

| Sévérité | Nombre |
|---|---|
| Critique | 4 |
| Majeur | 21 |
| Mineur | 27 |

### Répartition par catégorie (principaux thèmes)

| Catégorie | Constat dominant |
|---|---|
| Sécurité | Aucune autorisation par rôle, CSRF désactivé, session fixation, secrets en clair versionnés |
| Concurrence | Aucun `@Version`/verrou : lost updates et surventes sur effectif et stock |
| Intégrité des données | Écritures multi-entités non transactionnelles, imports CSV partiels, stock compté en double |
| Exactitude métier | Calculs de reproduction, d'évolution du cheptel et de suivi vaccinal erronés |
| Validation | Validation ad-hoc, `@Valid` quasi inexistant, contraintes DB atteintes en 500 |
| Configuration | `ddl-auto=update` + `schema.sql` + `continue-on-error` masquant les écarts de schéma |
| Tests | Couverture nulle (fichier de test vide, `spring-boot-starter-test` absent) |

### Verdict global

**NON production-ready.** Les 4 problèmes critiques (élévation de privilèges, fuite de secrets, double comptage de stock) doivent être corrigés avant tout déploiement, même interne. Le manque total de transactions et de verrous rend par ailleurs les données non fiables dès qu'il y a plus d'un utilisateur simultané.

---

## 2. Problèmes CRITIQUES

### C1 — Aucun contrôle de rôle : élévation de privilèges via la gestion des utilisateurs

- **Fichiers :** `src/main/java/com/madaporc/controller/UtilisateurController.java:53`, `src/main/java/com/madaporc/config/AuthInterceptor.java:22`, `src/main/java/com/madaporc/config/SecurityConfig.java:22`
- **Impact / scénario :** `SecurityConfig` applique `anyRequest().permitAll()` et l'`AuthInterceptor` ne vérifie que la présence de l'attribut de session `userId`, jamais `roleId` (pourtant posé au login). Aucun `@PreAuthorize`/`@Secured`. Un simple GESTIONNAIRE authentifié peut appeler `POST /utilisateurs/save` avec `roleId` = ADMIN pour se créer ou se promouvoir administrateur, ou `POST /utilisateurs/desactiver/{idAdmin}` pour désactiver l'admin légitime. `UtilisateurService.creer()`/`modifier()` assignent le rôle directement depuis le DTO client, sans restriction.
- **Correctif :** Porter l'autorisation dans Spring Security (`authorizeHttpRequests` + `hasRole('ADMIN')` sur `/utilisateurs/**`, `/imports/**`, `/depenses/**`) OU enrichir l'interceptor pour comparer le `roleId` de session au rôle requis par chemin. Ne jamais accepter le `roleId` fourni par le formulaire pour un non-admin.

### C2 — Stock initial compté en double à la création d'un ingrédient

- **Fichier :** `src/main/java/com/madaporc/service/IngredientService.java:64-78`
- **Impact / scénario :** `creerIngredient` fixe `ingredient.stockActuel = dto.getStockActuel()` et sauvegarde, PUIS crée un mouvement ENTREE de `quantite = dto.getStockActuel()`. `appliquerEntree` fait `stockActuel.add(quantite)`, ajoutant une seconde fois le stock initial. Créer « Maïs » avec 100 kg aboutit à un stock affiché de 200 kg (et `stock_apres=200` dans le mouvement). Reproductible sur tout stock initial > 0.
- **Correctif :** Une seule source doit fixer le stock. Soit poser `setStockActuel(BigDecimal.ZERO)` et laisser le mouvement ENTREE l'amener à la valeur saisie, soit conserver le stock et ne pas créer de mouvement.

### C3 — Secret Gmail (app-password) en clair et committé dans git

- **Fichier :** `src/main/resources/application.properties:32`
- **Impact / scénario :** `app.mail.mot-de-passe=znbt mfaw yhwi tgtm` est un app-password Google valide (format 16 caractères) en clair, dans un fichier suivi par git. L'expéditeur `noahtown011@gmail.com` et la config SMTP (host/port/starttls) sont opérationnels. Toute personne clonant le dépôt (collaborateur, fork, fuite) peut envoyer des e-mails au nom du compte. Le secret subsiste dans l'historique après suppression.
- **Correctif :** Révoquer immédiatement l'app-password dans le compte Google. Externaliser via variable d'environnement (`${APP_MAIL_PASSWORD}`), purger l'historique git (BFG / git-filter-repo) puis force-push, et fournir un `application.properties.example` sans secrets.

### C4 — Aucun `.gitignore` : `target/` et secrets versionnés

- **Fichier :** `.gitignore` (absent à la racine), `src/main/resources/application.properties`, `target/classes/application.properties`
- **Impact / scénario :** Le dépôt n'a aucun `.gitignore`. 144 fichiers `target/` sont suivis, dont `target/classes/application.properties` qui duplique les secrets. Le remote étant GitHub, l'app-password Gmail et les identifiants Postgres (`postgres/postgres`) sont exposés dans l'historique et dans toute copie/fork, y compris via les binaires compilés.
- **Correctif :** Créer un `.gitignore` (`target/`, `*.class`, `application.properties` ou profil local), exécuter `git rm -r --cached target/` et `git rm --cached src/main/resources/application.properties`, externaliser les secrets, purger l'historique.

---

## 3. Problèmes MAJEURS

| Catégorie | Fichier | Problème | Correctif |
|---|---|---|---|
| security | `config/SecurityConfig.java:18` | CSRF désactivé alors que l'auth repose sur cookie de session ; toutes les mutations POST sont sans jeton (forge de requêtes cross-site) | Réactiver la protection CSRF et inclure `_csrf` dans tous les formulaires JSP |
| security | `service/AuthService.java:28` | Session fixation : l'ID de session n'est pas régénéré après login | `changeSessionId()` (ou invalidate + nouvelle session) avant de poser les attributs |
| security | `service/UtilisateurService.java:88` | La désactivation d'un utilisateur n'invalide pas ses sessions actives (l'interceptor ne relit jamais `actif`) | Vérifier l'état `actif` dans l'interceptor et/ou invalider les sessions via un `SessionRegistry` |
| security | `config/AuthInterceptor.java:22` | Aucune séparation ADMIN/EMPLOYE : tous les modules (utilisateurs, dépenses, imports de masse) ouverts à tout compte connecté | Matrice rôles→URL centralisée (Spring Security ou interceptor par chemin) |
| config | `resources/application.properties:12` | `ddl-auto=update` + `schema.sql always` + `continue-on-error=true` : colonnes fantômes créées silencieusement (ex. `nb_a_retier_reproduction`), erreurs d'init masquées | Passer à `ddl-auto=validate`, retirer `continue-on-error`, une seule source de vérité du schéma |
| security | `resources/application.properties:9` | Identifiants Postgres (`postgres/postgres`) en clair et versionnés | Externaliser en `${DB_USER}`/`${DB_PASSWORD}`, retirer du fichier suivi |
| correctness | `service/AuthService.java:31` | Login réussi retourne la vue `placeholder` au lieu de rediriger vers `/dashboard` (URL reste POST `/connexion`, re-soumission au refresh) | Retourner `redirect:/dashboard` au succès |
| correctness | `service/AlerteReproductionService.java:92` | Une alerte de mise bas relue est recréée toutes les 60 s (`alerteExiste()` ne teste que les NON_LUE) | Rechercher l'existence de l'alerte tous statuts confondus pour ce groupe/type |
| correctness | `service/AnalyseReproductionService.java:137` | Un lot dont toutes les femelles sont EN_CYCLE (au dénominateur mais pas au numérateur) est classé « RÉFORME RECOMMANDÉE » | Inclure `nbEnCycle` au numérateur ou traiter EN_CYCLE explicitement dans `genererDecision` |
| correctness | `service/DashboardService.java:169` | Rappels de vaccination en retard jamais signalés (requête bornée à `now()`, aucun `@Scheduled`, statut affiché « REALISE ») | Requête des rappels en retard + génération d'alerte planifiée `VACCINATION_A_VENIR` |
| correctness | `service/VenteService.java:94` | L'édition d'une vente (`/ventes/form?id=X`) crée un doublon : `creerVente` fait toujours `new Vente()` et ignore `dto.getId()` | Charger la vente existante si `id != null`, vérifier statut BROUILLON, recréer les détails |
| correctness | `service/CalendrierService.java:73` | `LazyInitializationException` sur `/calendrier/events` dès qu'un mouvement DECES existe (accès lazy hors session, `open-in-view=false`) | Annoter `@Transactional(readOnly=true)` ou requête dédiée avec `JOIN FETCH lot` |
| correctness | `service/DashboardService.java:207` | Graphe d'évolution du cheptel faux : effectif COURANT appliqué rétroactivement, lots archivés/groupes clôturés exclus de tous les mois | Reconstruire l'état par mois depuis les mouvements historiques |
| correctness | `controller/VenteController.java:41` | `saveVente` ne gère aucune exception métier → page Whitelabel 500 pour une saisie normale (quantité > effectif, prix négatif) | `try/catch(IllegalArgumentException)` ré-affichant le formulaire, ou `@ControllerAdvice` global |
| concurrency | `service/MouvementLotService.java:66` | Lost update sur `effectif_actuel` (read-modify-write sans `@Version`/verrou) : effectif faux, survente, garde `>= 0` non déclenchée | `@Version` sur `LotPorc`, verrou pessimiste, ou UPDATE atomique conditionnel |
| concurrency | `service/MouvementStockService.java:82` | Lost update + TOCTOU sur `stock_actuel` : `verifierStockDisponible` recharge une entité distincte de celle mutée | `@Transactional` + verrou sur la même instance, ou UPDATE conditionnel atomique |
| concurrency | `service/VenteService.java:267` | Survente : double validation d'une même vente BROUILLON (idempotence non verrouillée) et TOCTOU création→validation | Recharger/verrouiller la vente en `PESSIMISTIC_WRITE`, poser VALIDEE avant décrément, ou UPDATE conditionnel sur statut |
| concurrency | `service/IngredientService.java:73` | Récupération de l'id via `findFirstByOrderByCreatedAtDesc()` au lieu de l'entité sauvée : mouvement/dépense rattachés au mauvais ingrédient en concurrence | Réutiliser `ingredientRepository.save(ingredient).getId()` |
| data-integrity | `service/RepartitionReproductiveService.java:58` | Répartition femelle calculée sur `effectifInitial` (jamais `effectifActuel`) : on peut engager plus de femelles qu'il n'en reste vivantes | Baser `nbLibres` sur `getEffectifActuel()` |
| data-integrity | `service/LotNaissanceService.java:85` | Répartition porcelets non bornée par `nb_porcelets_vivants` : 5 nés peuvent créer 100 porcs actifs | Rejeter `nbFemelles + nbMales > nbPorceletsVivants` |
| data-integrity | `service/MouvementStockService.java:45` | Écriture stock + dépense non atomique : échec de `creerDepense` laisse le stock augmenté sans dépense | Rendre `enregistrerMouvementStock` `@Transactional` |
| data-integrity | `service/GroupeReproductionCreationService.java:99` | Exception avalée dans un `@Transactional` (try/catch générique) : mutations de répartition committées sans groupe créé → femelles EN_CYCLE fantômes (déclenchable via NPE sur `dureeGestation` null) | Retirer le catch générique ou `setRollbackOnly()` / relancer une `RuntimeException` |
| data-integrity | `service/ImportExportService.java:112` | Import CSV non transactionnel : erreur en milieu de fichier → lignes précédentes committées, re-import = doublons/blocage sur `code_lot` UNIQUE | Annoter `importerCsv` `@Transactional` et valider toutes les lignes avant écriture |
| data-integrity | `service/ImportExportService.java:200` | Import vaccinations : `date_rappel < date_vaccination` non validée (bypass du service) → violation CHECK + import partiel | Valider `dateRappel >= dateVaccination` ou router via `VaccinationService.enregistrer()` |
| validation | `service/ImportExportService.java:167` | Import lots : `sexe`/`objectif`/`origine`/`effectif` non validés contre les valeurs autorisées → violation CHECK au save + message technique | Valider et normaliser chaque champ avant `save`, rejeter proprement la ligne |

---

## 4. Problèmes MINEURS (groupés par catégorie)

### Sécurité
- **Énumération d'utilisateurs par canal temporel** (`service/AuthService.java:27`) : short-circuit BCrypt quand l'email est inconnu/inactif ; pas de rate-limiting. → Comparaison contre un hash factice + limitation de tentatives.
- **Config Spring Security entièrement permissive** (`config/SecurityConfig.java:22`) : `anyRequest().permitAll()`, aucune défense en profondeur, point de défaillance unique sur l'interceptor. → Porter les règles réelles dans `authorizeHttpRequests`.

### Validation
- **Mot de passe null/vide non gardé à la création** (`service/UtilisateurService.java:43`) : `encode(null)` → 500 ; champ vide → compte à mot de passe vide. → `@NotBlank` + `@Valid`.
- **Guérison antérieure au traitement non rejetée** (`service/SuiviSanitaireService.java:75`) : `dateGuerison >= dateTraitement` jamais vérifié. → Ajouter la comparaison.
- **Aucune validation de date future** sur diagnostic/vaccination (`service/SuiviSanitaireService.java:66`, `service/VaccinationService.java:64`) : incohérent avec `PeseeLotService`. → Contrôle `isAfter(now())`.
- **`validerCreation` ne valide pas objectif/origine** (`service/LotPorcService.java:380`) : valeur non autorisée → 500 sur CHECK. → Valider contre l'ensemble autorisé.
- **Poids total négatif/nul non validé** (`service/VenteService.java:134`) : viole `CHECK poids_total > 0` → 500 non géré (bug latent, exploitable par requête forgée).
- **`@Valid` quasi inexistant** (`controller/DepenseController.java:61` et 15+ controllers) : validation ad-hoc par comparaison de String. → Annoter les DTO (JSR-380) + `@Valid`/`BindingResult`.

### Exactitude / correctness
- **Lot vide (effectif 0) reste ACTIF** (`service/MouvementLotService.java:73`) : statut « VIDE » jamais appliqué → comptages dashboard faussés, analyse repro sur 0 animal.
- **Annulation impossible si le lot a été archivé** (`service/VenteService.java:259`) : `enregistrerMouvement` refuse les mouvements sur lot ARCHIVE → stock jamais restitué.
- **Baseline de `calculerEvolutionPoids` non fiable** (`service/PeseeLotService.java:61`) : pesée antérieure à la création acceptée, pas de tie-break par id.
- **AJUSTEMENT traité comme SORTIE** (`service/MouvementStockService.java:54`) : type non validé, tout ce qui n'est pas ENTREE décrémente le stock.
- **GET `/clients/{id}` inexistant → 500** (`controller/ClientController.java:54`) : `model.addAttribute(null)` lève une exception.
- **Aucun gestionnaire d'exception global** (`controller/AnalyseReproductionController.java:31` et autres) : id périmé dans l'URL → Whitelabel 500 au lieu de 404. → `@ControllerAdvice`.
- **`savePesee` rappelle `listPesees(null)` → `findById(null)` crash** (`controller/PeseeLotController.java:55`).

### Concurrence / atomicité
- **Créations multiples non transactionnelles** (`service/IngredientService.java:70`) : ingrédient + mouvement + dépense sans `@Transactional`.
- **Find-or-create de catégorie sujet à une course** (`service/DepenseService.java:126`) : deux INSERT concurrents violent le UNIQUE `nom`. → Pré-créer les catégories ou rattraper la `DataIntegrityViolationException`.
- **Contrôle stock-disponible sans verrou** (`service/MouvementStockService.java:92`) : survente possible (variante mineure du lost update stock).

### Intégrité des données
- **Annulation de vente = mouvement ENTREE fictif** (`service/MouvementLotService.java:111`) : type codé en dur, piste d'audit faussée. → Type dédié (AJUSTEMENT) + libellé explicite.
- **`modifierIngredient` écrase `stock_actuel` sans mouvement** (`service/IngredientService.java:93`) : grand livre de stock désynchronisé.
- **ENTREE de stock sans montant → dépense non enregistrée silencieusement** (`service/MouvementStockService.java:48`) : `creerDepense(null)` retourne null ; touche aussi la création d'ingrédient. → Exiger `montant > 0` pour ENTREE.

### Import/Export CSV
- **Parsing CSV ne gère pas les guillemets** (`service/ImportExportService.java:339`) : incohérent avec l'échappement à l'export, round-trip cassé. → Parseur CSV réel (OpenCSV/Commons CSV).
- **En-têtes du modèle d'import LOTS incohérents avec l'export** (`service/ImportExportService.java:286`) : colonnes décalées au ré-import.

### Performance
- **Notifications « stock faible » / « vaccination à venir » ré-émises à chaque chargement du dashboard** (`service/DashboardService.java:164`, `:174`) : effet de bord dans des getters, spam SSE.
- **Requêtes repository dupliquées 2-3 fois par appel** (`service/DashboardService.java:161`) : stocker le résultat dans une variable.
- **`CalendrierService` charge des tables entières et filtre en mémoire** (`service/CalendrierService.java:71`) : `findAll()` + N+1 sur `getLot()`. → Requêtes ciblées `findByTypeMouvement("DECES")` + `JOIN FETCH`.

### Code mort
- **`StockService` : duplication de `DepenseService`, jamais injecté** (`service/StockService.java:14`). → Supprimer.
- **`BaseController` vide** (`controller/BaseController.java:1`). → Supprimer.

### Couverture de tests
- **Aucun test réel** (`src/test/java/com/madaporc/MadaporcApplicationTests.java:1`) : fichier de test vide (0 octet) et `spring-boot-starter-test` absent du `pom.xml`. Couverture = 0. → Ajouter la dépendance de test + un `contextLoads()` et des tests unitaires sur les services de calcul.

---

## 5. Points forts / ce qui est correct

- **Contraintes CHECK en base bien posées** (`schema.sql`) : `effectif_initial > 0`, `stock_actuel >= 0`, `poids_total > 0`, énumérations sexe/objectif/origine, cohérence des dates sanitaires. Elles rattrapent une partie des défauts de validation applicative (aucune corruption silencieuse pour ces cas — l'insert échoue).
- **Hachage des mots de passe via BCrypt** (`SecurityConfig.java:33`) : les mots de passe ne sont pas stockés en clair.
- **Message d'erreur de login générique** (`AuthService`) : bloque l'énumération triviale d'utilisateurs (le canal temporel résiduel reste mineur).
- **Certains controllers gèrent proprement les erreurs métier** via `RedirectAttributes`/`BindingResult` (MouvementStock, Ingredient, GroupeReproduction) — ce qui montre que les manques ailleurs (VenteController) sont des oublis rattrapables.
- **Emails de rappel plafonnés à 1/jour** (`emailDejaEnvoyeAujourdHui`) : limite l'impact du respawn d'alertes côté e-mail.
- **Architecture en couches claire** (controller / service / repository / DTO) et cohérente, facilitant les corrections ciblées.
- **Un `@Scheduled` existant** pour les alertes de mise bas : le mécanisme planifié est en place et réutilisable pour les vaccinations.

---

## 6. Recommandations priorisées (top 5)

1. **Purger et externaliser tous les secrets, ajouter un `.gitignore`** (C3, C4). Révoquer l'app-password Gmail et les credentials Postgres, les passer en variables d'environnement, nettoyer l'historique git, ignorer `target/` et `application.properties`. Action de sécurité la plus urgente car déjà matérialisée sur GitHub.

2. **Rétablir une autorisation par rôle** (C1, majeurs sécurité). Réactiver Spring Security (`authorizeHttpRequests` + `hasRole`), réactiver le CSRF, régénérer la session au login, refuser le `roleId` client pour les non-admins. Cela ferme l'élévation de privilèges et la majorité des failles d'authentification d'un seul chantier.

3. **Corriger le double comptage du stock initial** (C2) et la désynchronisation du grand livre de stock (`modifierIngredient`, ENTREE sans montant). Défaut de données visible dès la première utilisation.

4. **Introduire transactions et verrouillage** sur les écritures multi-entités et les compteurs partagés : `@Transactional` sur `enregistrerMouvementStock`, `creerIngredient`, les imports CSV et `GroupeReproductionCreationService.creer` ; `@Version` (ou UPDATE atomique conditionnel) sur `LotPorc` et `Ingredient`. Élimine lost updates, surventes et états partiels.

5. **Fiabiliser les calculs métier et l'UX d'erreur** : rediriger le login vers `/dashboard`, baser la répartition/analyse reproductive sur `effectifActuel`, corriger l'évolution du cheptel et les rappels vaccinaux, et ajouter un `@ControllerAdvice` global (404/messages propres au lieu de 500). En parallèle, initialiser une base de tests (`spring-boot-starter-test` + tests des services de calcul) pour prévenir les régressions.
