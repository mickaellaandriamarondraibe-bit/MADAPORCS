# Cahier de tests utilisateur - MADAPORC

Application de gestion d'elevage porcin (Spring Boot MVC + JSP + PostgreSQL).

Ce document liste tous les tests fonctionnels a realiser manuellement pour valider le projet avant livraison.
Chaque ligne est un cas de test : effectuez l'action, comparez au resultat attendu, puis remplissez la colonne Statut.

## Comment utiliser ce cahier

- Colonne **Statut** : ecrire `OK` si le comportement est conforme, `KO` sinon.
- Colonne **Remarques** : en cas de KO, noter le probleme observe (message, capture, etape qui bloque).
- Testez de preference dans l'ordre des sections (l'authentification et la creation de donnees conditionnent les tests suivants).
- Testez avec les deux roles (ADMIN et GESTIONNAIRE) pour la partie controle d'acces.

## Prerequis et demarrage

| Element              | Valeur                                                         |
| -------------------- | -------------------------------------------------------------- |
| Base de donnees      | PostgreSQL, base`madaporc` (voir `application.properties`) |
| Lancement            | `mvn spring-boot:run` (ou depuis l'IDE)                      |
| URL                  | http://localhost:8082/                                         |
| Navigateurs a tester | Chrome/Brave + Firefox (au moins un des deux)                  |

## Comptes de test

| Role         | Email                                | Mot de passe                                         |
| ------------ | ------------------------------------ | ---------------------------------------------------- |
| ADMIN        | admin@madaporc.local                 | (mot de passe defini a l'installation - a completer) |
| GESTIONNAIRE | (a creer via le module Utilisateurs) | (a completer)                                        |

Note : le compte GESTIONNAIRE doit etre cree par l'ADMIN (section 18) avant de pouvoir tester le controle d'acces (section 1).

---

## Repartition des tests par membre

Repartition par module fonctionnel (chaque membre teste des domaines complets et coherents). Noms deduits des auteurs Git : ajustez si besoin.

| Membre         | Perimetre (modules)                                              | Sections       | Tests                                          | Nb |
| -------------- | ---------------------------------------------------------------- | -------------- | ---------------------------------------------- | -- |
| Miaro          | Authentification & acces, Securite & session                     | 1, 24          | AUTH-01..14, SEC-01..05                        | 19 |
| Sarobidy       | Navigation & interface, Interactions JS cote client              | 2, 22          | NAV-01..06, JS-01..14                          | 20 |
| Noah           | Tableau de bord, Notifications, Temps reel & e-mail              | 3, 20, 23      | DASH-01..13, NOT-01..03, NTF-01..06            | 22 |
| Fanilo         | Lots de porcs, Calendrier                                        | 5, 4           | LOT-01..16, CAL-01..05                         | 21 |
| Micka - Tsanta | Reproduction (Groupes, Analyse, Alertes), Import/Export          | 6, 7, 8, 19    | REP-01..08, ANA-01..04, ALE-01..04, IMP-01..08 | 24 |
| Davida         | Sante (Vaccins, Vaccinations, Suivis), Rapports                  | 9, 10, 11, 17  | VAC-01..04, VCN-01..04, SUI-01..04, RAP-01..09 | 21 |
| Manoa          | Commerce (Clients, Ventes), Utilisateurs                         | 12, 13, 18     | CLI-01..06, VEN-01..11, USR-01..06             | 23 |
| Mandresy       | Stocks & Finance (Ingredients, Mouvements, Depenses), Robustesse | 14, 15, 16, 21 | ING-01..05, MVS-01..05, DEP-01..05, ROB-01..08 | 23 |

Total : 173 tests.

Points de coordination :

- Comptes ADMIN requis : Mandresy (Depenses), Miaro (Import/Export), Manoa (Utilisateurs) doivent disposer d'un compte ADMIN.
- Mika (controle d'acces) a besoin des deux comptes : un ADMIN et un GESTIONNAIRE.
- Manoa cree d'abord un compte GESTIONNAIRE (USR-02) : il sert aussi a Mika pour la section 1.
- Les tests transverses (JS, Notifications, Robustesse) peuvent croiser plusieurs modules : signaler tout KO au membre proprietaire du module concerne.

---

## 1. Authentification et controle d'acces

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| AUTH-01 | Se connecter avec email + mot de passe ADMIN valides | Redirection vers le tableau de bord, nom affiche en haut a droite | OK | |
| AUTH-02 | Se connecter avec un mot de passe incorrect | Message "Email ou mot de passe incorrect, ou compte desactive", reste sur login |OK | |
| AUTH-03 | Se connecter avec un email inconnu | Meme message d'erreur, pas d'acces |OK | |
| AUTH-04 | Se connecter avec un compte desactive | Message d'erreur, connexion refusee | OK | |
| AUTH-05 | Champ email ou mot de passe vide puis valider | Connexion refusee (pas de plantage) | OK | |
| AUTH-06 | Acceder a une URL interne (ex: /dashboard) sans etre connecte | Redirection automatique vers la page de login | OK | |
| AUTH-07 | Se deconnecter (Deconnexion dans le menu) | Retour a la page de login, session fermee |OK | |
| AUTH-08 | Apres deconnexion, cliquer "Precedent" du navigateur | Ne redonne pas acces aux pages internes (redirige vers login) | OK | |
| AUTH-09 | Connecte en GESTIONNAIRE, ouvrir /utilisateurs | Acces refuse (erreur 403 "Acces reserve a l'administrateur") | OK |  |
| AUTH-10 | Connecte en GESTIONNAIRE, ouvrir /depenses | Acces refuse (403) | OK | |
| AUTH-11 | Connecte en GESTIONNAIRE, ouvrir /imports (Import/Export) | Acces refuse (403) | OK |  |
| AUTH-12 | Connecte en GESTIONNAIRE, lancer un export Excel/PDF (/exports) | Acces refuse (403) | OK | |
| AUTH-13 | Connecte en ADMIN, ouvrir Utilisateurs, Depenses, Import/Export | Acces autorise a tous les modules | OK | |
| AUTH-14 | Menu lateral masque-t-il Utilisateurs/Depenses/Import pour un GESTIONNAIRE ? | A verifier : les entrees reservees ne doivent pas etre utilisables par le GESTIONNAIRE | KO | Mbola mipotra ilay menu fa misy erreur raha compte GESTIONNAIRE no miditra ao |

## 2. Navigation et interface generale

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| NAV-01 | Cliquer chaque entree du menu lateral | Chaque lien ouvre la bonne page sans erreur |OK|R à S|
| NAV-02 | Verifier la surbrillance du menu | L'entree de la page courante est mise en evidence (etat actif) |Pas en marche|L'entree de la page n'est pas mise en évidence dans le sidebar|
| NAV-03 | Verifier le fil d'Ariane en haut | Correspond a la page affichee |OK|La representation n'est pas totalement homogène sur tout les pages ex: Reproduction (light) / Groupes (gras) alors que Reproduction (light) / Analyse reproductive (light)|
| NAV-04 | Reduire la fenetre / mobile : bouton menu | Le menu s'ouvre et se ferme correctement |Pas en marche|Le bouton pour étaler le menu lateral ne fonctionne pas encore et le contenu de certaine tableau sur version mobile n'est pas correctement affiché (problème de responsive design)|
| NAV-05 | Coherence visuelle (couleurs, boutons, titres) | Theme homogene sur toutes les pages |OK|Le Bouton Générer un rapport pourrait être changé pour être conforme aux autres boutons|
| NAV-06 | Titre de page non duplique | Le titre n'apparait qu'une fois (pas de doublon topbar + contenu) |OK|R à S|

## 3. Tableau de bord

| #       | Cas de test                                                   | Resultat attendu                                                      | Statut | Remarques |
| ------- | ------------------------------------------------------------- | --------------------------------------------------------------------- | ------ | --------- |
| DASH-01 | Ouvrir le tableau de bord                                     | Tous les indicateurs (KPI) s'affichent sans erreur                    |        |           |
| DASH-02 | Verifier "Lots actifs" et "porcs actifs"                      | Valeurs coherentes avec le module Lots                                |        |           |
| DASH-03 | Verifier "Groupes actifs" et "mises bas proches"              | Coherent avec Reproduction                                            |        |           |
| DASH-04 | Verifier "Ventes du mois", "Depenses du mois", "Benefice net" | Chiffres coherents avec Ventes/Depenses du mois en cours              |        |           |
| DASH-05 | Benefice net negatif                                          | Affiche en rouge / signale negatif                                    |        |           |
| DASH-06 | Bandeau "Alertes prioritaires"                                | Compte mises bas, stocks faibles, alertes sanitaires corrects         |        |           |
| DASH-07 | Graphique : onglet Finances                                   | Affiche ventes/depenses/benefice                                      |        |           |
| DASH-08 | Graphique : onglet Reproduction                               | Affiche aptitude et fertilite                                         |        |           |
| DASH-09 | Graphique : onglet Cheptel                                    | Affiche l'evolution sur 12 mois                                       |        |           |
| DASH-10 | Tableau "Mises bas a venir"                                   | Liste correcte, colonne "jours restants" calculee                     |        |           |
| DASH-11 | Tableau "Alertes stock alimentaire"                           | Ingredients sous le seuil listes                                      |        |           |
| DASH-12 | Bouton "Generer un rapport"                                   | Ouvre la page Rapports                                                |        |           |
| DASH-13 | Base vide (aucune donnee)                                     | Etats vides affiches proprement, pas d'erreur ni valeurs incoherentes |        |           |

## 4. Calendrier

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| CAL-01 | Ouvrir le calendrier | Le calendrier s'affiche sur le mois courant |OK | |
| CAL-02 | Les evenements apparaissent (mises bas, vaccinations, ventes) | Evenements positionnes aux bonnes dates |OK| |
| CAL-03 | Changer de mois (precedent/suivant) | Les evenements se mettent a jour |OK | |
| CAL-04 | Cliquer un evenement | Detail lisible / accessible |KO | Evenement qui ne fonctionne pas apres click|
| CAL-05 | Verifier la coherence des recettes affichees | Les ventes non validees ne doivent pas etre comptees comme recettes (voir vigilance F-05) | KO| Les ventes non validee meme refusee est encore afficher sur la calendrier|

## 5. Cheptel - Lots de porcs

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| LOT-01 | Ouvrir la liste des lots | Liste affichee avec pagination |KO | Pas de pagination|
| LOT-02 | Rechercher / filtrer un lot | Le filtre retourne les bons resultats |OK | |
| LOT-03 | Ouvrir le formulaire "Nouveau lot" | Formulaire affiche avec races et statuts |OK | |
| LOT-04 | Creer un lot NAISSANCE (champs valides) | Enregistrement OK, redirection vers le detail |OK | |
| LOT-05 | Creer un lot ACHAT avec prix d'achat renseigne | Lot cree ET une depense correspondante enregistree |OK | |
| LOT-06 | Creer un lot ACHAT sans prix d'achat | Comportement a verifier (voir vigilance F-03 : aucune depense enregistree) |KO | Si le champs prix d'achat est nul, l'exception n'est pas controlle et on arrive sur une page d'erreur|
| LOT-07 | Soumettre le formulaire avec champs obligatoires vides | Message d'erreur / refus, pas d'enregistrement | OK| Tous les champs possede deja un verification d'exception sauf pour le prix d'achat|
| LOT-08 | Saisir une quantite negative ou zero | Refus ou message d'erreur |OK | |
| LOT-09 | Modifier un lot existant | Modifications enregistrees, pas de doublon cree |KO | La modification n'est pas applique sur la quantite du lot, mais le reste de modification fonctionne correctement|
| LOT-10 | Ouvrir le detail d'un lot | Informations, effectif, historique affiches |OK | |
| LOT-11 | Ajouter un mouvement de lot (entree/sortie/mortalite) | Effectif du lot mis a jour en consequence | OK| |
| LOT-12 | Consulter les mouvements d'un lot | Historique complet et coherent |OK | |
| LOT-13 | Ajouter une pesee | Pesee enregistree, poids/date visibles |KO | Pesee fonctionnelle, mais ne considere pas l'exception: date de pesee < date de creation de lot de porcs|
| LOT-14 | Consulter l'historique des pesees | Liste triee, evolution du poids visible |KO | Pas de triage , et pas de suivis d'evolution de poids|
| LOT-15 | Archiver un lot | Le lot passe en archive et sort des listes actives |OK | |
| LOT-16 | Verifier l'impact sur le tableau de bord | "Lots actifs" diminue apres archivage | OK| |

## 6. Reproduction - Groupes

| #      | Cas de test                                        | Resultat attendu                                               | Statut | Remarques |
| ------ | -------------------------------------------------- | -------------------------------------------------------------- | ------ | --------- |
| REP-01 | Ouvrir la liste des groupes de reproduction        | Liste affichee                                                 |        |           |
| REP-02 | Creer un groupe (lot femelle, male, dates)         | Groupe cree, date prevue de mise bas calculee                  |        |           |
| REP-03 | Formulaire avec donnees manquantes                 | Refus / message d'erreur                                       |        |           |
| REP-04 | Ouvrir le detail d'un groupe                       | Informations completes (lots, dates, statut)                   |        |           |
| REP-05 | Ouvrir "Confirmer la mise bas"                     | Formulaire de confirmation affiche                             |        |           |
| REP-06 | Confirmer une mise bas (nombre de porcelets, date) | Mise bas enregistree, lot de naissance cree, statut mis a jour |        |           |
| REP-07 | Cloturer un groupe                                 | Groupe cloture, sort des groupes actifs                        |        |           |
| REP-08 | Verifier l'impact tableau de bord / calendrier     | Mise a jour des mises bas proches et du taux de fertilite      |        |           |

## 7. Reproduction - Analyse reproductive

| #      | Cas de test                              | Resultat attendu                             | Statut | Remarques |
| ------ | ---------------------------------------- | -------------------------------------------- | ------ | --------- |
| ANA-01 | Ouvrir "Analyse reproductive"            | Liste des lots analysables affichee          |        |           |
| ANA-02 | Ouvrir l'analyse d'un lot                | Detail de l'analyse affiche                  |        |           |
| ANA-03 | Generer/mettre a jour l'analyse d'un lot | Analyse (re)calculee, indicateurs mis a jour |        |           |
| ANA-04 | Lot sans donnee suffisante               | Message ou etat vide clair, pas d'erreur     |        |           |

## 8. Reproduction - Alertes

| #      | Cas de test                         | Resultat attendu                                        | Statut | Remarques |
| ------ | ----------------------------------- | ------------------------------------------------------- | ------ | --------- |
| ALE-01 | Ouvrir la liste des alertes         | Alertes affichees (non lues distinguees)                |        |           |
| ALE-02 | Marquer une alerte comme lue        | L'alerte passe en "lue"                                 |        |           |
| ALE-03 | Marquer une alerte comme traitee    | L'alerte passe en "traitee" et sort des alertes actives |        |           |
| ALE-04 | Verifier le badge du menu "Alertes" | Le compteur correspond au nombre d'alertes non traitees |        |           |

## 9. Sante - Vaccins

| #      | Cas de test                 | Resultat attendu                          | Statut | Remarques |
| ------ | --------------------------- | ----------------------------------------- | ------ | --------- |
| VAC-01 | Ouvrir la liste des vaccins | Liste affichee                            |    ok    |           |
| VAC-02 | Creer un vaccin             | Enregistrement OK, apparait dans la liste |      ok  |           |
| VAC-03 | Modifier un vaccin          | Modifications enregistrees                |        ok|           |
| VAC-04 | Champs obligatoires vides   | Refus / message d'erreur                  |        ok|           |

## 10. Sante - Vaccinations

| #      | Cas de test                                     | Resultat attendu                                              | Statut | Remarques |
| ------ | ----------------------------------------------- | ------------------------------------------------------------- | ------ | --------- |
| VCN-01 | Ouvrir la liste des vaccinations                | Liste affichee                                                |    ok    |           |
| VCN-02 | Enregistrer une vaccination (lot, vaccin, date) | Vaccination enregistree                                       |     ok   |           |
| VCN-03 | Vaccination avec rappel/date future             | Apparait dans les vaccinations a venir (dashboard/calendrier) |      ok  |           |
| VCN-04 | Champs manquants                                | Refus / message d'erreur                                      |       ok |           |

## 11. Sante - Suivis sanitaires

| #      | Cas de test                                        | Resultat attendu                                        | Statut | Remarques |
| ------ | -------------------------------------------------- | ------------------------------------------------------- | ------ | --------- |
| SUI-01 | Ouvrir la liste des suivis sanitaires              | Liste affichee                                          |   ok     |           |
| SUI-02 | Creer un suivi (lot, maladie, traitement, date)    | Suivi enregistre                                        |     ok   |           |
| SUI-03 | Champs manquants                                   | Refus / message d'erreur                                |      ok  |           |
| SUI-04 | Verifier l'apparition dans les rapports sanitaires | Le suivi remonte dans le rapport sanitaire (section 17) |       ok |           |

## 12. Commerce - Clients

| #      | Cas de test                                           | Resultat attendu                                            | Statut | Remarques                                                            |
| ------ | ----------------------------------------------------- | ----------------------------------------------------------- | ------ | -------------------------------------------------------------------- |
| CLI-01 | Ouvrir la liste des clients                           | Liste affichee avec pagination                              | KO     | bouton "rechercher" manquant                                         |
| CLI-02 | Creer un client                                       | Enregistrement OK                                           | KO     | num_telephone: regle de gestion manquante (num seulement en chiffre) |
| CLI-03 | Modifier un client                                    | Modifications enregistrees                                  | OK     |                                                                      |
| CLI-04 | Ouvrir le detail d'un client                          | Informations + historique d'achats affiches                 | KO     | pas d'historique d'achat                                             |
| CLI-05 | Champs obligatoires vides                             | Refus / message d'erreur                                    | OK     | mieux avec message d'erreur personnalise                             |
| CLI-06 | Donnee en double (ex: meme telephone/email si unique) | Comportement attendu verifie (refus ou accepte selon regle) | KO     | meme num_telephone en double                                         |

## 13. Commerce - Ventes

| #      | Cas de test                                             | Resultat attendu                                                                      | Statut | Remarques                                                                                                                       |
| ------ | ------------------------------------------------------- | ------------------------------------------------------------------------------------- | ------ | ------------------------------------------------------------------------------------------------------------------------------- |
| VEN-01 | Ouvrir la liste des ventes                              | Liste avec statuts (BROUILLON / VALIDEE / ANNULEE)                                    | OK     | bouton "rechercher" manquant                                                                                                    |
| VEN-02 | Creer une vente (client, lots, quantites, prix)         | Vente creee en BROUILLON, redirection vers le detail                                  | OK     | - mettre en js les input de lot<br />- montant total calculé automatiquement apres chaque changement de quantite ou prix lot |
| VEN-03 | Ajouter plusieurs lignes de vente                       | Total recalcule correctement                                                          | KO     | - formulaire d'insertion, montant non mis à jour                                                                               |
| VEN-04 | Saisir un prix unitaire a 0                             | Comportement a verifier (voir vigilance F-07 : prix 0 accepte)                        | KO     | - montant 0 accepté                                                                                                            |
| VEN-05 | Quantite superieure au stock du lot                     | Refus ou controle attendu                                                             | KO     | page blanche obtenue et non message d'erreur                                                                                    |
| VEN-06 | Valider une vente                                       | Statut passe VALIDEE, effectif du lot decremente                                      | OK     |                                                                                                                                 |
| VEN-07 | Verifier l'impact sur le chiffre d'affaires (dashboard) | CA du mois augmente du montant de la vente validee                                    | OK     |                                                                                                                                 |
| VEN-08 | Annuler une vente validee                               | Statut ANNULEE, effectif reintegre (voir vigilance F-04 sur l'etiquette du mouvement) | OK     |                                                                                                                                 |
| VEN-09 | Telecharger le recu PDF d'une vente                     | PDF genere et lisible                                                                 | OK     |                                                                                                                                 |
| VEN-10 | Modifier une vente existante                            | ATTENTION : verifier qu'aucun doublon n'est cree (voir vigilance F-01)                | nean   |                                                                                                                                 |
| VEN-11 | Recu PDF d'une vente inexistante (id invalide)          | Erreur 404 propre, pas de plantage                                                    | OK     |                                                                                                                                 |

## 14. Stocks et Finance - Ingredients

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| ING-01 | Ouvrir la liste des ingredients | Liste affichee | OK | |
| ING-02 | Creer un ingredient (unite, seuil d'alerte, stock) | Enregistrement OK | OK | |
| ING-03 | Modifier un ingredient | Modifications enregistrees | OK | |
| ING-04 | Ingredient sous le seuil d'alerte | Signale en "stock faible" (liste + dashboard) | OK | |
| ING-05 | Champs obligatoires vides / valeurs negatives | Refus / message d'erreur | KO | La barre de recherche ne fonctionne pas. |

## 15. Stocks et Finance - Mouvements de stock

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| MVS-01 | Ouvrir la liste des mouvements de stock | Liste affichee | OK | |
| MVS-02 | Enregistrer une ENTREE de stock | Stock de l'ingredient augmente | OK | |
| MVS-03 | Enregistrer une SORTIE de stock | Stock de l'ingredient diminue | OK | |
| MVS-04 | Sortie superieure au stock disponible | Refus ou controle attendu | OK  | Test effectué : stock = 50 kg, sortie demandée = 100 kg. La sortie est refusée. |
| MVS-05 | Mouvement faisant passer sous le seuil | Alerte stock faible declenchee | OK | Test effectué : stock initial = 150 kg, seuil = 100 kg, sortie = 80 kg. Stock final = 70 kg et alerte créée. |

## 16. Finance - Depenses (ADMIN uniquement)

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| DEP-01 | Ouvrir la liste des depenses | Liste affichee, total correct | OK | |
| DEP-02 | Creer une depense (categorie, montant, date) | Enregistrement OK | OK | |
| DEP-03 | Montant a 0 ou negatif | Refus / message d'erreur | KO | Le message affiché est général : "La catégorie, le montant et la date sont obligatoires" au lieu d'indiquer précisément que le montant est invalide ou négatif. |
| DEP-04 | Depense sans categorie | Verifier le comportement reel vs message (voir vigilance F-06) | KO | Message affiché indiquant que la catégorie est obligatoire alors que le problème concerne uniquement la catégorie absente. |
| DEP-05 | Impact sur le total des depenses du mois (dashboard) | Depenses du mois mises a jour | OK | |

## 17. Rapports

| #      | Cas de test                                 | Resultat attendu                                    | Statut | Remarques |
| ------ | ------------------------------------------- | --------------------------------------------------- | ------ | --------- |
| RAP-01 | Ouvrir la page Rapports                     | Formulaire de dates + cartes de rapports affiches   |    ok    |           |
| RAP-02 | Calculer le bilan financier sur une periode | Total ventes, total depenses, benefice net corrects |     ok   |           |
| RAP-03 | Periode sans donnee                         | Resultats a 0, pas d'erreur                         |      ok  |           |
| RAP-04 | Date debut posterieure a date fin           | Comportement gere (message ou resultat vide)        |      ok  |           |
| RAP-05 | Generer le PDF Sanitaire                    | PDF telecharge, contenu coherent                    |      ok  |           |
| RAP-06 | Generer le PDF Commercial                   | PDF telecharge, contenu coherent                    |       ok |           |
| RAP-07 | Generer le PDF Financier                    | PDF telecharge, contenu coherent                    |       ok |           |
| RAP-08 | Generer le PDF Reproduction                 | PDF telecharge, contenu coherent                    |        ok|           |
| RAP-09 | Telecharger l'export global Excel           | Fichier Excel telecharge et ouvrable                |        |ok           |

## 18. Administration - Utilisateurs (ADMIN uniquement)

| #      | Cas de test                                      | Resultat attendu                                   | Statut | Remarques |
| ------ | ------------------------------------------------ | -------------------------------------------------- | ------ | --------- |
| USR-01 | Ouvrir la liste des utilisateurs                 | Liste affichee avec role et statut                 |        |           |
| USR-02 | Creer un utilisateur GESTIONNAIRE                | Compte cree, connexion possible avec ce compte     |        |           |
| USR-03 | Creer un utilisateur avec un email deja existant | Refus / message d'erreur (email unique)            |        |           |
| USR-04 | Modifier un utilisateur                          | Modifications enregistrees                         |        |           |
| USR-05 | Desactiver un utilisateur                        | Le compte ne peut plus se connecter (voir AUTH-04) |        |           |
| USR-06 | Champs obligatoires vides                        | Refus / message d'erreur                           |        |           |

## 19. Import / Export de donnees (ADMIN uniquement)

| #      | Cas de test                                           | Resultat attendu                                    | Statut | Remarques |
| ------ | ----------------------------------------------------- | --------------------------------------------------- | ------ | --------- |
| IMP-01 | Ouvrir la page Import/Export                          | Formulaire d'import + options d'export affiches     |        |           |
| IMP-02 | Telecharger le modele Excel                           | Fichier modele telecharge                           |        |           |
| IMP-03 | Importer un fichier Excel valide (base sur le modele) | Donnees importees, message de succes                |        |           |
| IMP-04 | Importer un fichier au mauvais format                 | Erreur claire, aucune donnee corrompue              |        |           |
| IMP-05 | Importer un fichier avec lignes invalides             | Rejet/rapport des lignes en erreur, pas de plantage |        |           |
| IMP-06 | Exporter en Excel                                     | Fichier Excel telecharge et ouvrable                |        |           |
| IMP-07 | Exporter en PDF                                       | Fichier PDF telecharge et lisible                   |        |           |
| IMP-08 | Consulter l'historique des imports/exports            | Historique complet et date                          |        |           |

## 20. Notifications

| #      | Cas de test                                          | Resultat attendu                                                      | Statut | Remarques |
| ------ | ---------------------------------------------------- | --------------------------------------------------------------------- | ------ | --------- |
| NOT-01 | Declencher une condition d'alerte (ex: stock faible) | Notification / toast affiche                                          |        |           |
| NOT-02 | Verifier les badges d'alerte du menu                 | Compteurs a jour                                                      |        |           |
| NOT-03 | Rester sur le dashboard (flux notifications)         | Pas d'envoi repete d'e-mails a chaque affichage (voir vigilance F-02) |        |           |

## 21. Robustesse et non-fonctionnel

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| ROB-01 | Pagination sur toutes les grandes listes | Navigation entre pages correcte | OK | |
| ROB-02 | Etats vides (liste sans donnee) | Message clair "aucun resultat", pas d'erreur | OK | |
| ROB-03 | URL directe vers un id inexistant (ex: /lots/99999) | Erreur geree proprement (404/message), pas de page blanche | KO | Aucune page 404 affichée mais aucune erreur bloquante non plus. |
| ROB-04 | Rafraichir une page apres un POST (F5) | Pas de re-soumission accidentelle / doublon | OK | |
| ROB-05 | Redemarrer l'application | Les donnees saisies sont toujours presentes (persistance) | OK | |
| ROB-06 | Caracteres speciaux / accents dans les formulaires | Sauvegarde et affichage corrects (encodage UTF-8) | OK | |
| ROB-07 | Champs numeriques avec du texte | Refus / message d'erreur | KO | En modifiant le type du champ en "text" via l'inspecteur navigateur et en envoyant une valeur texte (ex: montant = abc), une erreur 404 apparaît au lieu d'une validation propre. |
| ROB-08 | Affichage sur petit ecran (responsive) | Mise en page lisible, menu accessible | KO | La sidebar ne fonctionne pas correctement sur mobile. |

## 22. Comportements interactifs cote client (JavaScript)

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| JS-01 | Taper dans le champ de recherche d'une liste | Les lignes du tableau se filtrent en temps reel, sans recharger la page |Pas en marche|Pages à revoir: Groupes de reproduction, vaccins, Suivi Sanitaire, Clients, Ventes, Ingredients, Utilisateurs|
| JS-02 | Vider le champ de filtre | Toutes les lignes reapparaissent |Pas en marche|Besoin de la correction de JS-01|
| JS-03 | Verifier le filtre live sur chaque liste concernee (clients, ventes, ingredients, vaccins, vaccinations, suivis, utilisateurs, groupes) | Chaque liste filtre correctement |À revoir|Pas de filtre mais juste un champs de recherche avec les pages concernées mais seulement avec Lots de porcs, Dépense et rapport. Le filtre de recherche n'est pas encore fonctionnel (JS-01)|
| JS-04 | Confirmation "Valider cette vente ?" puis Annuler | Aucune action, la vente reste en BROUILLON |OK|R à S|
| JS-05 | Confirmation "Valider cette vente ?" puis OK | La vente est validee |OK|R à S|
| JS-06 | Confirmation "Annuler cette vente ?" (Annuler puis OK) | Annuler = aucune action ; OK = vente annulee |OK|R à S|
| JS-07 | Confirmation "Archiver ce lot ?" (Annuler puis OK) | Annuler = aucune action ; OK = lot archive |À revoir|Pas de confirmation mais directement Archivé|
| JS-08 | Confirmation "Cloturer ce groupe ?" (Annuler puis OK) | Annuler = aucune action ; OK = groupe cloture |À revoir|Pas de confirmation mais directement Cloturé|
| JS-09 | Confirmation "Desactiver X ?" (Annuler puis OK) | Annuler = aucune action ; OK = utilisateur desactive |OK|R à S|
| JS-10 | Formulaire de vente : total d'une ligne | Le total de ligne se recalcule en direct (quantite x prix unitaire) |Pas en marche|Les calculs ne se font pas encore directement|
| JS-11 | Formulaire de vente : total general | Le total general se met a jour en direct a chaque modification |Pas en marche|Les calculs ne se font pas encore directement|
| JS-12 | Confirmer mise bas avec (vivants + morts) superieur au nombre de nes | Un avertissement s'affiche ET le bouton d'enregistrement est desactive |À revoir|Le bouton reste toujours actif mais le message d'erreur s'affiche bien. Message d'erreur doit être en rouge|
| JS-13 | Confirmer mise bas avec (vivants + morts) inferieur ou egal aux nes | Pas d'avertissement, bouton actif |OK|R à S|
| JS-14 | Menu lateral (mobile) : ouverture au bouton, fermeture via Echap et via clic sur le fond | Ouverture et fermeture correctes dans les trois cas |À revoir|Même cas que NAV-04|

## 23. Notifications temps reel et e-mail

| #      | Cas de test                                                | Resultat attendu                                                       | Statut | Remarques |
| ------ | ---------------------------------------------------------- | ---------------------------------------------------------------------- | ------ | --------- |
| NTF-01 | Ouvrir une page quelconque (flux temps reel SSE)           | La connexion s'etablit sans erreur en console                          |        |           |
| NTF-02 | Declencher un evenement notifiable                         | Un toast apparait en bas puis disparait apres quelques secondes        |        |           |
| NTF-03 | Badge de notification du menu                              | Le compteur reflete les notifications en cours                         |        |           |
| NTF-04 | E-mail d'alertes avec SMTP configure et alertes existantes | Un e-mail recapitulatif des alertes du jour est envoye au destinataire |        |           |
| NTF-05 | Redeclencher l'envoi le meme jour                          | Aucun second e-mail (un seul envoi par jour)                           |        |           |
| NTF-06 | SMTP non configure                                         | Aucun e-mail, aucune erreur bloquante (mode degrade)                   |        |           |

## 24. Securite et session

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| SEC-01 | Deux onglets ouverts, se deconnecter dans l'un | L'autre onglet perd l'acces aux pages protegees | OK | Mila actualisena |
| SEC-02 | Reutiliser une page interne apres deconnexion (bouton Precedent) | Acces refuse, redirection login | OK | |
| SEC-03 | Saisir du code dans un champ texte (ex: balise script) | La valeur est affichee telle quelle, aucun script execute (pas de XSS) | OK | |
| SEC-04 | Saisir une apostrophe / point-virgule (ex: nom "O'Brien") | Sauvegarde correcte, aucune erreur SQL |OK | |
| SEC-05 | Appeler une action ADMIN par URL directe en GESTIONNAIRE (ex: POST /utilisateurs/desactiver/1) | Refuse (403), aucune execution | OK | |

Note technique : la protection CSRF est desactivee au niveau Spring Security ; la securite repose sur la session applicative (interceptor). A signaler comme choix de configuration.

---

## Points de vigilance connus (module financier - a corriger)

Ces anomalies ont ete identifiees lors d'un audit anterieur et ne sont pas encore corrigees.
Elles sont susceptibles de faire echouer certains tests ci-dessus (references F-xx). A confirmer pendant la recette puis a corriger.

| Ref  | Anomalie                                                                                                                                     | Tests impactes |
| ---- | -------------------------------------------------------------------------------------------------------------------------------------------- | -------------- |
| F-01 | Modifier une vente cree un doublon (l'enregistrement rappelle toujours la creation, l'id est ignore) : double comptage du chiffre d'affaires | VEN-10         |
| F-02 | Effet de bord : notifications potentiellement renvoyees a chaque affichage du tableau de bord                                                | NOT-03         |
| F-03 | Un lot ACHAT sans prix d'achat n'enregistre aucune depense (echec silencieux)                                                                | LOT-06         |
| F-04 | Annuler une vente validee cree un mouvement etiquete "ENTREE" (tracabilite ambigue)                                                          | VEN-08         |
| F-05 | Le calendrier compte les ventes BROUILLON/ANNULEE comme recettes (incoherent avec dashboard/rapports qui filtrent VALIDEE)                   | CAL-05         |
| F-06 | Le message d'erreur d'une depense mentionne "categorie obligatoire" alors que la categorie n'est pas exigee                                  | DEP-04         |
| F-07 | Un prix unitaire a 0 est accepte sur une vente                                                                                               | VEN-04         |

---

## Synthese de la recette

| Indicateur            | Valeur |
| --------------------- | ------ |
| Nombre de tests total | 173    |
| Tests OK              |        |
| Tests KO              |        |
| Bloquants restants    |        |
| Date de recette       |        |
| Testeur(s)            |        |

Conclusion / decision de livraison :
