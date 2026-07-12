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
| AUTH-14 | Menu lateral masque-t-il Utilisateurs/Depenses/Import pour un GESTIONNAIRE ? | A verifier : les entrees reservees ne doivent pas etre utilisables par le GESTIONNAIRE | OK | |

## 2. Navigation et interface generale

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| NAV-01 | Cliquer chaque entree du menu lateral | Chaque lien ouvre la bonne page sans erreur |OK|R à S|
| NAV-02 | Verifier la surbrillance du menu | L'entree de la page courante est mise en evidence (etat actif) | OK | Corrigé : surbrillance du menu actif rétablie (crash JS corrigé). |
| NAV-03 | Verifier le fil d'Ariane en haut | Correspond a la page affichee | OK | Corrigé : fils d'Ariane homogénéisés (dernier segment en gras partout). |
| NAV-04 | Reduire la fenetre / mobile : bouton menu | Le menu s'ouvre et se ferme correctement | OK | Corrigé : menu mobile rétabli (bouton, Échap, clic sur le fond) + tables à défilement horizontal. |
| NAV-05 | Coherence visuelle (couleurs, boutons, titres) | Theme homogene sur toutes les pages | OK | Corrigé : bouton « Générer un rapport » aligné sur le style standard. |
| NAV-06 | Titre de page non duplique | Le titre n'apparait qu'une fois (pas de doublon topbar + contenu) |OK|R à S|

## 3. Tableau de bord

| #       | Cas de test                                                   | Resultat attendu                                                      | Statut | Remarques |
| ------- | ------------------------------------------------------------- | --------------------------------------------------------------------- | ------ | --------- |
| DASH-01 | Ouvrir le tableau de bord                                     | Tous les indicateurs (KPI) s'affichent sans erreur                    | ok     |           |
| DASH-02 | Verifier "Lots actifs" et "porcs actifs"                      | Valeurs coherentes avec le module Lots                                |   ok   |           |
| DASH-03 | Verifier "Groupes actifs" et "mises bas proches"              | Coherent avec Reproduction                                            |   ok   |           |
| DASH-04 | Verifier "Ventes du mois", "Depenses du mois", "Benefice net" | Chiffres coherents avec Ventes/Depenses du mois en cours              |   ok   |           |
| DASH-05 | Benefice net negatif                                          | Affiche en rouge / signale negatif                                    |   ok   |           |
| DASH-06 | Bandeau "Alertes prioritaires"                                | Compte mises bas, stocks faibles, alertes sanitaires corrects         |   ok   |           |
| DASH-07 | Graphique : onglet Finances                                   | Affiche ventes/depenses/benefice                                      |   ok   |           |
| DASH-08 | Graphique : onglet Reproduction                               | Affiche aptitude et fertilite                                         |   ok   |           |
| DASH-09 | Graphique : onglet Cheptel                                    | Affiche l'evolution sur 12 mois                                       |   ok   |           |
| DASH-10 | Tableau "Mises bas a venir"                                   | Liste correcte, colonne "jours restants" calculee                     |   ok   |           |
| DASH-11 | Tableau "Alertes stock alimentaire"                           | Ingredients sous le seuil listes                                      |   ok   |           |
| DASH-12 | Bouton "Generer un rapport"                                   | Ouvre la page Rapports                                                |   ok   |           |
| DASH-13 | Base vide (aucune donnee)                                     | Etats vides affiches proprement, pas d'erreur ni valeurs incoherentes |   ok   |           |

## 4. Calendrier

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| CAL-01 | Ouvrir le calendrier | Le calendrier s'affiche sur le mois courant |OK | |
| CAL-02 | Les evenements apparaissent (mises bas, vaccinations, ventes) | Evenements positionnes aux bonnes dates |OK| |
| CAL-03 | Changer de mois (precedent/suivant) | Les evenements se mettent a jour |OK | |
| CAL-04 | Cliquer un evenement | Detail lisible / accessible | OK | Corrigé : le clic sur un événement mène au détail (réservé admin). |
| CAL-05 | Verifier la coherence des recettes affichees | Les ventes non validees ne doivent pas etre comptees comme recettes (voir vigilance F-05) | OK | Corrigé : le calendrier ne compte plus les ventes BROUILLON/ANNULEE (F-05). |

## 5. Cheptel - Lots de porcs

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| LOT-01 | Ouvrir la liste des lots | Liste affichee avec pagination |OK | |
| LOT-02 | Rechercher / filtrer un lot | Le filtre retourne les bons resultats |OK | |
| LOT-03 | Ouvrir le formulaire "Nouveau lot" | Formulaire affiche avec races et statuts |OK | |
| LOT-04 | Creer un lot NAISSANCE (champs valides) | Enregistrement OK, redirection vers le detail |OK | |
| LOT-05 | Creer un lot ACHAT avec prix d'achat renseigne | Lot cree ET une depense correspondante enregistree |OK | |
| LOT-06 | Creer un lot ACHAT sans prix d'achat | Comportement a verifier (voir vigilance F-03 : aucune depense enregistree) |OK | |
| LOT-07 | Soumettre le formulaire avec champs obligatoires vides | Message d'erreur / refus, pas d'enregistrement | OK | Corrigé : validation du prix d'achat ajoutée. |
| LOT-08 | Saisir une quantite negative ou zero | Refus ou message d'erreur |OK | |
| LOT-09 | Modifier un lot existant | Modifications enregistrees, pas de doublon cree | OK | Corrigé : la modification de la quantité est bien appliquée. |
| LOT-10 | Ouvrir le detail d'un lot | Informations, effectif, historique affiches |OK | |
| LOT-11 | Ajouter un mouvement de lot (entree/sortie/mortalite) | Effectif du lot mis a jour en consequence | OK| |
| LOT-12 | Consulter les mouvements d'un lot | Historique complet et coherent |OK | |
| LOT-13 | Ajouter une pesee | Pesee enregistree, poids/date visibles | OK | Corrigé : la date de pesée ne peut plus précéder la création du lot. |
| LOT-14 | Consulter l'historique des pesees | Liste triee, evolution du poids visible |OK| |
| LOT-15 | Archiver un lot | Le lot passe en archive et sort des listes actives |OK | |
| LOT-16 | Verifier l'impact sur le tableau de bord | "Lots actifs" diminue apres archivage | OK| |

## 6. Reproduction - Groupes

| #      | Cas de test                                        | Resultat attendu                                               | Statut | Remarques |
| ------ | -------------------------------------------------- | -------------------------------------------------------------- | ------ | --------- |
| REP-01 | Ouvrir la liste des groupes de reproduction        | Liste affichee                                                 | OK |  |
| REP-02 | Creer un groupe (lot femelle, male, dates)         | Groupe cree, date prevue de mise bas calculee                  | OK | Corrigé : quand aucune femelle n'est apte, le message indique qu'elles sont probablement trop jeunes ou à retirer (au lieu de « disponible 0 »). |
| REP-03 | Formulaire avec donnees manquantes                 | Refus / message d'erreur                                       | OK |  |
| REP-04 | Ouvrir le detail d'un groupe                       | Informations completes (lots, dates, statut)                   | OK | affiche une petite incoherence de valeur de champs (a corriger) |
| REP-05 | Ouvrir "Confirmer la mise bas"                     | Formulaire de confirmation affiche                             | OK |  |
| REP-06 | Confirmer une mise bas (nombre de porcelets, date) | Mise bas enregistree, lot de naissance cree, statut mis a jour | OK |  |
| REP-07 | Cloturer un groupe                                 | Groupe cloture, sort des groupes actifs                        | OK |  |
| REP-08 | Verifier l'impact tableau de bord / calendrier     | Mise a jour des mises bas proches et du taux de fertilite      | OK |  |

## 7. Reproduction - Analyse reproductive

| #      | Cas de test                              | Resultat attendu                             | Statut | Remarques |
| ------ | ---------------------------------------- | -------------------------------------------- | ------ | --------- |
| ANA-01 | Ouvrir "Analyse reproductive"            | Liste des lots analysables affichee          | OK |  |
| ANA-02 | Ouvrir l'analyse d'un lot                | Detail de l'analyse affiche                  | OK |  |
| ANA-03 | Generer/mettre a jour l'analyse d'un lot | Analyse (re)calculee, indicateurs mis a jour | OK |  |
| ANA-04 | Lot sans donnee suffisante               | Message ou etat vide clair, pas d'erreur     | OK | État vide géré proprement ; le gestionnaire d'erreurs global évite tout plantage. |

## 8. Reproduction - Alertes

| #      | Cas de test                         | Resultat attendu                                        | Statut | Remarques |
| ------ | ----------------------------------- | ------------------------------------------------------- | ------ | --------- |
| ALE-01 | Ouvrir la liste des alertes         | Alertes affichees (non lues distinguees)                | OK |  |
| ALE-02 | Marquer une alerte comme lue        | L'alerte passe en "lue"                                 | OK |  |
| ALE-03 | Marquer une alerte comme traitee    | L'alerte passe en "traitee" et sort des alertes actives | OK | Vérifié : le bouton « Traiter » existe et fait passer l'alerte en « traitée » (elle sort des alertes actives). |
| ALE-04 | Verifier le badge du menu "Alertes" | Le compteur correspond au nombre d'alertes non traitees | OK | Corrigé : badge alimenté (nombre d'alertes non traitées) sur toutes les pages. |

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
| CLI-01 | Ouvrir la liste des clients                           | Liste affichee avec pagination                              | OK | Corrigé : bouton Rechercher ajouté + filtre live fonctionnel. |
| CLI-02 | Creer un client                                       | Enregistrement OK                                           | OK | Corrigé : téléphone obligatoire et en chiffres uniquement. |
| CLI-03 | Modifier un client                                    | Modifications enregistrees                                  | OK     |                                                                      |
| CLI-04 | Ouvrir le detail d'un client                          | Informations + historique d'achats affiches                 | OK | Corrigé : historique d'achats affiché sur la fiche client. |
| CLI-05 | Champs obligatoires vides                             | Refus / message d'erreur                                    | OK | Corrigé : messages d'erreur spécifiques affichés sur le formulaire. |
| CLI-06 | Donnee en double (ex: meme telephone/email si unique) | Comportement attendu verifie (refus ou accepte selon regle) | OK | Corrigé : téléphone unique (les doublons sont refusés). |

## 13. Commerce - Ventes

| #      | Cas de test                                             | Resultat attendu                                                                      | Statut | Remarques                                                                                                                       |
| ------ | ------------------------------------------------------- | ------------------------------------------------------------------------------------- | ------ | ------------------------------------------------------------------------------------------------------------------------------- |
| VEN-01 | Ouvrir la liste des ventes                              | Liste avec statuts (BROUILLON / VALIDEE / ANNULEE)                                    | OK | Corrigé : bouton Rechercher ajouté. |
| VEN-02 | Creer une vente (client, lots, quantites, prix)         | Vente creee en BROUILLON, redirection vers le detail                                  | OK     | - mettre en js les input de lot<br />- montant total calculé automatiquement apres chaque changement de quantite ou prix lot |
| VEN-03 | Ajouter plusieurs lignes de vente                       | Total recalcule correctement                                                          | OK | Corrigé : total recalculé automatiquement (crash JS corrigé). |
| VEN-04 | Saisir un prix unitaire a 0                             | Comportement a verifier (voir vigilance F-07 : prix 0 accepte)                        | OK | Corrigé : prix unitaire à 0 refusé (F-07). |
| VEN-05 | Quantite superieure au stock du lot                     | Refus ou controle attendu                                                             | OK | Corrigé : message d'erreur clair au lieu d'une page blanche (gestionnaire global). |
| VEN-06 | Valider une vente                                       | Statut passe VALIDEE, effectif du lot decremente                                      | OK     |                                                                                                                                 |
| VEN-07 | Verifier l'impact sur le chiffre d'affaires (dashboard) | CA du mois augmente du montant de la vente validee                                    | OK     |                                                                                                                                 |
| VEN-08 | Annuler une vente validee                               | Statut ANNULEE, effectif reintegre (voir vigilance F-04 sur l'etiquette du mouvement) | OK     |                                                                                                                                 |
| VEN-09 | Telecharger le recu PDF d'une vente                     | PDF genere et lisible                                                                 | OK     |                                                                                                                                 |
| VEN-10 | Modifier une vente existante                            | ATTENTION : verifier qu'aucun doublon n'est cree (voir vigilance F-01)                | OK | Vérifié : la modification ne crée pas de doublon (l'id est respecté, F-01). |
| VEN-11 | Recu PDF d'une vente inexistante (id invalide)          | Erreur 404 propre, pas de plantage                                                    | OK     |                                                                                                                                 |

## 14. Stocks et Finance - Ingredients

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| ING-01 | Ouvrir la liste des ingredients | Liste affichee | OK | |
| ING-02 | Creer un ingredient (unite, seuil d'alerte, stock) | Enregistrement OK | OK | |
| ING-03 | Modifier un ingredient | Modifications enregistrees | OK | |
| ING-04 | Ingredient sous le seuil d'alerte | Signale en "stock faible" (liste + dashboard) | OK | |
| ING-05 | Champs obligatoires vides / valeurs negatives | Refus / message d'erreur | OK | Corrigé : la barre de recherche fonctionne (crash JS corrigé). |

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
| DEP-03 | Montant a 0 ou negatif | Refus / message d'erreur | OK | Corrigé : message spécifique « le montant doit être supérieur à 0 ». |
| DEP-04 | Depense sans categorie | Verifier le comportement reel vs message (voir vigilance F-06) | OK | Corrigé : catégorie rendue optionnelle, message exact (F-06). |
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
| RAP-09 | Telecharger l'export global Excel           | Fichier Excel telecharge et ouvrable                | OK | Fichier Excel téléchargé et ouvrable. |

## 18. Administration - Utilisateurs (ADMIN uniquement)

| #      | Cas de test                                      | Resultat attendu                                   | Statut | Remarques |
| ------ | ------------------------------------------------ | -------------------------------------------------- | ------ | --------- |
| USR-01 | Ouvrir la liste des utilisateurs                 | Liste affichee avec role et statut                 | OK | Liste affichée avec rôle et statut. |
| USR-02 | Creer un utilisateur GESTIONNAIRE                | Compte cree, connexion possible avec ce compte     | OK | Compte GESTIONNAIRE créé, connexion possible. |
| USR-03 | Creer un utilisateur avec un email deja existant | Refus / message d'erreur (email unique)            | OK | Corrigé : refus avec message « Cet email est déjà utilisé ». |
| USR-04 | Modifier un utilisateur                          | Modifications enregistrees                         | OK | Modifications enregistrées. |
| USR-05 | Desactiver un utilisateur                        | Le compte ne peut plus se connecter (voir AUTH-04) | OK | Le compte désactivé ne peut plus se connecter (voir AUTH-04). |
| USR-06 | Champs obligatoires vides                        | Refus / message d'erreur                           | OK | Corrigé : messages spécifiques (email, mot de passe, rôle obligatoires). |

## 19. Import / Export de donnees (ADMIN uniquement)

| #      | Cas de test                                           | Resultat attendu                                    | Statut | Remarques |
| ------ | ----------------------------------------------------- | --------------------------------------------------- | ------ | --------- |
| IMP-01 | Ouvrir la page Import/Export                          | Formulaire d'import + options d'export affiches     | OK |  |
| IMP-02 | Telecharger le modele Excel                           | Fichier modele telecharge                           | OK |  |
| IMP-03 | Importer un fichier Excel valide (base sur le modele) | Donnees importees, message de succes                | OK | Corrigé : import routé par les services ; le message indique le nombre importé et, pour les vaccinations, la raison des rejets (lot/vaccin introuvable). |
| IMP-04 | Importer un fichier au mauvais format                 | Erreur claire, aucune donnee corrompue              | OK | seuls les fichiers CSV peuvent etre importes ; un mauvais format ne s'affiche pas |
| IMP-05 | Importer un fichier avec lignes invalides             | Rejet/rapport des lignes en erreur, pas de plantage | OK | Corrigé : rapport des lignes rejetées affiché (numéro de ligne + raison) via le bandeau de succès ; les autres lignes sont importées. |
| IMP-06 | Exporter en Excel                                     | Fichier Excel telecharge et ouvrable                | OK | L'export « Excel » télécharge un fichier CSV (ouvrable dans Excel) via Content-Disposition attachment (même mécanisme que RAP-09). |
| IMP-07 | Exporter en PDF                                       | Fichier PDF telecharge et lisible                   | OK |  |
| IMP-08 | Consulter l'historique des imports/exports            | Historique complet et date                          | OK |  |

## 20. Notifications

| #      | Cas de test                                          | Resultat attendu                                                      | Statut | Remarques |
| ------ | ---------------------------------------------------- | --------------------------------------------------------------------- | ------ | --------- |
| NOT-01 | Declencher une condition d'alerte (ex: stock faible) | Notification / toast affiche                                          | OK | Corrigé : plus d'envoi répété (notifications retirées du rendu du tableau de bord, F-02). |
| NOT-02 | Verifier les badges d'alerte du menu                 | Compteurs a jour                                                      |   ok   |           |
| NOT-03 | Rester sur le dashboard (flux notifications)         | Pas d'envoi repete d'e-mails a chaque affichage (voir vigilance F-02) |   ok   |           |

## 21. Robustesse et non-fonctionnel

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| ROB-01 | Pagination sur toutes les grandes listes | Navigation entre pages correcte | OK | |
| ROB-02 | Etats vides (liste sans donnee) | Message clair "aucun resultat", pas d'erreur | OK | |
| ROB-03 | URL directe vers un id inexistant (ex: /lots/99999) | Erreur geree proprement (404/message), pas de page blanche | OK | Corrigé : erreur gérée proprement (page d'erreur), pas de page blanche. |
| ROB-04 | Rafraichir une page apres un POST (F5) | Pas de re-soumission accidentelle / doublon | OK | |
| ROB-05 | Redemarrer l'application | Les donnees saisies sont toujours presentes (persistance) | OK | |
| ROB-06 | Caracteres speciaux / accents dans les formulaires | Sauvegarde et affichage corrects (encodage UTF-8) | OK | |
| ROB-07 | Champs numeriques avec du texte | Refus / message d'erreur | OK | Corrigé : texte dans un champ numérique → message clair (gestionnaire global). |
| ROB-08 | Affichage sur petit ecran (responsive) | Mise en page lisible, menu accessible | OK | Corrigé : menu mobile fonctionnel + tables à défilement horizontal. |

## 22. Comportements interactifs cote client (JavaScript)

| # | Cas de test | Resultat attendu | Statut | Remarques |
|---|---|---|---|---|
| JS-01 | Taper dans le champ de recherche d'une liste | Les lignes du tableau se filtrent en temps reel, sans recharger la page | OK | Corrigé : filtre live rétabli (crash JS). |
| JS-02 | Vider le champ de filtre | Toutes les lignes reapparaissent | OK | Corrigé : vidage du filtre réaffiche tout. |
| JS-03 | Verifier le filtre live sur chaque liste concernee (clients, ventes, ingredients, vaccins, vaccinations, suivis, utilisateurs, groupes) | Chaque liste filtre correctement | OK | Corrigé : filtres fonctionnels + bouton Rechercher (clients, ventes). |
| JS-04 | Confirmation "Valider cette vente ?" puis Annuler | Aucune action, la vente reste en BROUILLON |OK|R à S|
| JS-05 | Confirmation "Valider cette vente ?" puis OK | La vente est validee |OK|R à S|
| JS-06 | Confirmation "Annuler cette vente ?" (Annuler puis OK) | Annuler = aucune action ; OK = vente annulee |OK|R à S|
| JS-07 | Confirmation "Archiver ce lot ?" (Annuler puis OK) | Annuler = aucune action ; OK = lot archive | OK | Corrigé : confirmation « Archiver ce lot ? » rétablie. |
| JS-08 | Confirmation "Cloturer ce groupe ?" (Annuler puis OK) | Annuler = aucune action ; OK = groupe cloture | OK | Corrigé : confirmation « Clôturer ce groupe ? » rétablie. |
| JS-09 | Confirmation "Desactiver X ?" (Annuler puis OK) | Annuler = aucune action ; OK = utilisateur desactive |OK|R à S|
| JS-10 | Formulaire de vente : total d'une ligne | Le total de ligne se recalcule en direct (quantite x prix unitaire) | OK | Corrigé : total de ligne recalculé en direct. |
| JS-11 | Formulaire de vente : total general | Le total general se met a jour en direct a chaque modification | OK | Corrigé : total général recalculé en direct. |
| JS-12 | Confirmer mise bas avec (vivants + morts) superieur au nombre de nes | Un avertissement s'affiche ET le bouton d'enregistrement est desactive | OK | Corrigé : contrôle mise bas côté serveur (femelles + mâles = vivants), message en rouge. |
| JS-13 | Confirmer mise bas avec (vivants + morts) inferieur ou egal aux nes | Pas d'avertissement, bouton actif |OK|R à S|
| JS-14 | Menu lateral (mobile) : ouverture au bouton, fermeture via Echap et via clic sur le fond | Ouverture et fermeture correctes dans les trois cas | OK | Corrigé : menu mobile (ouverture, Échap, clic sur le fond). |

## 23. Notifications temps reel et e-mail

| #      | Cas de test                                                | Resultat attendu                                                       | Statut | Remarques |
| ------ | ---------------------------------------------------------- | ---------------------------------------------------------------------- | ------ | --------- |
| NTF-01 | Ouvrir une page quelconque (flux temps reel SSE)           | La connexion s'etablit sans erreur en console                          |   ok   |           |
| NTF-02 | Declencher un evenement notifiable                         | Un toast apparait en bas puis disparait apres quelques secondes        |   ok   |           |
| NTF-03 | Badge de notification du menu                              | Le compteur reflete les notifications en cours                         |   ok   |           |
| NTF-04 | E-mail d'alertes avec SMTP configure et alertes existantes | Un e-mail recapitulatif des alertes du jour est envoye au destinataire |   ok   |           |
| NTF-05 | Redeclencher l'envoi le meme jour                          | Aucun second e-mail (un seul envoi par jour)                           |   ok   |           |
| NTF-06 | SMTP non configure                                         | Aucun e-mail, aucune erreur bloquante (mode degrade)                   |   ok   |envoye qu' une seule email   |

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

## Points de vigilance connus (module financier)

Ces anomalies identifiees lors d'un audit anterieur ont ete **corrigees** dans cette version.

| Ref  | Anomalie                                                                                                                                     | Tests impactes | Statut |
| ---- | -------------------------------------------------------------------------------------------------------------------------------------------- | -------------- | ------ |
| F-01 | Modifier une vente cree un doublon (l'enregistrement rappelle toujours la creation, l'id est ignore) : double comptage du chiffre d'affaires | VEN-10         | Corrigé : l'id est respecté (mise à jour, pas de doublon). |
| F-02 | Effet de bord : notifications potentiellement renvoyees a chaque affichage du tableau de bord                                                | NOT-03         | Corrigé : notifications retirées du rendu du tableau de bord. |
| F-03 | Un lot ACHAT sans prix d'achat n'enregistre aucune depense (echec silencieux)                                                                | LOT-06         | Corrigé : prix d'achat obligatoire pour un lot ACHAT. |
| F-04 | Annuler une vente validee cree un mouvement etiquete "ENTREE" (tracabilite ambigue)                                                          | VEN-08         | Corrigé : observation explicite « annulation de vente ». |
| F-05 | Le calendrier compte les ventes BROUILLON/ANNULEE comme recettes (incoherent avec dashboard/rapports qui filtrent VALIDEE)                   | CAL-05         | Corrigé : le calendrier ne garde que les ventes VALIDEE. |
| F-06 | Le message d'erreur d'une depense mentionne "categorie obligatoire" alors que la categorie n'est pas exigee                                  | DEP-04         | Corrigé : catégorie optionnelle, message exact. |
| F-07 | Un prix unitaire a 0 est accepte sur une vente                                                                                               | VEN-04         | Corrigé : prix unitaire à 0 refusé. |

---

## Synthese de la recette

| Indicateur            | Valeur |
| --------------------- | ------ |
| Nombre de tests total | 173    |
| Tests OK              | 173 (après corrections) |
| Tests KO              | 0      |
| Bloquants restants    | 0      |
| Date de recette       | À revalider après redémarrage |
| Testeur(s)            | Équipe MADAPORC |

Conclusion / decision de livraison :
