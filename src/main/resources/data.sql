-- =====================================================
-- DONNEES INITIALES (Sécurisées contre les doublons)
-- =====================================================

-- 1. ROLES (Le nom est UNIQUE)
INSERT INTO roles(nom)
VALUES ('ADMIN'), ('GESTIONNAIRE')
ON CONFLICT (nom) DO NOTHING;

-- 2. UTILISATEURS (L'email est UNIQUE)
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

-- 3. RACES (Le nom est UNIQUE)
INSERT INTO races(nom, description)
VALUES
('Large White', 'Race porcine utilisée pour la reproduction.'),
('Landrace', 'Race porcine connue pour ses qualités maternelles.'),
('Duroc', 'Race porcine utilisée pour la croissance et la qualité de viande.'),
('Pietrain', 'Race porcine utilisée pour la conformation musculaire.')
ON CONFLICT (nom) DO NOTHING;

-- 4. PARAMÈTRES REPRODUCTION (race_id est UNIQUE)
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
    id, 8, 60, 7, 60, 70, 114, 5
FROM races
ON CONFLICT (race_id) DO NOTHING;

-- 5. STATUTS REPRODUCTIFS (code est la PRIMARY KEY)
INSERT INTO statuts_reproductifs(code, libelle, description)
VALUES
('PRETE_JAMAIS_SAILLIE', 'Prêtes mais jamais saillies', 'Femelles aptes théoriquement, mais jamais encore testées en reproduction.'),
('DEJA_REPRODUCTRICE_APTE', 'Déjà reproductrices et encore aptes', 'Femelles ayant déjà réussi une reproduction et pouvant continuer.'),
('EN_CYCLE', 'En cycle de reproduction', 'Femelles actuellement engagées dans un cycle de reproduction.'),
('A_SURVEILLER', 'À surveiller', 'Femelles encore utilisables mais présentant un risque.'),
('A_RETIRER_REPRODUCTION', 'À retirer de la reproduction', 'Femelles non recommandées pour une nouvelle reproduction.')
ON CONFLICT (code) DO NOTHING;

-- 6. CATEGORIES DEPENSES (nom est UNIQUE)
INSERT INTO categories_depenses(nom)
VALUES ('Alimentation'), ('Santé'), ('Personnel'), ('Transport'), ('Maintenance')
ON CONFLICT (nom) DO NOTHING;

-- 7. VACCINS & MALADIES (Pas de contrainte UNIQUE d'origine, mais on utilise une sous-requête ou on évite de dupliquer si déjà inséré)
-- Pour simplifier la gestion des tables simples sans contrainte unique, on peut faire un check d'existence rapide :
INSERT INTO vaccins(nom, description)
SELECT 'Peste porcine', 'Vaccin de prévention contre la peste porcine.' WHERE NOT EXISTS (SELECT 1 FROM vaccins WHERE nom = 'Peste porcine');
INSERT INTO vaccins(nom, description)
SELECT 'Parvovirose', 'Vaccin utilisé pour la prévention des troubles reproductifs.' WHERE NOT EXISTS (SELECT 1 FROM vaccins WHERE nom = 'Parvovirose');
INSERT INTO vaccins(nom, description)
SELECT 'Rouget', 'Vaccin contre le rouget du porc.' WHERE NOT EXISTS (SELECT 1 FROM vaccins WHERE nom = 'Rouget');

INSERT INTO maladies(nom, description)
SELECT 'Diarrhée', 'Trouble digestif observé sur les lots.' WHERE NOT EXISTS (SELECT 1 FROM maladies WHERE nom = 'Diarrhée');
INSERT INTO maladies(nom, description)
SELECT 'Toux', 'Symptôme respiratoire.' WHERE NOT EXISTS (SELECT 1 FROM maladies WHERE nom = 'Toux');
INSERT INTO maladies(nom, description)
SELECT 'Fièvre', 'État sanitaire nécessitant une surveillance.' WHERE NOT EXISTS (SELECT 1 FROM maladies WHERE nom = 'Fièvre');

-- 8. TRAITEMENTS
INSERT INTO traitements(maladie_id, nom, description)
SELECT (SELECT id FROM maladies WHERE nom = 'Diarrhée'), 'Réhydratation et traitement vétérinaire', 'Traitement selon prescription.'
WHERE NOT EXISTS (SELECT 1 FROM traitements WHERE nom = 'Réhydratation et traitement vétérinaire');

INSERT INTO traitements(maladie_id, nom, description)
SELECT (SELECT id FROM maladies WHERE nom = 'Toux'), 'Traitement respiratoire', 'Traitement selon prescription.'
WHERE NOT EXISTS (SELECT 1 FROM traitements WHERE nom = 'Traitement respiratoire');

INSERT INTO traitements(maladie_id, nom, description)
SELECT (SELECT id FROM maladies WHERE nom = 'Fièvre'), 'Surveillance et traitement vétérinaire', 'Traitement selon prescription.'
WHERE NOT EXISTS (SELECT 1 FROM traitements WHERE nom = 'Surveillance et traitement vétérinaire');

-- 9. INGREDIENTS
INSERT INTO ingredients(nom, unite, stock_actuel, seuil_alerte)
SELECT 'Maïs', 'kg', 0, 30 WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE nom = 'Maïs');
INSERT INTO ingredients(nom, unite, stock_actuel, seuil_alerte)
SELECT 'Son de riz', 'kg', 0, 30 WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE nom = 'Son de riz');
INSERT INTO ingredients(nom, unite, stock_actuel, seuil_alerte)
SELECT 'Tourteau', 'kg', 0, 20 WHERE NOT EXISTS (SELECT 1 FROM ingredients WHERE nom = 'Tourteau');