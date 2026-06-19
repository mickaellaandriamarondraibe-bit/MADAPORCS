INSERT INTO roles(id, libelle, niveau_acces, description, created_at) VALUES
(1, 'Admin', 100, 'Administrateur global', '2026-01-01'),
(2, 'Utilisateur', 10, 'Utilisateur standard', '2026-01-01')
ON CONFLICT (id) DO NOTHING;

INSERT INTO permissions(id, code, libelle, module) VALUES
(1, 'PMSS01', 'gerant', 'gestion globale')
ON CONFLICT (id) DO NOTHING;

INSERT INTO role_permissions(role_id, permission_id) VALUES
(1, 1)
ON CONFLICT (role_id, permission_id) DO NOTHING;

INSERT INTO statuts_utilisateur(id, libelle) VALUES
(1, 'Actif'),
(2, 'Inactif')
ON CONFLICT (id) DO NOTHING;

INSERT INTO utilisateurs(id, nom, email, mot_de_passe_hash, role_id, statut_utilisateur_id, created_at, updated_at) VALUES
(1, 'CP', 'chief@gmail.com', '$2y$10$WiJCo0FpczlO/u4175ScQu/5IF/EYGgYUTmWoLMHBGmrMsc8/CO1K', 1, 1, '2026-01-01', NULL)
ON CONFLICT (id) DO NOTHING;

INSERT INTO races(id, libelle, description) VALUES
(1, 'Large White', 'Race porcine blanche, reconnue pour sa prolificite.'),
(2, 'Landrace', 'Race porcine blanche, bonne qualite maternelle.'),
(3, 'Duroc', 'Race rustique, croissance rapide et viande de qualite.'),
(4, 'Pietrain', 'Race charcutiere, forte conformation musculaire.'),
(5, 'Hampshire', 'Race robuste, adaptee aux croisements.')
ON CONFLICT (id) DO NOTHING;

INSERT INTO sexes(id, libelle) VALUES
(1, 'Male'),
(2, 'Femelle')
ON CONFLICT (id) DO NOTHING;

INSERT INTO statuts_lots(id, libelle) VALUES
(1, 'Actif'),
(2, 'Inactif')
ON CONFLICT (id) DO NOTHING;

INSERT INTO type_mouvements_lots(id, libelle) VALUES
(1, 'Naissance'),
(2, 'Achat'),
(3, 'Perte'),
(4, 'Deces'),
(5, 'Vente'),
(6, 'Archivage')
ON CONFLICT (id) DO NOTHING;

SELECT setval(pg_get_serial_sequence('roles', 'id'), COALESCE((SELECT MAX(id) FROM roles), 1));
SELECT setval(pg_get_serial_sequence('permissions', 'id'), COALESCE((SELECT MAX(id) FROM permissions), 1));
SELECT setval(pg_get_serial_sequence('statuts_utilisateur', 'id'), COALESCE((SELECT MAX(id) FROM statuts_utilisateur), 1));
SELECT setval(pg_get_serial_sequence('utilisateurs', 'id'), COALESCE((SELECT MAX(id) FROM utilisateurs), 1));
SELECT setval(pg_get_serial_sequence('races', 'id'), COALESCE((SELECT MAX(id) FROM races), 1));
SELECT setval(pg_get_serial_sequence('sexes', 'id'), COALESCE((SELECT MAX(id) FROM sexes), 1));
SELECT setval(pg_get_serial_sequence('statuts_lots', 'id'), COALESCE((SELECT MAX(id) FROM statuts_lots), 1));
SELECT setval(pg_get_serial_sequence('lots_porcs', 'id'), COALESCE((SELECT MAX(id) FROM lots_porcs), 1));
SELECT setval(pg_get_serial_sequence('type_mouvements_lots', 'id'), COALESCE((SELECT MAX(id) FROM type_mouvements_lots), 1));
