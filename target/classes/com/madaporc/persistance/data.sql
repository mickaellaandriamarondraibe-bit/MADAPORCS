-- Data for table `mouvements_lots_porcs`


INSERT INTO roles VALUES 
(1, 'Admin'),
(2, 'Utilisateur');

INSERT INTO permissions VALUES 
(1, 'PMSS01', 'gérant', 'gestion globale');

INSERT INTO statuts_utilisateur VALUES 
(1, 'Actif'),
(2, 'Inactif');

-- mot de passe = okokOKOK1!
INSERT INTO utilisateurs VALUES 
(1, 'CP', 'chief@gmail.com', '$2y$10$WiJCo0FpczlO/u4175ScQu/5IF/EYGgYUTmWoLMHBGmrMsc8/CO1K', 1, 1, '2026-01-01', null);

INSERT INTO races VALUES 
(1, 'Large White'),
(2, 'Landrace'),
(3, 'Duroc'),
(4, 'Pietrain'),
(5, 'Hampshire');

INSERT INTO sexes VALUES
(1, 'Mâle'),
(2, 'Femelle');

INSERT INTO statuts_lots VALUES
(1, 'Actif'),
(2, 'Inactif');

INSERT INTO lots_porcs VALUES
(1, 'LT001', 'Naissance', 1, 1, 20, 20, 11, 9, 11, 9, 0, '2026-01-01', null, 10000000, 10000, 20000, 'en bonne santé', 1, '2026-01-01',null),
(2, 'LT002', 'Naissance', 2, 2, 15, 15, 8, 7, 8, 7, 0, '2026-01-05', null, 7500000, 10000, 15000, 'en bonne santé', 1, '2026-01-05',null);

INSERT INTO type_mouvements_lots VALUES 
(1, 'Naissance'),
(2, 'Achat'),
(3, 'Perte'),
(4, 'Décès'),
(5, 'Vente'),
(6, 'Archivage');