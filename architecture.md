madaporc/
│
├── pom.xml
├── README.md
├── .gitignore
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── madaporc/
│   │   │           ├── MadaporcApplication.java
│   │   │           │
│   │   │           ├── common/
│   │   │           │   └── AppConstants.java
│   │   │           │
│   │   │           ├── controller/
│   │   │           │   ├── BaseController.java
│   │   │           │   ├── AuthController.java
│   │   │           │   ├── DashboardController.java
│   │   │           │   ├── LotPorcController.java
│   │   │           │   ├── MouvementLotController.java
│   │   │           │   ├── PeseeLotController.java
│   │   │           │   ├── GroupeReproductionController.java
│   │   │           │   ├── AnalyseReproductionController.java
│   │   │           │   ├── AlerteReproductionController.java
│   │   │           │   ├── UtilisateurController.java
│   │   │           │   ├── ClientController.java
│   │   │           │   ├── VenteController.java
│   │   │           │   ├── VaccinController.java
│   │   │           │   ├── VaccinationController.java
│   │   │           │   ├── SuiviSanitaireController.java
│   │   │           │   ├── IngredientController.java
│   │   │           │   ├── MouvementStockController.java
│   │   │           │   ├── DepenseController.java
│   │   │           │   ├── ImportExportController.java
│   │   │           │   └── RapportController.java
│   │   │           │
│   │   │           ├── service/
│   │   │           │   ├── AuthService.java
│   │   │           │   ├── UtilisateurService.java
│   │   │           │   ├── DashboardService.java
│   │   │           │   ├── LotPorcService.java
│   │   │           │   ├── MouvementLotService.java
│   │   │           │   ├── PeseeLotService.java
│   │   │           │   ├── GroupeReproductionCreationService.java
│   │   │           │   ├── GroupeReproductionQueryService.java
│   │   │           │   ├── GroupeReproductionMiseBasService.java
│   │   │           │   ├── LotNaissanceService.java
│   │   │           │   ├── AnalyseReproductionService.java
│   │   │           │   ├── RepartitionReproductiveService.java
│   │   │           │   ├── AlerteReproductionService.java
│   │   │           │   ├── VaccinService.java
│   │   │           │   ├── VaccinationService.java
│   │   │           │   ├── SuiviSanitaireService.java
│   │   │           │   ├── VenteService.java
│   │   │           │   ├── StockService.java
│   │   │           │   ├── ImportExportService.java
│   │   │           │   └── RapportService.java
│   │   │           │
│   │   │           ├── repository/
│   │   │           │   ├── UtilisateurRepository.java
│   │   │           │   ├── RoleRepository.java
│   │   │           │   ├── RaceRepository.java
│   │   │           │   ├── LotPorcRepository.java
│   │   │           │   ├── MouvementLotPorcRepository.java
│   │   │           │   ├── PeseeLotRepository.java
│   │   │           │   ├── GroupeReproductionRepository.java
│   │   │           │   ├── RepartitionReproductiveLotRepository.java
│   │   │           │   ├── StatutReproductifRepository.java
│   │   │           │   ├── AnalyseReproductionLotRepository.java
│   │   │           │   ├── AlerteReproductionRepository.java
│   │   │           │   ├── VaccinRepository.java
│   │   │           │   ├── VaccinationRepository.java
│   │   │           │   ├── SuiviSanitaireRepository.java
│   │   │           │   ├── ClientRepository.java
│   │   │           │   ├── VenteRepository.java
│   │   │           │   ├── DetailVenteRepository.java
│   │   │           │   ├── IngredientRepository.java
│   │   │           │   ├── DepenseRepository.java
│   │   │           │   └── ImportExportRepository.java
│   │   │           │
│   │   │           ├── model/
│   │   │           │   ├── Role.java
│   │   │           │   ├── Utilisateur.java
│   │   │           │   ├── Race.java
│   │   │           │   ├── ParametreReproductionRace.java
│   │   │           │   ├── LotPorc.java
│   │   │           │   ├── MouvementLotPorc.java
│   │   │           │   ├── PeseeLot.java
│   │   │           │   ├── StatutReproductif.java
│   │   │           │   ├── RepartitionReproductiveLot.java
│   │   │           │   ├── GroupeReproduction.java
│   │   │           │   ├── AnalyseReproductionLot.java
│   │   │           │   ├── AlerteReproduction.java
│   │   │           │   ├── Vaccin.java
│   │   │           │   ├── Vaccination.java
│   │   │           │   ├── Maladie.java
│   │   │           │   ├── Traitement.java
│   │   │           │   ├── SuiviSanitaire.java
│   │   │           │   ├── Client.java
│   │   │           │   ├── Vente.java
│   │   │           │   ├── DetailVente.java
│   │   │           │   ├── Ingredient.java
│   │   │           │   ├── Depense.java
│   │   │           │   └── ImportExport.java
│   │   │           │
│   │   │           └── dto/
│   │   │               ├── LoginDTO.java
│   │   │               ├── UtilisateurDTO.java
│   │   │               ├── LotPorcDTO.java
│   │   │               ├── MouvementLotDTO.java
│   │   │               ├── PeseeLotDTO.java
│   │   │               ├── GroupeReproductionDTO.java
│   │   │               ├── ConfirmationMiseBasDTO.java
│   │   │               ├── LotNaissanceDTO.java
│   │   │               ├── AnalyseReproductionLotDTO.java
│   │   │               ├── DashboardDTO.java
│   │   │               ├── VenteDTO.java
│   │   │               ├── DetailVenteDTO.java
│   │   │               ├── ImportExcelDTO.java
│   │   │               └── RapportFiltreDTO.java
│   │   │
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   ├── schema.sql
│   │   │   ├── data.sql
│   │   │   └── static/
│   │   │       └── css/
│   │   │           └── app.css
│   │   │
│   │   └── webapp/
│   │       └── WEB-INF/
│   │           └── views/
│   │               ├── login.jsp
│   │               ├── placeholder.jsp
│   │               │
│   │               ├── layout/
│   │               │   ├── header.jsp
│   │               │   ├── sidebar.jsp
│   │               │   └── footer.jsp
│   │               │
│   │               ├── dashboard/
│   │               │   └── index.jsp
│   │               │
│   │               ├── lots/
│   │               │   ├── listeLots.jsp
│   │               │   ├── formLot.jsp
│   │               │   ├── detailLot.jsp
│   │               │   ├── mouvements.jsp
│   │               │   └── pesees.jsp
│   │               │
│   │               ├── reproduction/
│   │               │   ├── groupes/
│   │               │   │   ├── liste.jsp
│   │               │   │   ├── form.jsp
│   │               │   │   ├── detail.jsp
│   │               │   │   └── confirmerMiseBas.jsp
│   │               │   ├── analyse/
│   │               │   │   └── analyseLot.jsp
│   │               │   └── alertes/
│   │               │       └── liste.jsp
│   │               │
│   │               ├── sante/
│   │               ├── commerce/
│   │               ├── stocks/
│   │               ├── finance/
│   │               ├── imports/
│   │               └── rapports/
│   │
│   └── test/
│       └── java/
│           └── com/
│               └── madaporc/
│                   └── MadaporcApplicationTests.java