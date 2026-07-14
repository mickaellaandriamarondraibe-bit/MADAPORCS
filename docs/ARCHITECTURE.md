# Architecture du projet MADAPORC

Document de référence sur l'architecture réelle du code (généré à partir du dépôt).
Application web de gestion d'élevage porcin, construite en **Spring Boot MVC** avec un rendu de pages **JSP** côté serveur et une base **PostgreSQL**.

---

## 1. Vue d'ensemble

Le projet suit une architecture **MVC en couches**. Une requête traverse toujours les mêmes couches, dans le même ordre. Chaque couche a une seule responsabilité et ne parle qu'à la couche voisine.

```
NAVIGATEUR (HTML / CSS / JS)
      |  requête HTTP
      v
DispatcherServlet (Spring MVC)  ---> AuthInterceptor (contrôle de session)
      |
      v
Couche CONTROLLER   @Controller      reçoit la requête, appelle le service, remplit le Model
      |  DTO / paramètres
      v
Couche SERVICE      @Service         règles métier, calculs, validation, @Transactional
      |  appels de méthodes
      v
Couche REPOSITORY   @Repository      interfaces Spring Data JPA (pas de SQL écrit à la main)
      |
      v
Hibernate / JPA (ORM)               traduit objet <-> table, génère le SQL
      |  SQL
      v
PostgreSQL                          exécute la requête, renvoie les lignes

Remontée : ResultSet -> Entity -> DTO -> Model -> vue JSP (JSTL/EL) -> HTML -> navigateur
```

Ce qui circule entre les couches :

| Sens | Entre | Objet qui circule |
|------|-------|-------------------|
| Aller | Navigateur -> Controller | requête HTTP (paramètres, chemin) |
| Aller | Controller -> Service | paramètres Java (`Long id`, DTO...) |
| Aller | Service -> Repository | appel de méthode (`findById(Long)`) |
| Aller | Hibernate -> PostgreSQL | requête `SQL` |
| Retour | PostgreSQL -> Hibernate | `ResultSet` (lignes) |
| Retour | Repository -> Service | `Entity` / `List<Entity>` |
| Retour | Service -> Controller | `DTO` (objet de transport) |
| Retour | Controller -> Vue | `Model` + nom de vue (`String`) |
| Retour | Vue -> Navigateur | page `HTML` |

---

## 2. Stack technique

| Élément | Choix | Détail |
|---------|-------|--------|
| Langage | Java | version 17 |
| Framework | Spring Boot | version 3.3.5 (`spring-boot-starter-parent`) |
| Build | Maven | `spring-boot-maven-plugin` |
| Web / MVC | `spring-boot-starter-web` | Tomcat embarqué |
| Vues | JSP + JSTL | `tomcat-embed-jasper`, JSTL Jakarta |
| Accès données | `spring-boot-starter-data-jpa` | Hibernate (ORM) |
| Base de données | PostgreSQL | driver `postgresql`, base `madaporc` |
| Sécurité | `spring-boot-starter-security` | `BCryptPasswordEncoder` (hash mots de passe) |
| Validation | `spring-boot-starter-validation` | validation des DTO |
| Email | `spring-boot-starter-mail` | notifications (SMTP Gmail) |
| Export PDF | `openhtmltopdf-pdfbox` | génération des rapports PDF |
| Outils | Lombok, DevTools | confort de développement |

Configuration clé (`application.properties`) :

- `server.port=8082`
- `spring.mvc.view.prefix=/WEB-INF/views/` et `spring.mvc.view.suffix=.jsp` (résolution des vues)
- `spring.datasource.url=jdbc:postgresql://localhost:5432/madaporc`
- `spring.jpa.hibernate.ddl-auto=update` (le schéma suit les entités)
- `spring.jpa.open-in-view=false` (session Hibernate fermée hors de la couche service)

---

## 3. Arborescence du code

```
src/main/java/com/madaporc/
├── controller/   (24)  couche présentation serveur : @Controller
├── service/      (30)  couche métier : @Service
├── repository/   (25)  couche accès données : interfaces Spring Data JPA
├── model/        (25)  entités JPA (@Entity) = tables PostgreSQL
├── dto/          (28)  objets de transport (Data Transfer Objects)
├── config/       (3)   SecurityConfig, WebConfig, AuthInterceptor
└── common/       (1)   AppConstants (constantes partagées)

src/main/webapp/WEB-INF/views/   43 pages JSP
├── layout/       header.jsp, sidebar.jsp, footer.jsp (gabarit commun)
├── dashboard/    lots/    reproduction/    sante/    stocks/
├── commerce/     finance/ rapports/        calendrier/ imports/  utilisateurs/

src/main/resources/
├── static/css/app.css      thème centralisé (bleu/blanc)
├── static/js/app.js        interactions front
├── application.properties  configuration
└── data.sql                données initiales (rôles, races, admin...)
```

---

## 4. Rôle exact de chaque couche

### 4.1 Controller (`com.madaporc.controller`)
- Annotés `@Controller`, mappent les URL (`@GetMapping`, `@PostMapping`).
- Reçoivent la requête, appellent un ou plusieurs services, placent des DTO dans le `Model`.
- Retournent un `String` = nom de la vue (résolu en `/WEB-INF/views/<nom>.jsp`).
- Ne contiennent **aucune** règle métier ni accès base.

### 4.2 Service (`com.madaporc.service`)
- Annotés `@Service`, `@Transactional` pour les écritures.
- Portent la logique métier : calculs, validation, coordination de plusieurs repositories, effets de bord (ex. création d'un lot -> dépense d'achat + mouvement de stock + répartition reproductive).
- Convertissent Entity <-> DTO.

### 4.3 Repository (`com.madaporc.repository`)
- **Interfaces** étendant `JpaRepository`. Aucune implémentation à écrire.
- Les méthodes dérivées (`findByCodeLot`, `findAllByOrderBy...`) sont traduites automatiquement en SQL par Spring Data / Hibernate.

### 4.4 Model (`com.madaporc.model`)
- Entités JPA annotées `@Entity` / `@Table`.
- **1 attribut Java = 1 colonne SQL** (`@Column`), relations via `@ManyToOne` / `@OneToMany`.
- Exemple : `LotPorc` -> table `lots_porcs`.

### 4.5 DTO (`com.madaporc.dto`)
- Transportent les données entre service et vue.
- Évitent d'exposer directement les entités et portent les données de formulaires / filtres / affichage.

---

## 5. Sécurité et éléments transverses

- **Authentification par session** (pas de `formLogin` Spring) : au login, `AuthService` vérifie le mot de passe (BCrypt) et pose en session `userId` et `roleNom`.
- **`AuthInterceptor`** (enregistré dans `WebConfig`) protège `/**` sauf les chemins publics (`/`, `/connexion`, `/logout`, `/css/**`, `/js/**`...). Il redirige vers le login sans session, ferme la session d'un compte désactivé, et restreint certains chemins au rôle `ADMIN`.
- **`SecurityConfig`** fournit le `BCryptPasswordEncoder` ; `formLogin` est désactivé et la protection effective est assurée par l'interceptor.
- **`GlobalExceptionHandler`** (`@ControllerAdvice`) : capture les exceptions (type invalide, `IllegalArgumentException`, générique) et affiche une page d'erreur propre.
- **`GlobalModelAdvice`** (`@ControllerAdvice`) : injecte des attributs communs à toutes les vues (ex. `nbAlertes` dans la barre latérale).
- **Vues** : gabarit commun `layout/` (header, sidebar, footer) réutilisé par tous les modules, JSTL + EL pour l'affichage dynamique.

---

## 6. Répartition par module fonctionnel

| Module | Controllers | Services | Entités principales |
|--------|-------------|----------|---------------------|
| Authentification / Utilisateurs | `AuthController`, `UtilisateurController` | `AuthService`, `UtilisateurService` | `Utilisateur`, `Role` |
| Lots de porcs | `LotPorcController`, `MouvementLotController`, `PeseeLotController` | `LotPorcService`, `MouvementLotService`, `PeseeLotService`, `LotNaissanceService`, `RepartitionReproductiveService` | `LotPorc`, `MouvementLotPorc`, `PeseeLot`, `RepartitionReproductiveLot`, `Race` |
| Reproduction | `GroupeReproductionController`, `AnalyseReproductionController`, `AlerteReproductionController` | `GroupeReproductionCreationService`, `GroupeReproductionMiseBasService`, `GroupeReproductionQueryService`, `AnalyseReproductionService`, `AlerteReproductionService` | `GroupeReproduction`, `AnalyseReproductionLot`, `AlerteReproduction`, `ParametreReproductionRace`, `StatutReproductif` |
| Santé / sanitaire | `SuiviSanitaireController`, `VaccinationController`, `VaccinController` | `SuiviSanitaireService`, `VaccinationService`, `VaccinService`, `MaladieService`, `TraitementService` | `SuiviSanitaire`, `Vaccination`, `Vaccin`, `Maladie`, `Traitement` |
| Stock alimentaire | `IngredientController`, `MouvementStockController` | `IngredientService`, `StockService`, `MouvementStockService` | `Ingredient`, `MouvementStock` |
| Commercial | `ClientController`, `VenteController` | `ClientService`, `VenteService` | `Client`, `Vente`, `DetailVente` |
| Finance / dépenses | `DepenseController` | `DepenseService`, `CategorieDepenseService` | `Depense`, `CategorieDepense` |
| Pilotage / transverse | `DashboardController`, `RapportController`, `CalendrierController`, `NotificationController`, `ImportExportController` | `DashboardService`, `RapportService`, `CalendrierService`, `NotificationService`, `EmailService`, `ImportExportService` | `ImportExport` |
| Technique (transverse) | `BaseController`, `GlobalExceptionHandler`, `GlobalModelAdvice` | — | — |

---

## 7. Exemple de flux complet : écran « Analyse de reproduction »

1. L'utilisateur clique sur un lien dans le navigateur.
2. Requête `HTTP GET /reproduction/analyse/{id}`.
3. Le `DispatcherServlet` route la requête ; `AuthInterceptor` vérifie la session.
4. `AnalyseReproductionController` reçoit la requête et appelle `AnalyseReproductionService`.
5. Le service applique les règles métier et interroge les repositories (`LotPorcRepository`, `AnalyseReproductionLotRepository`...).
6. Les repositories, via Hibernate, exécutent le `SQL` sur PostgreSQL.
7. Remontée : `ResultSet` -> `Entity` -> conversion en `DTO`.
8. Le contrôleur place le DTO dans le `Model` et retourne le nom de vue (`String`).
9. La JSP correspondante affiche les données avec JSTL / EL.
10. La page `HTML` est renvoyée au navigateur.

---

## 8. Avantages de cette architecture

- **Séparation des responsabilités** : chaque couche a un rôle unique.
- **Maintenance** : une modification dans une couche impacte peu les autres.
- **Réutilisabilité** : un service peut être appelé par plusieurs contrôleurs.
- **Testabilité** : les services se testent indépendamment des vues et de la base.
