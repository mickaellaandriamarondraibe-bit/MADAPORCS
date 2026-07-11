-- =====================================================
-- DONNEES INITIALES MADAPORC
-- Version propre pour test mise bas
-- Aujourd'hui : 23 juin 2026
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

INSERT INTO utilisateurs(nom, prenom, email, mot_de_passe, role_id)
VALUES (
    'Administrateur',
    'MADAPORC',
    'admin@madaporc.local',
    '$2a$10$Rd4XjgqiGoS5e3sYu7T8OeLGXy6RMe2/6dgQZphKGoMVxVCZ5XMvC',
    (SELECT id FROM roles WHERE nom = 'ADMIN')
)
ON CONFLICT (email)
DO UPDATE SET
    mot_de_passe = EXCLUDED.mot_de_passe,
    role_id = EXCLUDED.role_id;
-- =====================================================
-- 3. RACES
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
-- 11. LOTS DE PORCS (jeu de depart commun - recette)
-- Prerequis pour les tests Reproduction, Ventes, Sante,
-- Mouvements et Pesees. Idempotent via code_lot (UNIQUE).
-- Baseline attendue : 4 lots actifs, 29 porcs actifs.
-- =====================================================

-- =====================================================
-- 12. CLIENTS (jeu de depart commun - recette)
-- Prerequis pour les tests Ventes. Idempotent via nom.
-- =====================================================

INSERT INTO clients(nom, telephone, adresse)
SELECT 'Boucherie Centrale', '0341234567', 'Antananarivo'
WHERE NOT EXISTS (
SELECT 1 FROM clients WHERE nom = 'Boucherie Centrale'
);

INSERT INTO clients(nom, telephone, adresse)
SELECT 'Marche Ambohipo', '0329876543', 'Ambohipo'
WHERE NOT EXISTS (
SELECT 1 FROM clients WHERE nom = 'Marche Ambohipo'
);