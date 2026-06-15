INSERT INTO roles(id,libelle) VALUES
(1,'Admin'),
(2,'Utilisateur');

INSERT INTO permissions(id,code,libelle,module) VALUES
(1,'PMSS01','gérant','gestion globale');

INSERT INTO role_permissions(role_id,permission_id) VALUES
(1,1);

INSERT INTO statuts_utilisateur(id,libelle) VALUES
(1,'Actif'),
(2,'Inactif');

INSERT INTO utilisateurs(id,nom,email,mot_de_passe_hash,role_id,statut_utilisateur_id,created_at,updated_at) VALUES
(1,'CP','chief@gmail.com','$2y$10$WiJCo0FpczlO/u4175ScQu/5IF/EYGgYUTmWoLMHBGmrMsc8/CO1K',1,1,'2026-01-01',NULL);

INSERT INTO races(id,libelle) VALUES
(1,'Large White'),
(2,'Landrace'),
(3,'Duroc'),
(4,'Pietrain'),
(5,'Hampshire');

INSERT INTO sexes(id,libelle) VALUES
(1,'Mâle'),
(2,'Femelle');

INSERT INTO statuts_lots(id,libelle) VALUES
(1,'Actif'),
(2,'Inactif');

INSERT INTO lots_porcs(id,code_lot,type_entree,race_id,statut_lot_id,nombre_initial,nombre_actuel,nombre_males_initial,nombre_femelles_initial,nombre_males_actuel,nombre_femelles_actuel,nombre_morts,date_naissance_estimee,date_achat,prix_achat_total,poids_moyen_initial_kg,poids_moyen_actuel_kg,observation,created_by,created_at,archived_at) VALUES
(1,'LT001','Naissance',1,1,20,20,11,9,11,9,0,'2026-01-01',NULL,10000000,10000,20000,'en bonne santé',1,'2026-01-01',NULL),
(2,'LT002','Naissance',2,2,15,15,8,7,8,7,0,'2026-01-05',NULL,7500000,10000,15000,'en bonne santé',1,'2026-01-05',NULL);

INSERT INTO type_mouvements_lots(id,libelle) VALUES
(1,'Naissance'),
(2,'Achat'),
(3,'Perte'),
(4,'Décès'),
(5,'Vente'),
(6,'Archivage');


INSERT INTO roles(id,libelle) VALUES
(1,'Admin'),
(2,'Utilisateur');

INSERT INTO permissions(id,code,libelle,module) VALUES
(1,'PMSS01','gérant','gestion globale');

INSERT INTO role_permissions(role_id,permission_id) VALUES
(1,1);

INSERT INTO statuts_utilisateur(id,libelle) VALUES
(1,'Actif'),
(2,'Inactif');

INSERT INTO utilisateurs(id,nom,email,mot_de_passe_hash,role_id,statut_utilisateur_id,created_at,updated_at) VALUES
(1,'CP','chief@gmail.com','$2y$10$WiJCo0FpczlO/u4175ScQu/5IF/EYGgYUTmWoLMHBGmrMsc8/CO1K',1,1,'2026-01-01',NULL);

INSERT INTO races(id,libelle) VALUES
(1,'Large White'),
(2,'Landrace'),
(3,'Duroc'),
(4,'Pietrain'),
(5,'Hampshire');

INSERT INTO sexes(id,libelle) VALUES
(1,'Mâle'),
(2,'Femelle');

INSERT INTO statuts_lots(id,libelle) VALUES
(1,'Actif'),
(2,'Inactif');

INSERT INTO lots_porcs(id,code_lot,type_entree,race_id,statut_lot_id,nombre_initial,nombre_actuel,nombre_males_initial,nombre_femelles_initial,nombre_males_actuel,nombre_femelles_actuel,nombre_morts,date_naissance_estimee,date_achat,prix_achat_total,poids_moyen_initial_kg,poids_moyen_actuel_kg,observation,created_by,created_at,archived_at) VALUES
(1,'LT001','Naissance',1,1,20,20,11,9,11,9,0,'2026-01-01',NULL,10000000,10000,20000,'en bonne santé',1,'2026-01-01',NULL),
(2,'LT002','Naissance',2,2,15,15,8,7,8,7,0,'2026-01-05',NULL,7500000,10000,15000,'en bonne santé',1,'2026-01-05',NULL);

INSERT INTO type_mouvements_lots(id,libelle) VALUES
(1,'Naissance'),
(2,'Achat'),
(3,'Perte'),
(4,'Décès'),
(5,'Vente'),
(6,'Archivage');