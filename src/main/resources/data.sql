-- =====================================================
-- DONNEES INITIALES MADAPORC
-- Version corrigée pour Hibernate/JPA
-- =====================================================

-- =====================================================
-- 1. ROLES
-- =====================================================

INSERT INTO roles(nom)
VALUES 
('ADMIN'),
('GESTIONNAIRE')
ON CONFLICT (nom) DO NOTHING;


-- =====================================================
-- 2. UTILISATEUR ADMIN
-- =====================================================

INSERT INTO utilisateurs(
    nom,
    prenom,
    email,
    mot_de_passe,
    role_id
)
VALUES (
    'Administrateur',
    'MADAPORC',
    'admin@madaporc.local',
    'admin123',
    (SELECT id FROM roles WHERE nom = 'ADMIN')
)
ON CONFLICT (email) DO NOTHING;


-- =====================================================
-- 3. RACES
-- IMPORTANT :
-- Hibernate attend la table "race", pas "races"
-- =====================================================

INSERT INTO race(nom, description)
VALUES
('Large White', 'Race porcine utilisée pour la reproduction.'),
('Landrace', 'Race porcine connue pour ses qualités maternelles.'),
('Duroc', 'Race porcine utilisée pour la croissance et la qualité de viande.'),
('Pietrain', 'Race porcine utilisée pour la conformation musculaire.')
ON CONFLICT (nom) DO NOTHING;


-- =====================================================
-- 4. PARAMETRES DE REPRODUCTION PAR RACE
-- =====================================================

INSERT INTO parametres_reproduction_race(
    race_id,
    age_min_reproduction_mois,
    age_max_reproduction_mois,
    nombre_max_portees,
    seuil_fertilite_min,
    seuil_survie_min,
    duree_gestation_jours,
    jours_alerte_mise_bas
)
SELECT
    r.id,
    8,
    60,
    7,
    60,
    70,
    114,
    5
FROM race r
ON CONFLICT (race_id) DO NOTHING;


-- =====================================================
-- 5. STATUTS REPRODUCTIFS
-- =====================================================

INSERT INTO statuts_reproductifs(
    code,
    libelle,
    description
)
VALUES
(
    'PRETE_JAMAIS_SAILLIE',
    'Prêtes mais jamais saillies',
    'Femelles aptes théoriquement, mais jamais encore testées en reproduction.'
),
(
    'DEJA_REPRODUCTRICE_APTE',
    'Déjà reproductrices et encore aptes',
    'Femelles ayant déjà réussi une reproduction et pouvant continuer.'
),
(
    'EN_CYCLE',
    'En cycle de reproduction',
    'Femelles actuellement engagées dans un cycle de reproduction.'
),
(
    'A_SURVEILLER',
    'À surveiller',
    'Femelles encore utilisables mais présentant un risque.'
),
(
    'A_RETIRER_REPRODUCTION',
    'À retirer de la reproduction',
    'Femelles non recommandées pour une nouvelle reproduction.'
)
ON CONFLICT (code) DO NOTHING;


-- =====================================================
-- 6. CATEGORIES DEPENSES
-- =====================================================

INSERT INTO categories_depenses(nom)
VALUES
('Alimentation'),
('Santé'),
('Personnel'),
('Transport'),
('Maintenance')
ON CONFLICT (nom) DO NOTHING;


-- =====================================================
-- 7. VACCINS
-- =====================================================

INSERT INTO vaccins(nom, description)
SELECT 
    'Peste porcine',
    'Vaccin de prévention contre la peste porcine.'
WHERE NOT EXISTS (
    SELECT 1 FROM vaccins WHERE nom = 'Peste porcine'
);

INSERT INTO vaccins(nom, description)
SELECT 
    'Parvovirose',
    'Vaccin utilisé pour la prévention des troubles reproductifs.'
WHERE NOT EXISTS (
    SELECT 1 FROM vaccins WHERE nom = 'Parvovirose'
);

INSERT INTO vaccins(nom, description)
SELECT 
    'Rouget',
    'Vaccin contre le rouget du porc.'
WHERE NOT EXISTS (
    SELECT 1 FROM vaccins WHERE nom = 'Rouget'
);


-- =====================================================
-- 8. MALADIES
-- =====================================================

INSERT INTO maladies(nom, description)
SELECT 
    'Diarrhée',
    'Trouble digestif observé sur les lots.'
WHERE NOT EXISTS (
    SELECT 1 FROM maladies WHERE nom = 'Diarrhée'
);

INSERT INTO maladies(nom, description)
SELECT 
    'Toux',
    'Symptôme respiratoire.'
WHERE NOT EXISTS (
    SELECT 1 FROM maladies WHERE nom = 'Toux'
);

INSERT INTO maladies(nom, description)
SELECT 
    'Fièvre',
    'État sanitaire nécessitant une surveillance.'
WHERE NOT EXISTS (
    SELECT 1 FROM maladies WHERE nom = 'Fièvre'
);


-- =====================================================
-- 9. TRAITEMENTS
-- =====================================================

INSERT INTO traitements(
    maladie_id,
    nom,
    description
)
SELECT 
    (SELECT id FROM maladies WHERE nom = 'Diarrhée'),
    'Réhydratation et traitement vétérinaire',
    'Traitement selon prescription.'
WHERE NOT EXISTS (
    SELECT 1 
    FROM traitements 
    WHERE nom = 'Réhydratation et traitement vétérinaire'
);

INSERT INTO traitements(
    maladie_id,
    nom,
    description
)
SELECT 
    (SELECT id FROM maladies WHERE nom = 'Toux'),
    'Traitement respiratoire',
    'Traitement selon prescription.'
WHERE NOT EXISTS (
    SELECT 1 
    FROM traitements 
    WHERE nom = 'Traitement respiratoire'
);

INSERT INTO traitements(
    maladie_id,
    nom,
    description
)
SELECT 
    (SELECT id FROM maladies WHERE nom = 'Fièvre'),
    'Surveillance et traitement vétérinaire',
    'Traitement selon prescription.'
WHERE NOT EXISTS (
    SELECT 1 
    FROM traitements 
    WHERE nom = 'Surveillance et traitement vétérinaire'
);


-- =====================================================
-- 10. INGREDIENTS
-- =====================================================

INSERT INTO ingredients(
    nom,
    unite,
    stock_actuel,
    seuil_alerte
)
SELECT 
    'Maïs',
    'kg',
    0,
    30
WHERE NOT EXISTS (
    SELECT 1 FROM ingredients WHERE nom = 'Maïs'
);

INSERT INTO ingredients(
    nom,
    unite,
    stock_actuel,
    seuil_alerte
)
SELECT 
    'Son de riz',
    'kg',
    0,
    30
WHERE NOT EXISTS (
    SELECT 1 FROM ingredients WHERE nom = 'Son de riz'
);

INSERT INTO ingredients(
    nom,
    unite,
    stock_actuel,
    seuil_alerte
)
SELECT 
    'Tourteau',
    'kg',
    0,
    20
WHERE NOT EXISTS (
    SELECT 1 FROM ingredients WHERE nom = 'Tourteau'
);




-- =====================================================
-- 11. LOTS DE TEST POUR REPRODUCTION
-- =====================================================

INSERT INTO lots_porcs (
    code_lot,
    date_creation,
    race_id,
    sexe,
    objectif,
    origine,
    effectif_initial,
    effectif_actuel,
    statut,
    description
)
VALUES (
    'LOT-F-001',
    CURRENT_DATE,
    (SELECT id FROM race WHERE nom = 'Large White'),
    'FEMELLE',
    'REPRODUCTION',
    'ACHAT',
    4,
    4,
    'ACTIF',
    'Lot femelle de test pour reproduction.'
)
ON CONFLICT (code_lot)
DO UPDATE SET
    race_id = EXCLUDED.race_id,
    sexe = EXCLUDED.sexe,
    objectif = EXCLUDED.objectif,
    origine = EXCLUDED.origine,
    effectif_initial = EXCLUDED.effectif_initial,
    effectif_actuel = EXCLUDED.effectif_actuel,
    statut = EXCLUDED.statut,
    description = EXCLUDED.description;


INSERT INTO lots_porcs (
    code_lot,
    date_creation,
    race_id,
    sexe,
    objectif,
    origine,
    effectif_initial,
    effectif_actuel,
    statut,
    description
)
VALUES (
    'LOT-M-001',
    CURRENT_DATE,
    (SELECT id FROM race WHERE nom = 'Large White'),
    'MALE',
    'REPRODUCTION',
    'ACHAT',
    2,
    2,
    'ACTIF',
    'Lot mâle de test pour reproduction.'
)
ON CONFLICT (code_lot)
DO UPDATE SET
    race_id = EXCLUDED.race_id,
    sexe = EXCLUDED.sexe,
    objectif = EXCLUDED.objectif,
    origine = EXCLUDED.origine,
    effectif_initial = EXCLUDED.effectif_initial,
    effectif_actuel = EXCLUDED.effectif_actuel,
    statut = EXCLUDED.statut,
    description = EXCLUDED.description;


-- =====================================================
-- 12. REPARTITION REPRODUCTIVE INITIALE DU LOT FEMELLE
-- Après création du groupe, 2 femelles sont en cycle et 2 restent prêtes.
-- =====================================================

INSERT INTO repartitions_reproductives_lots (
    lot_id,
    statut_reproductif,
    quantite
)
VALUES
(
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    'PRETE_JAMAIS_SAILLIE',
    2
),
(
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    'DEJA_REPRODUCTRICE_APTE',
    0
),
(
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    'EN_CYCLE',
    2
),
(
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    'A_SURVEILLER',
    0
),
(
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    'A_RETIRER_REPRODUCTION',
    0
)
ON CONFLICT (lot_id, statut_reproductif)
DO UPDATE SET
    quantite = EXCLUDED.quantite,
    date_mise_a_jour = CURRENT_TIMESTAMP;


-- =====================================================
-- 13. GROUPE DE REPRODUCTION DE TEST
-- =====================================================

INSERT INTO groupes_reproduction (
    code_groupe,
    lot_femelle_id,
    lot_male_id,
    nombre_femelles_concernees,
    nombre_males_utilises,
    date_saillie,
    duree_gestation_jours,
    nb_femelles_gestantes,
    nb_femelles_non_gestantes,
    nb_femelles_mise_bas,
    nb_porcelets_nes,
    nb_porcelets_vivants,
    nb_porcelets_morts,
    statut,
    observation,
    created_by
)
VALUES (
    'GR-001',
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-F-001'),
    (SELECT id FROM lots_porcs WHERE code_lot = 'LOT-M-001'),
    2,
    1,
    DATE '2026-07-01',
    114,
    0,
    0,
    0,
    0,
    0,
    0,
    'SAILLIE',
    'Groupe de reproduction de test.',
    (SELECT id FROM utilisateurs WHERE email = 'admin@madaporc.local')
)
ON CONFLICT (code_groupe)
DO UPDATE SET
    lot_femelle_id = EXCLUDED.lot_femelle_id,
    lot_male_id = EXCLUDED.lot_male_id,
    nombre_femelles_concernees = EXCLUDED.nombre_femelles_concernees,
    nombre_males_utilises = EXCLUDED.nombre_males_utilises,
    date_saillie = EXCLUDED.date_saillie,
    duree_gestation_jours = EXCLUDED.duree_gestation_jours,
    nb_femelles_gestantes = EXCLUDED.nb_femelles_gestantes,
    nb_femelles_non_gestantes = EXCLUDED.nb_femelles_non_gestantes,
    nb_femelles_mise_bas = EXCLUDED.nb_femelles_mise_bas,
    nb_porcelets_nes = EXCLUDED.nb_porcelets_nes,
    nb_porcelets_vivants = EXCLUDED.nb_porcelets_vivants,
    nb_porcelets_morts = EXCLUDED.nb_porcelets_morts,
    statut = EXCLUDED.statut,
    observation = EXCLUDED.observation,
    created_by = EXCLUDED.created_by,
    updated_at = CURRENT_TIMESTAMP;