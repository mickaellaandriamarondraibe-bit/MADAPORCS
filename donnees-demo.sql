-- =====================================================
-- DONNEES DE DEMONSTRATION MADAPORC - JOUR J
-- =====================================================
-- A executer UNE fois sur une base deja initialisee
-- (schema.sql + data.sql doivent avoir tourne avant,
--  et l'application demarree au moins une fois pour que
--  la table destinataires_alerte existe).
--
-- Lancement :
--   psql -U postgres -d madaporc -f donnees-demo.sql
--   (mot de passe : postgres)
--
-- Les dates sont RELATIVES a CURRENT_DATE : les alertes
-- (mise bas proche, mise bas en retard) restent coherentes
-- quel que soit le jour de la demonstration.
--
-- Rejouable : si les lots existent deja, le script s'arrete
-- proprement sans rien inserer en double.
-- =====================================================

DO $$
DECLARE
    v_admin        BIGINT;
    v_race_lw      BIGINT;
    v_race_duroc   BIGINT;
    v_race_piet    BIGINT;
    v_race_land    BIGINT;
    v_lot_f01      BIGINT;
    v_lot_f02      BIGINT;
    v_lot_m01      BIGINT;
    v_lot_e01      BIGINT;
    v_lot_c01      BIGINT;
    v_grp1         BIGINT;
    v_grp2         BIGINT;
    v_grp3         BIGINT;
    v_vente1       BIGINT;
    v_vente2       BIGINT;
BEGIN
    -- Garde-fou : donnees deja chargees ?
    IF EXISTS (SELECT 1 FROM lots_porcs WHERE code_lot = 'LOT-F01') THEN
        RAISE NOTICE 'Donnees demo deja presentes, insertion ignoree.';
        RETURN;
    END IF;

    -- References
    v_admin      := (SELECT id FROM utilisateurs WHERE email = 'admin@madaporc.local');
    v_race_lw    := (SELECT id FROM race WHERE nom = 'Large White');
    v_race_duroc := (SELECT id FROM race WHERE nom = 'Duroc');
    v_race_piet  := (SELECT id FROM race WHERE nom = 'Pietrain');
    v_race_land  := (SELECT id FROM race WHERE nom = 'Landrace');

    -- =====================================================
    -- LOTS DE PORCS (5 lots)
    --  F01 : truies reproductrices (gestation proche)
    --  F02 : truies reproductrices (mise bas en retard)
    --  M01 : verrats reproducteurs
    --  E01 : engraissement (partiellement vendu)
    --  C01 : jeunes femelles en croissance (partiellement vendu)
    -- =====================================================
    INSERT INTO lots_porcs(code_lot, date_creation, race_id, sexe, objectif, origine, age_mois, effectif_initial, effectif_actuel, statut, description)
    VALUES ('LOT-F01', CURRENT_DATE - 400, v_race_lw, 'FEMELLE', 'REPRODUCTION', 'ACHAT', 14, 6, 6, 'ACTIF', 'Truies reproductrices Large White')
    RETURNING id INTO v_lot_f01;

    INSERT INTO lots_porcs(code_lot, date_creation, race_id, sexe, objectif, origine, age_mois, effectif_initial, effectif_actuel, statut, description)
    VALUES ('LOT-F02', CURRENT_DATE - 300, v_race_land, 'FEMELLE', 'REPRODUCTION', 'ACHAT', 16, 5, 5, 'ACTIF', 'Truies reproductrices Landrace')
    RETURNING id INTO v_lot_f02;

    INSERT INTO lots_porcs(code_lot, date_creation, race_id, sexe, objectif, origine, age_mois, effectif_initial, effectif_actuel, statut, description)
    VALUES ('LOT-M01', CURRENT_DATE - 380, v_race_duroc, 'MALE', 'REPRODUCTION', 'ACHAT', 20, 2, 2, 'ACTIF', 'Verrats reproducteurs Duroc')
    RETURNING id INTO v_lot_m01;

    INSERT INTO lots_porcs(code_lot, date_creation, race_id, sexe, objectif, origine, age_mois, effectif_initial, effectif_actuel, statut, description)
    VALUES ('LOT-E01', CURRENT_DATE - 150, v_race_piet, 'MALE', 'ENGRAISSEMENT', 'ACHAT', 5, 13, 6, 'ACTIF', 'Lot d''engraissement Pietrain')
    RETURNING id INTO v_lot_e01;

    INSERT INTO lots_porcs(code_lot, date_creation, race_id, sexe, objectif, origine, age_mois, effectif_initial, effectif_actuel, statut, description)
    VALUES ('LOT-C01', CURRENT_DATE - 90, v_race_land, 'FEMELLE', 'CROISSANCE', 'NAISSANCE', 3, 10, 5, 'ACTIF', 'Jeunes femelles en croissance')
    RETURNING id INTO v_lot_c01;

    -- =====================================================
    -- MOUVEMENTS DES LOTS (entrees, deces, ventes)
    -- =====================================================
    INSERT INTO mouvements_lots_porcs(lot_id, type_mouvement, quantite, date_mouvement, observation)
    VALUES (v_lot_f01, 'ENTREE', 6,  CURRENT_DATE - 400, 'Constitution du lot'),
           (v_lot_f02, 'ENTREE', 5,  CURRENT_DATE - 300, 'Achat truies'),
           (v_lot_m01, 'ENTREE', 2,  CURRENT_DATE - 380, 'Achat verrats'),
           (v_lot_e01, 'ENTREE', 13, CURRENT_DATE - 150, 'Achat porcelets engraissement'),
           (v_lot_e01, 'DECES',  1,  CURRENT_DATE - 40,  'Mortalite'),
           (v_lot_e01, 'VENTE',  6,  CURRENT_DATE - 6,   'Vente Boucherie Centrale'),
           (v_lot_c01, 'ENTREE', 10, CURRENT_DATE - 90,  'Naissances issues du sevrage'),
           (v_lot_c01, 'DECES',  1,  CURRENT_DATE - 20,  'Mortalite'),
           (v_lot_c01, 'VENTE',  4,  CURRENT_DATE - 2,   'Vente Marche Ambohipo');

    -- =====================================================
    -- PESEES (courbe de croissance de l'engraissement)
    -- =====================================================
    INSERT INTO pesees_lots(lot_id, date_pesee, poids_moyen, observation)
    VALUES (v_lot_e01, CURRENT_DATE - 120, 12.5, 'Debut engraissement'),
           (v_lot_e01, CURRENT_DATE - 90,  25.0, NULL),
           (v_lot_e01, CURRENT_DATE - 60,  42.0, NULL),
           (v_lot_e01, CURRENT_DATE - 30,  61.0, NULL),
           (v_lot_e01, CURRENT_DATE - 5,   78.0, 'Prets pour la vente'),
           (v_lot_c01, CURRENT_DATE - 40,  8.0,  NULL),
           (v_lot_c01, CURRENT_DATE - 10,  18.0, NULL);

    -- =====================================================
    -- REPARTITION REPRODUCTIVE (alimente la page Analyse)
    -- =====================================================
    INSERT INTO repartitions_reproductives_lots(lot_id, statut_reproductif, quantite)
    VALUES (v_lot_f01, 'DEJA_REPRODUCTRICE_APTE', 4),
           (v_lot_f01, 'PRETE_JAMAIS_SAILLIE',    1),
           (v_lot_f01, 'A_SURVEILLER',            1),
           (v_lot_f02, 'DEJA_REPRODUCTRICE_APTE', 3),
           (v_lot_f02, 'EN_CYCLE',                2);

    -- =====================================================
    -- GROUPES DE REPRODUCTION
    --  G1 : mise bas prevue dans 3 jours  -> alerte MISE_BAS_PROCHE
    --  G2 : portee precedente cloturee    -> historique
    --  G3 : mise bas depassee de 2 jours  -> alerte RETARD_MISE_BAS
    --  (date_prevue_mise_bas est calculee automatiquement par la base)
    -- =====================================================
    INSERT INTO groupes_reproduction(code_groupe, lot_femelle_id, lot_male_id, nombre_femelles_concernees, nombre_males_utilises, date_saillie, duree_gestation_jours, nb_femelles_gestantes, nb_femelles_non_gestantes, statut, observation, created_by)
    VALUES ('GRP-2026-01', v_lot_f01, v_lot_m01, 6, 1, CURRENT_DATE - 111, 114, 5, 1, 'EN_GESTATION', 'Gestation en cours, mise bas proche', v_admin)
    RETURNING id INTO v_grp1;

    INSERT INTO groupes_reproduction(code_groupe, lot_femelle_id, lot_male_id, nombre_femelles_concernees, nombre_males_utilises, date_saillie, duree_gestation_jours, date_mise_bas_reelle, nb_femelles_gestantes, nb_femelles_non_gestantes, nb_femelles_mise_bas, nb_porcelets_nes, nb_porcelets_vivants, nb_porcelets_morts, statut, observation, created_by)
    VALUES ('GRP-2026-02', v_lot_f01, v_lot_m01, 6, 1, CURRENT_DATE - 150, 114, CURRENT_DATE - 34, 5, 1, 5, 48, 44, 4, 'CLOTURE', 'Portee precedente sevree', v_admin)
    RETURNING id INTO v_grp2;

    INSERT INTO groupes_reproduction(code_groupe, lot_femelle_id, lot_male_id, nombre_femelles_concernees, nombre_males_utilises, date_saillie, duree_gestation_jours, nb_femelles_gestantes, nb_femelles_non_gestantes, statut, observation, created_by)
    VALUES ('GRP-2026-03', v_lot_f02, v_lot_m01, 5, 1, CURRENT_DATE - 116, 114, 5, 0, 'EN_GESTATION', 'Mise bas depassee, a controler', v_admin)
    RETURNING id INTO v_grp3;

    -- =====================================================
    -- ALERTES REPRODUCTION
    -- Message identique a celui du scheduler => pas de doublon
    -- genere par la tache planifiee.
    -- =====================================================
    INSERT INTO alertes_reproduction(groupe_reproduction_id, lot_id, type_alerte, message, date_alerte, statut)
    VALUES (v_grp1, v_lot_f01, 'MISE_BAS_PROCHE', 'Le groupe GRP-2026-01 a une mise bas proche.', CURRENT_DATE, 'NON_LUE'),
           (v_grp3, v_lot_f02, 'RETARD_MISE_BAS', 'Le groupe GRP-2026-03 a une mise bas en retard.', CURRENT_DATE, 'NON_LUE');

    -- =====================================================
    -- SANTE : vaccinations (dont un rappel a venir)
    -- =====================================================
    INSERT INTO vaccinations(lot_id, vaccin_id, date_vaccination, date_rappel, observation)
    VALUES (v_lot_f01, (SELECT id FROM vaccins WHERE nom = 'Parvovirose'),   CURRENT_DATE - 60, CURRENT_DATE + 4,  'Rappel a venir'),
           (v_lot_f02, (SELECT id FROM vaccins WHERE nom = 'Parvovirose'),   CURRENT_DATE - 50, CURRENT_DATE + 14, NULL),
           (v_lot_e01, (SELECT id FROM vaccins WHERE nom = 'Peste porcine'), CURRENT_DATE - 45, CURRENT_DATE + 20, NULL),
           (v_lot_c01, (SELECT id FROM vaccins WHERE nom = 'Rouget'),        CURRENT_DATE - 10, NULL, NULL);

    -- =====================================================
    -- SANTE : suivis sanitaires
    -- =====================================================
    INSERT INTO suivis_sanitaires(lot_id, maladie_id, traitement_id, date_diagnostic, date_traitement, date_guerison, observation)
    VALUES (v_lot_e01,
            (SELECT id FROM maladies WHERE nom = 'Diarrhée'),
            (SELECT id FROM traitements WHERE nom = 'Réhydratation et traitement vétérinaire'),
            CURRENT_DATE - 25, CURRENT_DATE - 24, CURRENT_DATE - 20, 'Gueri'),
           (v_lot_c01,
            (SELECT id FROM maladies WHERE nom = 'Toux'),
            (SELECT id FROM traitements WHERE nom = 'Traitement respiratoire'),
            CURRENT_DATE - 6, CURRENT_DATE - 5, NULL, 'Traitement en cours');

    -- =====================================================
    -- STOCKS ALIMENTAIRES
    -- Tourteau volontairement sous le seuil -> stock faible visible
    -- =====================================================
    UPDATE ingredients SET stock_actuel = 250, updated_at = CURRENT_TIMESTAMP WHERE nom = 'Maïs';
    UPDATE ingredients SET stock_actuel = 120, updated_at = CURRENT_TIMESTAMP WHERE nom = 'Son de riz';
    UPDATE ingredients SET stock_actuel = 8,   updated_at = CURRENT_TIMESTAMP WHERE nom = 'Tourteau';

    INSERT INTO mouvements_stock_aliment(ingredient_id, type_mouvement, quantite, date_mouvement, stock_apres, observation)
    VALUES ((SELECT id FROM ingredients WHERE nom = 'Maïs'),       'ENTREE', 300, CURRENT_DATE - 20, 300, 'Achat de mais'),
           ((SELECT id FROM ingredients WHERE nom = 'Maïs'),       'SORTIE', 50,  CURRENT_DATE - 5,  250, 'Distribution ration'),
           ((SELECT id FROM ingredients WHERE nom = 'Son de riz'), 'ENTREE', 120, CURRENT_DATE - 18, 120, 'Achat son de riz'),
           ((SELECT id FROM ingredients WHERE nom = 'Tourteau'),   'ENTREE', 40,  CURRENT_DATE - 30, 40,  'Achat tourteau'),
           ((SELECT id FROM ingredients WHERE nom = 'Tourteau'),   'SORTIE', 32,  CURRENT_DATE - 3,  8,   'Distribution ration');

    -- =====================================================
    -- COMMERCE : ventes + details
    -- =====================================================
    INSERT INTO ventes(client_id, date_vente, montant_total, statut)
    VALUES ((SELECT id FROM clients WHERE nom = 'Boucherie Centrale'), CURRENT_DATE - 6, 4212000, 'VALIDEE')
    RETURNING id INTO v_vente1;
    INSERT INTO details_vente(vente_id, lot_id, quantite, poids_total, prix_unitaire, montant)
    VALUES (v_vente1, v_lot_e01, 6, 468, 9000, 4212000);

    INSERT INTO ventes(client_id, date_vente, montant_total, statut)
    VALUES ((SELECT id FROM clients WHERE nom = 'Marche Ambohipo'), CURRENT_DATE - 2, 2210000, 'VALIDEE')
    RETURNING id INTO v_vente2;
    INSERT INTO details_vente(vente_id, lot_id, quantite, poids_total, prix_unitaire, montant)
    VALUES (v_vente2, v_lot_c01, 4, 260, 8500, 2210000);

    -- =====================================================
    -- FINANCE : depenses (mois courant + precedent)
    -- =====================================================
    INSERT INTO depenses(categorie_id, date_depense, montant, description)
    VALUES ((SELECT id FROM categories_depenses WHERE nom = 'Alimentation'), CURRENT_DATE - 20, 900000, 'Achat de mais et provende'),
           ((SELECT id FROM categories_depenses WHERE nom = 'Alimentation'), CURRENT_DATE - 3,  450000, 'Complement alimentaire'),
           ((SELECT id FROM categories_depenses WHERE nom = 'Santé'),        CURRENT_DATE - 25, 180000, 'Vaccins et honoraires veterinaires'),
           ((SELECT id FROM categories_depenses WHERE nom = 'Personnel'),    CURRENT_DATE - 30, 600000, 'Salaire ouvrier'),
           ((SELECT id FROM categories_depenses WHERE nom = 'Transport'),    CURRENT_DATE - 6,  120000, 'Livraison des porcs'),
           ((SELECT id FROM categories_depenses WHERE nom = 'Maintenance'),  CURRENT_DATE - 40, 250000, 'Reparation des enclos');

    -- =====================================================
    -- DESTINATAIRES DES ALERTES (page Administration)
    -- =====================================================
    INSERT INTO destinataires_alerte(email)
    SELECT 'responsable.elevage@madaporc.local'
    WHERE NOT EXISTS (SELECT 1 FROM destinataires_alerte WHERE email = 'responsable.elevage@madaporc.local');

    RAISE NOTICE 'Donnees de demonstration inserees avec succes.';
END $$;
