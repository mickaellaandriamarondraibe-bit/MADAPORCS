# Liste des fichiers créés selon le PDF
## 8.1 - Connexion
- Controller : `src/main/java/com/madaporc/controller/LoginController.java`
- Service : `src/main/java/com/madaporc/service/UtilisateurService.java`
- DTO : `src/main/java/com/madaporc/DTO/LoginDTO.java`
- Model : `src/main/java/com/madaporc/model/Utilisateur.java`
- Repository : `src/main/java/com/madaporc/repository/UtilisateurRepository.java`
- Model : `src/main/java/com/madaporc/model/Role.java`
- Repository : `src/main/java/com/madaporc/repository/RoleRepository.java`
- Model : `src/main/java/com/madaporc/model/StatutUtilisateur.java`
- Repository : `src/main/java/com/madaporc/repository/StatutUtilisateurRepository.java`
- Template : `src/main/resources/templates/auth/login.html`

## 8.2 - Tableau de bord
- Controller : `src/main/java/com/madaporc/controller/DashboardController.java`
- Service : `src/main/java/com/madaporc/service/DashboardService.java`
- DTO : `src/main/java/com/madaporc/DTO/DashboardDTO.java`
- Template : `src/main/resources/templates/dashboard/index.html`

## 8.3-8.5 - Lots de porcs
- Controller : `src/main/java/com/madaporc/controller/LotPorcController.java`
- Service : `src/main/java/com/madaporc/service/LotPorcService.java`
- DTO : `src/main/java/com/madaporc/DTO/LotPorcDTO.java`
- Model : `src/main/java/com/madaporc/model/LotPorc.java`
- Repository : `src/main/java/com/madaporc/repository/LotPorcRepository.java`
- Model : `src/main/java/com/madaporc/model/Race.java`
- Repository : `src/main/java/com/madaporc/repository/RaceRepository.java`
- Model : `src/main/java/com/madaporc/model/StatutLot.java`
- Repository : `src/main/java/com/madaporc/repository/StatutLotRepository.java`
- Template : `src/main/resources/templates/lots/list.html`
- Template : `src/main/resources/templates/lots/form.html`
- Template : `src/main/resources/templates/lots/detail.html`

## 8.6 - Mouvements des lots
- Controller : `src/main/java/com/madaporc/controller/MouvementLotController.java`
- Service : `src/main/java/com/madaporc/service/MouvementLotService.java`
- DTO : `src/main/java/com/madaporc/DTO/MouvementLotDTO.java`
- Model : `src/main/java/com/madaporc/model/MouvementLotPorc.java`
- Repository : `src/main/java/com/madaporc/repository/MouvementLotPorcRepository.java`
- Model : `src/main/java/com/madaporc/model/TypeMouvementLot.java`
- Repository : `src/main/java/com/madaporc/repository/TypeMouvementLotRepository.java`
- Template : `src/main/resources/templates/mouvements_lots/list.html`
- Template : `src/main/resources/templates/mouvements_lots/form.html`

## 8.7 - Pesées des lots
- Controller : `src/main/java/com/madaporc/controller/PeseeLotController.java`
- Service : `src/main/java/com/madaporc/service/PeseeLotService.java`
- DTO : `src/main/java/com/madaporc/DTO/PeseeLotDTO.java`
- Model : `src/main/java/com/madaporc/model/PeseeLot.java`
- Repository : `src/main/java/com/madaporc/repository/PeseeLotRepository.java`
- Template : `src/main/resources/templates/pesees_lots/list.html`
- Template : `src/main/resources/templates/pesees_lots/form.html`

## 8.8-8.9 - Reproducteurs
- Controller : `src/main/java/com/madaporc/controller/ReproducteurController.java`
- Service : `src/main/java/com/madaporc/service/ReproducteurService.java`
- DTO : `src/main/java/com/madaporc/DTO/ReproducteurDTO.java`
- Model : `src/main/java/com/madaporc/model/Reproducteur.java`
- Repository : `src/main/java/com/madaporc/repository/ReproducteurRepository.java`
- Model : `src/main/java/com/madaporc/model/Sexe.java`
- Repository : `src/main/java/com/madaporc/repository/SexeRepository.java`
- Model : `src/main/java/com/madaporc/model/StatutReproducteur.java`
- Repository : `src/main/java/com/madaporc/repository/StatutReproducteurRepository.java`
- Template : `src/main/resources/templates/reproducteurs/list.html`
- Template : `src/main/resources/templates/reproducteurs/form.html`
- Template : `src/main/resources/templates/reproducteurs/detail.html`

## 8.10 - Cycles de production
- Controller : `src/main/java/com/madaporc/controller/CycleProductionController.java`
- Service : `src/main/java/com/madaporc/service/CycleProductionService.java`
- DTO : `src/main/java/com/madaporc/DTO/CycleProductionDTO.java`
- Model : `src/main/java/com/madaporc/model/CycleProduction.java`
- Repository : `src/main/java/com/madaporc/repository/CycleProductionRepository.java`
- Template : `src/main/resources/templates/cycles/list.html`
- Template : `src/main/resources/templates/cycles/form.html`

## 8.11 - Événements de reproduction
- Controller : `src/main/java/com/madaporc/controller/EvenementReproductionController.java`
- Service : `src/main/java/com/madaporc/service/EvenementReproductionService.java`
- DTO : `src/main/java/com/madaporc/DTO/EvenementReproductionDTO.java`
- Model : `src/main/java/com/madaporc/model/EvenementReproduction.java`
- Repository : `src/main/java/com/madaporc/repository/EvenementReproductionRepository.java`
- Model : `src/main/java/com/madaporc/model/TypeEvenementReproduction.java`
- Repository : `src/main/java/com/madaporc/repository/TypeEvenementReproductionRepository.java`
- Template : `src/main/resources/templates/evenements_reproduction/list.html`
- Template : `src/main/resources/templates/evenements_reproduction/form.html`

## 8.12 - Clients / acheteurs
- Controller : `src/main/java/com/madaporc/controller/ClientController.java`
- Service : `src/main/java/com/madaporc/service/ClientService.java`
- DTO : `src/main/java/com/madaporc/DTO/ClientDTO.java`
- Model : `src/main/java/com/madaporc/model/Client.java`
- Repository : `src/main/java/com/madaporc/repository/ClientRepository.java`
- Template : `src/main/resources/templates/clients/list.html`
- Template : `src/main/resources/templates/clients/form.html`
- Template : `src/main/resources/templates/clients/detail.html`

## 8.13 - Ventes
- Controller : `src/main/java/com/madaporc/controller/VenteController.java`
- Service : `src/main/java/com/madaporc/service/VenteService.java`
- DTO : `src/main/java/com/madaporc/DTO/VenteDTO.java`
- Model : `src/main/java/com/madaporc/model/Vente.java`
- Repository : `src/main/java/com/madaporc/repository/VenteRepository.java`
- Model : `src/main/java/com/madaporc/model/DetailVente.java`
- Repository : `src/main/java/com/madaporc/repository/DetailVenteRepository.java`
- Template : `src/main/resources/templates/ventes/list.html`
- Template : `src/main/resources/templates/ventes/form.html`
- Template : `src/main/resources/templates/ventes/detail.html`

## 8.14 - Paiements et factures
- Controller : `src/main/java/com/madaporc/controller/PaiementFactureController.java`
- Service : `src/main/java/com/madaporc/service/PaiementFactureService.java`
- DTO : `src/main/java/com/madaporc/DTO/PaiementFactureDTO.java`
- Model : `src/main/java/com/madaporc/model/Paiement.java`
- Repository : `src/main/java/com/madaporc/repository/PaiementRepository.java`
- Model : `src/main/java/com/madaporc/model/Facture.java`
- Repository : `src/main/java/com/madaporc/repository/FactureRepository.java`
- Template : `src/main/resources/templates/paiements_factures/list.html`
- Template : `src/main/resources/templates/paiements_factures/form.html`

## 8.15 - Dépenses
- Controller : `src/main/java/com/madaporc/controller/DepenseController.java`
- Service : `src/main/java/com/madaporc/service/DepenseService.java`
- DTO : `src/main/java/com/madaporc/DTO/DepenseDTO.java`
- Model : `src/main/java/com/madaporc/model/Depense.java`
- Repository : `src/main/java/com/madaporc/repository/DepenseRepository.java`
- Model : `src/main/java/com/madaporc/model/CategorieDepense.java`
- Repository : `src/main/java/com/madaporc/repository/CategorieDepenseRepository.java`
- Template : `src/main/resources/templates/depenses/list.html`
- Template : `src/main/resources/templates/depenses/form.html`

## 8.16 - Aliments et ingrédients
- Controller : `src/main/java/com/madaporc/controller/IngredientController.java`
- Service : `src/main/java/com/madaporc/service/IngredientService.java`
- DTO : `src/main/java/com/madaporc/DTO/IngredientDTO.java`
- Model : `src/main/java/com/madaporc/model/Ingredient.java`
- Repository : `src/main/java/com/madaporc/repository/IngredientRepository.java`
- Template : `src/main/resources/templates/ingredients/list.html`
- Template : `src/main/resources/templates/ingredients/form.html`

## 8.17 - Mouvements de stock alimentaire
- Controller : `src/main/java/com/madaporc/controller/MouvementStockController.java`
- Service : `src/main/java/com/madaporc/service/MouvementStockService.java`
- DTO : `src/main/java/com/madaporc/DTO/MouvementStockDTO.java`
- Model : `src/main/java/com/madaporc/model/MouvementStockAliment.java`
- Repository : `src/main/java/com/madaporc/repository/MouvementStockAlimentRepository.java`
- Model : `src/main/java/com/madaporc/model/TypeMouvementStock.java`
- Repository : `src/main/java/com/madaporc/repository/TypeMouvementStockRepository.java`
- Template : `src/main/resources/templates/mouvements_stock/list.html`
- Template : `src/main/resources/templates/mouvements_stock/form.html`

## 8.18 - Mélanges alimentaires
- Controller : `src/main/java/com/madaporc/controller/MelangeController.java`
- Service : `src/main/java/com/madaporc/service/MelangeService.java`
- DTO : `src/main/java/com/madaporc/DTO/MelangeDTO.java`
- Model : `src/main/java/com/madaporc/model/Melange.java`
- Repository : `src/main/java/com/madaporc/repository/MelangeRepository.java`
- Model : `src/main/java/com/madaporc/model/MelangeIngredient.java`
- Repository : `src/main/java/com/madaporc/repository/MelangeIngredientRepository.java`
- Template : `src/main/resources/templates/melanges/list.html`
- Template : `src/main/resources/templates/melanges/form.html`
- Template : `src/main/resources/templates/melanges/detail.html`

## 8.19 - Distribution des aliments
- Controller : `src/main/java/com/madaporc/controller/DistributionAlimentController.java`
- Service : `src/main/java/com/madaporc/service/DistributionAlimentService.java`
- DTO : `src/main/java/com/madaporc/DTO/DistributionAlimentDTO.java`
- Model : `src/main/java/com/madaporc/model/DistributionAliment.java`
- Repository : `src/main/java/com/madaporc/repository/DistributionAlimentRepository.java`
- Template : `src/main/resources/templates/distributions_aliment/list.html`
- Template : `src/main/resources/templates/distributions_aliment/form.html`

## 8.20 - Vaccins
- Controller : `src/main/java/com/madaporc/controller/VaccinController.java`
- Service : `src/main/java/com/madaporc/service/VaccinService.java`
- DTO : `src/main/java/com/madaporc/DTO/VaccinDTO.java`
- Model : `src/main/java/com/madaporc/model/Vaccin.java`
- Repository : `src/main/java/com/madaporc/repository/VaccinRepository.java`
- Template : `src/main/resources/templates/vaccins/list.html`
- Template : `src/main/resources/templates/vaccins/form.html`

## 8.21 - Vaccinations
- Controller : `src/main/java/com/madaporc/controller/VaccinationController.java`
- Service : `src/main/java/com/madaporc/service/VaccinationService.java`
- DTO : `src/main/java/com/madaporc/DTO/VaccinationDTO.java`
- Model : `src/main/java/com/madaporc/model/Vaccination.java`
- Repository : `src/main/java/com/madaporc/repository/VaccinationRepository.java`
- Template : `src/main/resources/templates/vaccinations/list.html`
- Template : `src/main/resources/templates/vaccinations/form.html`

## 8.22 - Suivi sanitaire
- Controller : `src/main/java/com/madaporc/controller/SuiviSanitaireController.java`
- Service : `src/main/java/com/madaporc/service/SuiviSanitaireService.java`
- DTO : `src/main/java/com/madaporc/DTO/SuiviSanitaireDTO.java`
- Model : `src/main/java/com/madaporc/model/SuiviSanitaire.java`
- Repository : `src/main/java/com/madaporc/repository/SuiviSanitaireRepository.java`
- Model : `src/main/java/com/madaporc/model/Maladie.java`
- Repository : `src/main/java/com/madaporc/repository/MaladieRepository.java`
- Model : `src/main/java/com/madaporc/model/Traitement.java`
- Repository : `src/main/java/com/madaporc/repository/TraitementRepository.java`
- Model : `src/main/java/com/madaporc/model/StatutSuiviSanitaire.java`
- Repository : `src/main/java/com/madaporc/repository/StatutSuiviSanitaireRepository.java`
- Template : `src/main/resources/templates/suivis_sanitaires/list.html`
- Template : `src/main/resources/templates/suivis_sanitaires/form.html`

## 8.23 - Employés
- Controller : `src/main/java/com/madaporc/controller/EmployeController.java`
- Service : `src/main/java/com/madaporc/service/EmployeService.java`
- DTO : `src/main/java/com/madaporc/DTO/EmployeDTO.java`
- Model : `src/main/java/com/madaporc/model/Employe.java`
- Repository : `src/main/java/com/madaporc/repository/EmployeRepository.java`
- Model : `src/main/java/com/madaporc/model/PosteEmploye.java`
- Repository : `src/main/java/com/madaporc/repository/PosteEmployeRepository.java`
- Model : `src/main/java/com/madaporc/model/StatutEmploye.java`
- Repository : `src/main/java/com/madaporc/repository/StatutEmployeRepository.java`
- Template : `src/main/resources/templates/employes/list.html`
- Template : `src/main/resources/templates/employes/form.html`
- Template : `src/main/resources/templates/employes/detail.html`

## 8.24 - Présences et pointage
- Controller : `src/main/java/com/madaporc/controller/PresenceController.java`
- Service : `src/main/java/com/madaporc/service/PresenceService.java`
- DTO : `src/main/java/com/madaporc/DTO/PresenceDTO.java`
- Model : `src/main/java/com/madaporc/model/Presence.java`
- Repository : `src/main/java/com/madaporc/repository/PresenceRepository.java`
- Template : `src/main/resources/templates/presences/list.html`
- Template : `src/main/resources/templates/presences/form.html`

## 8.25 - Salaires employés
- Controller : `src/main/java/com/madaporc/controller/SalaireEmployeController.java`
- Service : `src/main/java/com/madaporc/service/SalaireEmployeService.java`
- DTO : `src/main/java/com/madaporc/DTO/SalaireEmployeDTO.java`
- Model : `src/main/java/com/madaporc/model/SalaireEmploye.java`
- Repository : `src/main/java/com/madaporc/repository/SalaireEmployeRepository.java`
- Template : `src/main/resources/templates/salaires/list.html`
- Template : `src/main/resources/templates/salaires/form.html`

## 8.26 - Rapports
- Controller : `src/main/java/com/madaporc/controller/RapportController.java`
- Service : `src/main/java/com/madaporc/service/RapportService.java`
- DTO : `src/main/java/com/madaporc/DTO/RapportDTO.java`
- Template : `src/main/resources/templates/rapports/index.html`

## 8.27 - Profil utilisateur
- Controller : `src/main/java/com/madaporc/controller/ProfilController.java`
- Service : `src/main/java/com/madaporc/service/ProfilService.java`
- DTO : `src/main/java/com/madaporc/DTO/ProfilDTO.java`
- Template : `src/main/resources/templates/profil/index.html`
- Template : `src/main/resources/templates/profil/form.html`

## 8.28 - Utilisateurs et rôles
- Controller : `src/main/java/com/madaporc/controller/UtilisateurController.java`
- Service : `src/main/java/com/madaporc/service/AdminUtilisateurService.java`
- DTO : `src/main/java/com/madaporc/DTO/UtilisateurDTO.java`
- Model : `src/main/java/com/madaporc/model/Permission.java`
- Repository : `src/main/java/com/madaporc/repository/PermissionRepository.java`
- Model : `src/main/java/com/madaporc/model/RolePermission.java`
- Repository : `src/main/java/com/madaporc/repository/RolePermissionRepository.java`
- Template : `src/main/resources/templates/utilisateurs/list.html`
- Template : `src/main/resources/templates/utilisateurs/form.html`
- Template : `src/main/resources/templates/roles/list.html`

