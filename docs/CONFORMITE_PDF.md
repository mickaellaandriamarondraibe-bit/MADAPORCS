# Vérification stricte PDF

Ce squelette suit les controllers et routes du PDF.

## AuthController
- Service : `UtilisateurService`
- Vue principale : `login`
- Référence Figma : Connexion - MADAPORC / GestPorc
- `GetMapping` `/login` → `showLogin(Model model)`
- `PostMapping` `/login` → `login(@ModelAttribute LoginDTO dto, Model model, HttpSession session)`
- `GetMapping` `/logout` → `logout(HttpSession session)`

## DashboardController
- Service : `DashboardService`
- Vue principale : `dashboard/index`
- Référence Figma : Tableau de bord - MADAPORC / GestPorc
- `GetMapping` `/dashboard` → `dashboard(Model model, HttpSession session)`

## LotPorcController
- Service : `LotPorcService`
- Vue principale : `lots/listeLots`
- Référence Figma : Liste des Lots / Ajouter Lot / Detail Lot - MADAPORC / GestPorc
- `GetMapping` `/lots` → `listLots(@RequestParam(required=false) String code, @RequestParam(required=false) Long raceId, @RequestParam(required=false) Long statutId, Model model)`
- `GetMapping` `/lots/form` → `showForm(@RequestParam(required=false) Long id, @RequestParam(required=false) String typeEntree, Model model)`
- `PostMapping` `/lots/save` → `saveLot(@ModelAttribute LotPorcDTO dto, Model model, HttpSession session)`
- `GetMapping` `/lots/{id}` → `detailLot(@PathVariable Long id, Model model)`
- `PostMapping` `/lots/archive/{id}` → `archiver(@PathVariable Long id, Model model)`

## MouvementLotController
- Service : `MouvementLotService`
- Vue principale : `lots/mouvements`
- Référence Figma : Detail du Lot - Onglet Mouvements / Liste des Lots - Actions
- `GetMapping` `/lots/{id}/mouvements` → `listMouvements(@PathVariable Long id, Model model)`
- `PostMapping` `/lots/mouvements/save` → `saveMouvement(@ModelAttribute MouvementLotDTO dto, Model model, HttpSession session)`

## PeseeLotController
- Service : `PeseeLotService`
- Vue principale : `lots/pesees`
- Référence Figma : Detail du Lot - Evolution du poids / Onglet Pesees
- `GetMapping` `/lots/{id}/pesees` → `listPesees(@PathVariable Long id, Model model)`
- `PostMapping` `/lots/pesees/save` → `savePesee(@ModelAttribute PeseeLotDTO dto, Model model, HttpSession session)`

## ReproducteurController
- Service : `ReproducteurService`
- Vue principale : `reproducteurs/listeReproducteurs`
- Référence Figma : Liste des Reproducteurs / Detail du Reproducteur - MADAPORC / GestPorc
- `GetMapping` `/reproducteurs` → `listReproducteurs(@RequestParam(required=false) String motCle, @RequestParam(required=false) Long sexeId, @RequestParam(required=false) Long statutId, Model model)`
- `GetMapping` `/reproducteurs/form` → `showForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/reproducteurs/save` → `save(@ModelAttribute ReproducteurDTO dto, Model model, HttpSession session)`
- `GetMapping` `/reproducteurs/{id}` → `detail(@PathVariable Long id, Model model)`
- `PostMapping` `/reproducteurs/archive/{id}` → `archiver(@PathVariable Long id, Model model)`

## CycleProductionController
- Service : `CycleProductionService`
- Vue principale : `production/cycles`
- Référence Figma : Cycles de Production - MADAPORC / GestPorc
- `GetMapping` `/cycles` → `listCycles(Model model)`
- `GetMapping` `/cycles/form` → `showCycleForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/cycles/save` → `saveCycle(@ModelAttribute CycleProductionDTO dto, Model model)`

## EvenementReproductionController
- Service : `EvenementReproductionService`
- Vue principale : `reproduction/evenements`
- Référence Figma : Evenements de Reproduction - MADAPORC / GestPorc
- `GetMapping` `/reproduction/evenements` → `listEvenements(@RequestParam(required=false) Long typeId, Model model)`
- `GetMapping` `/reproduction/evenements/form` → `showEvenementForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/reproduction/evenements/save` → `saveEvenement(@ModelAttribute EvenementReproductionDTO dto, Model model, HttpSession session)`

## ClientController
- Service : `ClientService`
- Vue principale : `commerce/clients`
- Référence Figma : Gestion des Clients - MADAPORC / GestPorc
- `GetMapping` `/clients` → `listClients(@RequestParam(required=false) String motCle, @RequestParam(required=false) String typeClient, Model model)`
- `GetMapping` `/clients/form` → `showClientForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/clients/save` → `saveClient(@ModelAttribute ClientDTO dto, Model model)`
- `GetMapping` `/clients/{id}` → `detailClient(@PathVariable Long id, Model model)`

## VenteController
- Service : `VenteService`
- Vue principale : `commerce/ventes`
- Référence Figma : Gestion des Ventes - MADAPORC / GestPorc
- `GetMapping` `/ventes` → `listVentes(Model model)`
- `GetMapping` `/ventes/form` → `showVenteForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/ventes/save` → `saveVente(@ModelAttribute VenteDTO dto, Model model, HttpSession session)`
- `PostMapping` `/ventes/valider/{id}` → `validerVente(@PathVariable Long id, Model model)`
- `PostMapping` `/ventes/annuler/{id}` → `annulerVente(@PathVariable Long id, Model model)`

## PaiementFactureController
- Service : `PaiementFactureService`
- Vue principale : `commerce/factureDetail`
- Référence Figma : Paiements et Factures - MADAPORC / GestPorc
- `GetMapping` `/factures/{venteId}` → `detailFacture(@PathVariable Long venteId, Model model)`
- `PostMapping` `/paiements/save` → `savePaiement(@ModelAttribute PaiementDTO dto, Model model)`
- `PostMapping` `/factures/generer` → `genererFacture(@RequestParam Long venteId, Model model)`
- `GetMapping` `/factures/pdf/{venteId}` → `exporterFacturePdf(@PathVariable Long venteId, Model model)`

## DepenseController
- Service : `DepenseService`
- Vue principale : `finance/depenses`
- Référence Figma : Analyses & Rapports - Depenses / Module Commerce-Finance
- `GetMapping` `/depenses` → `listDepenses(@RequestParam(required=false) LocalDate debut, @RequestParam(required=false) LocalDate fin, @RequestParam(required=false) Long categorieId, Model model)`
- `GetMapping` `/depenses/form` → `showDepenseForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/depenses/save` → `saveDepense(@ModelAttribute DepenseDTO dto, Model model, HttpSession session)`

## IngredientController
- Service : `IngredientService`
- Vue principale : `ressources/ingredients`
- Référence Figma : Gestion des Ingredients - MADAPORC / GestPorc
- `GetMapping` `/ingredients` → `listIngredients(@RequestParam(required=false) String motCle, Model model)`
- `GetMapping` `/ingredients/form` → `showIngredientForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/ingredients/save` → `saveIngredient(@ModelAttribute IngredientDTO dto, Model model)`

## MouvementStockController
- Service : `MouvementStockService`
- Vue principale : `ressources/mouvementsStock`
- Référence Figma : Mouvements de Stock - MADAPORC / GestPorc
- `GetMapping` `/stocks/mouvements` → `listMouvementsStock(@RequestParam(required=false) Long ingredientId, @RequestParam(required=false) Long typeId, Model model)`
- `GetMapping` `/stocks/mouvements/form` → `showMouvementStockForm(Model model)`
- `PostMapping` `/stocks/mouvements/save` → `saveMouvementStock(@ModelAttribute MouvementStockDTO dto, Model model, HttpSession session)`

## MelangeController
- Service : `MelangeService`
- Vue principale : `ressources/melanges`
- Référence Figma : Melanges Alimentaires / Feed Formulations - MADAPORC
- `GetMapping` `/melanges` → `listMelanges(Model model)`
- `GetMapping` `/melanges/form` → `showMelangeForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/melanges/save` → `saveMelange(@ModelAttribute MelangeDTO dto, Model model)`

## DistributionAlimentController
- Service : `DistributionAlimentService`
- Vue principale : `ressources/distributions`
- Référence Figma : Distribution des Aliments - MADAPORC / GestPorc
- `GetMapping` `/distributions` → `listDistributions(Model model)`
- `GetMapping` `/distributions/form` → `showDistributionForm(Model model)`
- `PostMapping` `/distributions/save` → `saveDistribution(@ModelAttribute DistributionAlimentDTO dto, Model model, HttpSession session)`

## VaccinController
- Service : `VaccinService`
- Vue principale : `sante/vaccins`
- Référence Figma : Gestion des Vaccins - MADAPORC / GestPorc
- `GetMapping` `/vaccins` → `listVaccins(Model model)`
- `GetMapping` `/vaccins/form` → `showVaccinForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/vaccins/save` → `saveVaccin(@ModelAttribute VaccinDTO dto, Model model)`

## VaccinationController
- Service : `VaccinationService`
- Vue principale : `sante/vaccinations`
- Référence Figma : Journal des Vaccinations - MADAPORC / GestPorc
- `GetMapping` `/vaccinations` → `listVaccinations(@RequestParam(required=false) Long lotId, @RequestParam(required=false) Long reproducteurId, Model model)`
- `GetMapping` `/vaccinations/form` → `showVaccinationForm(Model model)`
- `PostMapping` `/vaccinations/save` → `saveVaccination(@ModelAttribute VaccinationDTO dto, Model model, HttpSession session)`

## SuiviSanitaireController
- Service : `SuiviSanitaireService`
- Vue principale : `sante/suivisSanitaires`
- Référence Figma : Suivi Sanitaire - MADAPORC / GestPorc
- `GetMapping` `/sante/suivis` → `listSuivis(@RequestParam(required=false) Long statutId, Model model)`
- `GetMapping` `/sante/suivis/form` → `showSuiviForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/sante/suivis/save` → `saveSuivi(@ModelAttribute SuiviSanitaireDTO dto, Model model, HttpSession session)`
- `PostMapping` `/sante/suivis/statut` → `changerStatut(@RequestParam Long suiviId, @RequestParam Long statutId, Model model)`

## EmployeController
- Service : `EmployeService`
- Vue principale : `personnel/employes`
- Référence Figma : Gestion des Employes - MADAPORC / GestPorc
- `GetMapping` `/employes` → `listEmployes(@RequestParam(required=false) String motCle, Model model)`
- `GetMapping` `/employes/form` → `showEmployeForm(@RequestParam(required=false) Long id, Model model)`
- `PostMapping` `/employes/save` → `saveEmploye(@ModelAttribute EmployeDTO dto, Model model)`

## PresenceController
- Service : `PresenceService`
- Vue principale : `personnel/presences`
- Référence Figma : Presence et Pointage - MADAPORC / GestPorc
- `GetMapping` `/presences` → `listPresences(@RequestParam(required=false) LocalDate date, @RequestParam(required=false) Long employeId, Model model)`
- `PostMapping` `/presences/save` → `savePresence(@ModelAttribute PresenceDTO dto, Model model)`
- `PostMapping` `/presences/pointer` → `pointer(@RequestParam Long employeId, @RequestParam String typePointage, Model model)`

## SalaireController
- Service : `SalaireService`
- Vue principale : `personnel/salaires`
- Référence Figma : Gestion des Salaires - MADAPORC / GestPorc
- `GetMapping` `/salaires` → `listSalaires(@RequestParam(required=false) Integer mois, @RequestParam(required=false) Integer annee, Model model)`
- `PostMapping` `/salaires/generer` → `genererSalaires(@RequestParam Integer mois, @RequestParam Integer annee, Model model)`
- `PostMapping` `/salaires/save` → `saveSalaire(@ModelAttribute SalaireEmployeDTO dto, Model model)`
- `PostMapping` `/salaires/payer/{id}` → `payerSalaire(@PathVariable Long id, Model model)`
- `GetMapping` `/salaires/fiche/{id}` → `ficheSalaire(@PathVariable Long id, Model model)`

## RapportController
- Service : `RapportService`
- Vue principale : `rapports/index`
- Référence Figma : Analyses & Rapports - MADAPORC / GestPorc
- `GetMapping` `/rapports` → `rapports(Model model)`
- `GetMapping` `/rapports/export/pdf` → `exportPdf(@RequestParam(required=false) String typeRapport, Model model)`
- `GetMapping` `/rapports/export/excel` → `exportExcel(@RequestParam(required=false) String typeRapport, Model model)`

## ProfilController
- Service : `ProfilService`
- Vue principale : `settings/profil`
- Référence Figma : Profil Utilisateur - MADAPORC / GestPorc
- `GetMapping` `/profil` → `profil(HttpSession session, Model model)`
- `PostMapping` `/profil/save` → `saveProfil(@ModelAttribute ProfilUtilisateurDTO dto, HttpSession session, Model model)`
- `PostMapping` `/profil/password` → `changerMotDePasse(@ModelAttribute ChangerMotDePasseDTO dto, HttpSession session, Model model)`

## UtilisateurController
- Service : `AdminUtilisateurService`
- Vue principale : `settings/utilisateurs`
- Référence Figma : Gestion des Utilisateurs - MADAPORC / GestPorc
- `GetMapping` `/utilisateurs` → `listUtilisateurs(Model model, HttpSession session)`
- `GetMapping` `/utilisateurs/form` → `showUtilisateurForm(@RequestParam(required=false) Long id, Model model, HttpSession session)`
- `PostMapping` `/utilisateurs/save` → `saveUtilisateur(@ModelAttribute UtilisateurDTO dto, Model model, HttpSession session)`
- `PostMapping` `/utilisateurs/desactiver/{id}` → `desactiver(@PathVariable Long id, Model model, HttpSession session)`
- `PostMapping` `/roles/permissions/save` → `saveRolePermissions(@ModelAttribute RolePermissionDTO dto, Model model, HttpSession session)`

