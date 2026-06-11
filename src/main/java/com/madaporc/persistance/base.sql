

CREATE DATABASE MadaPorc;

\c MadaPorc

-- =============================================================
-- 1. GESTION DES RÔLES ET PERMISSIONS
-- =============================================================

CREATE TABLE roles (
    id           SERIAL PRIMARY KEY,
    libelle      VARCHAR(100) NOT NULL UNIQUE,e
    niveau_acces INTEGER,
    description  TEXT,
    created_at   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE permissions (
    id      SERIAL PRIMARY KEY,
    code    VARCHAR(100) NOT NULL UNIQUE,
    libelle VARCHAR(150) NOT NULL,
    module  VARCHAR(100)
);

CREATE TABLE role_permissions (
    role_id       INT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id INT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

CREATE TABLE statuts_utilisateur (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE utilisateurs (
    id                    SERIAL PRIMARY KEY,
    nom                   VARCHAR(150) NOT NULL,
    email                 VARCHAR(150) NOT NULL UNIQUE,
    mot_de_passe_hash     VARCHAR(255) NOT NULL,
    role_id               INT REFERENCES roles(id) ON DELETE SET NULL,
    statut_utilisateur_id INT REFERENCES statuts_utilisateur(id) ON DELETE SET NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 2. RÉFÉRENTIELS DE BASE
-- =============================================================

CREATE TABLE races (
    id          SERIAL PRIMARY KEY,
    libelle     VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE sexes (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

-- =============================================================
-- 3. LOTS DE PORCS
-- =============================================================

CREATE TABLE statuts_lots (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE lots_porcs (
    id                      SERIAL PRIMARY KEY,
    code_lot                VARCHAR(50) NOT NULL UNIQUE,
    type_entree             VARCHAR(30) CHECK (type_entree IN ('Naissance', 'Achat')),
    race_id                 INT REFERENCES races(id) ON DELETE SET NULL,
    statut_lot_id           INT REFERENCES statuts_lots(id) ON DELETE SET NULL,
    nombre_initial          INTEGER NOT NULL,
    nombre_actuel           INTEGER NOT NULL,
    nombre_males_initial    INTEGER,
    nombre_femelles_initial INTEGER,
    nombre_males_actuel     INTEGER,
    nombre_femelles_actuel  INTEGER,
    nombre_morts            INTEGER NOT NULL DEFAULT 0,
    date_naissance_estimee  DATE,
    date_achat              DATE,
    prix_achat_total        DECIMAL(12,2),
    poids_moyen_initial_kg  DECIMAL(10,2),
    poids_moyen_actuel_kg   DECIMAL(10,2),
    observation             TEXT,
    created_by              INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    archived_at             TIMESTAMP
);

CREATE TABLE type_mouvements_lots (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mouvements_lots_porcs (
    id                    SERIAL PRIMARY KEY,
    lot_porc_id           INT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,
    type_mouvement_lot_id INT NOT NULL REFERENCES type_mouvements_lots(id) ON DELETE RESTRICT,
    quantite              INTEGER NOT NULL,
    quantite_male         INTEGER,
    quantite_femelle      INTEGER,
    date_mouvement        TIMESTAMP NOT NULL,
    motif                 TEXT,
    created_by            INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at            TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE pesees_lots (
    id             SERIAL PRIMARY KEY,
    lot_porc_id    INT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,
    poids_moyen_kg DECIMAL(10,2) NOT NULL,
    date_pesee     DATE NOT NULL,
    observation    TEXT,
    created_by     INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 4. REPRODUCTEURS
-- =============================================================

CREATE TABLE statuts_reproducteurs (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE reproducteurs (
    id                     SERIAL PRIMARY KEY,
    code_reproducteur      VARCHAR(50) NOT NULL UNIQUE,
    nom                    VARCHAR(100),
    race_id                INT REFERENCES races(id) ON DELETE SET NULL,
    sexe_id                INT REFERENCES sexes(id) ON DELETE SET NULL,
    statut_reproducteur_id INT REFERENCES statuts_reproducteurs(id) ON DELETE SET NULL,
    date_naissance         DATE,
    date_arrivee           DATE,
    prix_achat             DECIMAL(12,2),
    poids_kg               DECIMAL(10,2),
    nombre_parite          INTEGER,
    observation            TEXT,
    created_by             INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at             TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    archived_at            TIMESTAMP
);

CREATE TABLE type_evenements_reproduction (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE evenements_reproduction (
    id                             SERIAL PRIMARY KEY,
    femelle_id                     INT NOT NULL REFERENCES reproducteurs(id) ON DELETE RESTRICT,
    male_id                        INT REFERENCES reproducteurs(id) ON DELETE SET NULL,
    type_evenement_reproduction_id INT NOT NULL REFERENCES type_evenements_reproduction(id) ON DELETE RESTRICT,
    lot_porc_id                    INT REFERENCES lots_porcs(id) ON DELETE SET NULL,
    date_evenement                 DATE NOT NULL,
    nombre_porcelets_nes           INTEGER,
    nombre_porcelets_morts         INTEGER,
    nombre_porcelets_vivants       INTEGER,
    observation                    TEXT,
    created_by                     INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at                     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cycles_production (
    id                SERIAL PRIMARY KEY,
    code_cycle        VARCHAR(50) NOT NULL UNIQUE,
    lot_porc_id       INT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,
    date_debut        DATE NOT NULL,
    date_fin_prevue   DATE,
    date_fin_reelle   DATE,
    nombre_naissances INTEGER,
    nombre_pertes     INTEGER,
    nombre_vivants    INTEGER,
    nombre_vendables  INTEGER,
    statut_cycle      VARCHAR(50),
    observation       TEXT,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 5. SANTÉ (MALADIES, TRAITEMENTS, SUIVIS, VACCINATIONS)
-- =============================================================

CREATE TABLE maladies (
    id          SERIAL PRIMARY KEY,
    libelle     VARCHAR(150) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE traitements (
    id                   SERIAL PRIMARY KEY,
    maladie_id           INT NOT NULL REFERENCES maladies(id) ON DELETE CASCADE,
    libelle              VARCHAR(150) NOT NULL,
    prix                 DECIMAL(12,2),
    duree_guerison_jours INTEGER,
    description          TEXT
);

CREATE TABLE statuts_suivi_sanitaire (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE suivis_sanitaires (
    id                        SERIAL PRIMARY KEY,
    lot_porc_id               INT REFERENCES lots_porcs(id) ON DELETE CASCADE,
    reproducteur_id           INT REFERENCES reproducteurs(id) ON DELETE CASCADE,
    nombre_porcs_malades      INTEGER,
    maladie_id                INT NOT NULL REFERENCES maladies(id) ON DELETE RESTRICT,
    traitement_id             INT REFERENCES traitements(id) ON DELETE SET NULL,
    statut_suivi_sanitaire_id INT NOT NULL REFERENCES statuts_suivi_sanitaire(id) ON DELETE RESTRICT,
    date_diagnostic           DATE NOT NULL,
    date_guerison_prevue      DATE,
    date_guerison_reelle      DATE,
    symptomes                 TEXT,
    diagnostic                TEXT,
    observation               TEXT,
    created_by                INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at                TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_suivi_cible CHECK (
        lot_porc_id IS NOT NULL OR reproducteur_id IS NOT NULL
    )
);

CREATE TABLE vaccins (
    id                 SERIAL PRIMARY KEY,
    libelle            VARCHAR(150) NOT NULL UNIQUE,
    prix               DECIMAL(12,2),
    delai_rappel_jours INTEGER,
    description        TEXT,
    actif              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE vaccinations (
    id               SERIAL PRIMARY KEY,
    lot_porc_id      INT REFERENCES lots_porcs(id) ON DELETE CASCADE,
    reproducteur_id  INT REFERENCES reproducteurs(id) ON DELETE CASCADE,
    vaccin_id        INT NOT NULL REFERENCES vaccins(id) ON DELETE RESTRICT,
    date_vaccination DATE NOT NULL,
    date_rappel      DATE,
    dose             VARCHAR(50),
    observation      TEXT,
    created_by       INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_vaccination_cible CHECK (
        lot_porc_id IS NOT NULL OR reproducteur_id IS NOT NULL
    )
);

-- =============================================================
-- 6. ALIMENTATION (INGRÉDIENTS, MÉLANGES, DISTRIBUTIONS)
-- =============================================================

CREATE TABLE ingredients (
    id              SERIAL PRIMARY KEY,
    libelle         VARCHAR(150) NOT NULL UNIQUE,
    prix_kg         DECIMAL(12,2) NOT NULL,
    stock_actuel_kg DECIMAL(12,2) NOT NULL DEFAULT 0,
    seuil_min_kg    DECIMAL(12,2) NOT NULL DEFAULT 0,
    unite           VARCHAR(20) NOT NULL DEFAULT 'kg',
    actif           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE type_mouvements_stock (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE mouvements_stock_aliment (
    id                      SERIAL PRIMARY KEY,
    ingredient_id           INT NOT NULL REFERENCES ingredients(id) ON DELETE RESTRICT,
    type_mouvement_stock_id INT NOT NULL REFERENCES type_mouvements_stock(id) ON DELETE RESTRICT,
    quantite_kg             DECIMAL(12,2) NOT NULL,
    prix_total              DECIMAL(12,2),
    date_mouvement          TIMESTAMP NOT NULL,
    motif                   TEXT,
    created_by              INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE melanges (
    id          SERIAL PRIMARY KEY,
    libelle     VARCHAR(150) NOT NULL UNIQUE,
    description TEXT,
    cout_kg     DECIMAL(12,2),
    statut      VARCHAR(50),
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE melange_ingredients (
    id            SERIAL PRIMARY KEY,
    melange_id    INT NOT NULL REFERENCES melanges(id) ON DELETE CASCADE,
    ingredient_id INT NOT NULL REFERENCES ingredients(id) ON DELETE RESTRICT,
    quantite_kg   DECIMAL(12,2),
    pourcentage   DECIMAL(5,2),
    CONSTRAINT uq_melange_ingredient UNIQUE (melange_id, ingredient_id)
);

CREATE TABLE ratios_croissance (
    id                   SERIAL PRIMARY KEY,
    race_id              INT NOT NULL REFERENCES races(id) ON DELETE CASCADE,
    melange_id           INT NOT NULL REFERENCES melanges(id) ON DELETE CASCADE,
    age_mois_min         INTEGER NOT NULL,
    age_mois_max         INTEGER NOT NULL,
    ration_kg_jour       DECIMAL(10,2),
    gain_poids_estime_kg DECIMAL(10,2),
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE distributions_aliment (
    id                SERIAL PRIMARY KEY,
    lot_porc_id       INT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,
    melange_id        INT NOT NULL REFERENCES melanges(id) ON DELETE RESTRICT,
    date_distribution DATE NOT NULL,
    quantite_kg       DECIMAL(12,2) NOT NULL,
    cout_total        DECIMAL(12,2),
    observation       TEXT,
    created_by        INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- =============================================================
-- 7. VENTES ET FACTURATION
-- =============================================================

CREATE TABLE clients (
    id          SERIAL PRIMARY KEY,
    nom         VARCHAR(150) NOT NULL,
    type_client VARCHAR(50),
    contact     VARCHAR(50),
    email       VARCHAR(150),
    adresse     TEXT,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ventes (
    id            SERIAL PRIMARY KEY,
    client_id     INT NOT NULL REFERENCES clients(id) ON DELETE RESTRICT,
    date_vente    TIMESTAMP NOT NULL,
    montant_total DECIMAL(12,2) NOT NULL,
    statut_vente  VARCHAR(50) CHECK (statut_vente IN ('Brouillon', 'Validee', 'Annulee')),
    observation   TEXT,
    created_by    INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE details_vente (
    id                  SERIAL PRIMARY KEY,
    vente_id            INT NOT NULL REFERENCES ventes(id) ON DELETE CASCADE,
    lot_porc_id         INT NOT NULL REFERENCES lots_porcs(id) ON DELETE RESTRICT,
    nombre_porcs_vendus INTEGER NOT NULL,
    poids_total_kg      DECIMAL(12,2),
    prix_kg             DECIMAL(12,2),
    prix_unitaire       DECIMAL(12,2),
    montant             DECIMAL(12,2) NOT NULL
);

CREATE TABLE paiements (
    id            SERIAL PRIMARY KEY,
    vente_id      INT NOT NULL REFERENCES ventes(id) ON DELETE CASCADE,
    montant       DECIMAL(12,2) NOT NULL,
    mode_paiement VARCHAR(50),
    reference     VARCHAR(100),
    date_paiement TIMESTAMP NOT NULL
);

CREATE TABLE factures (
    id             SERIAL PRIMARY KEY,
    vente_id       INT NOT NULL UNIQUE REFERENCES ventes(id) ON DELETE CASCADE,
    numero_facture VARCHAR(100) NOT NULL UNIQUE,
    date_facture   TIMESTAMP NOT NULL,
    montant_total  DECIMAL(12,2) NOT NULL,
    fichier_url    VARCHAR(255)
);

-- =============================================================
-- 8. DÉPENSES ET TRANSPORT
-- =============================================================

CREATE TABLE categories_depenses (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE depenses (
    id                   SERIAL PRIMARY KEY,
    categorie_depense_id INT NOT NULL REFERENCES categories_depenses(id) ON DELETE RESTRICT,
    libelle              VARCHAR(150) NOT NULL,
    montant              DECIMAL(12,2) NOT NULL,
    date_depense         DATE NOT NULL,
    description          TEXT,
    created_by           INT REFERENCES utilisateurs(id) ON DELETE SET NULL,
    created_at           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE transports (
    id             SERIAL PRIMARY KEY,
    depense_id     INT REFERENCES depenses(id) ON DELETE SET NULL,
    libelle        VARCHAR(150),
    trajet         VARCHAR(255),
    cout           DECIMAL(12,2),
    date_transport DATE
);

-- =============================================================
-- 9. RH (EMPLOYÉS, PRÉSENCES, SALAIRES)
-- =============================================================

CREATE TABLE postes_employes (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE statuts_employes (
    id      SERIAL PRIMARY KEY,
    libelle VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE employes (
    id                SERIAL PRIMARY KEY,
    nom               VARCHAR(100) NOT NULL,
    prenom            VARCHAR(100),
    contact           VARCHAR(50),
    adresse           TEXT,
    poste_employe_id  INT REFERENCES postes_employes(id) ON DELETE SET NULL,
    statut_employe_id INT REFERENCES statuts_employes(id) ON DELETE SET NULL,
    date_embauche     DATE,
    salaire_base      DECIMAL(12,2),
    created_at        TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE presences (
    id              SERIAL PRIMARY KEY,
    employe_id      INT NOT NULL REFERENCES employes(id) ON DELETE CASCADE,
    date_presence   DATE NOT NULL,
    statut_presence VARCHAR(50) CHECK (statut_presence IN ('Present', 'Absent', 'Retard')),
    heure_arrivee   TIME,
    heure_depart    TIME,
    observation     TEXT,
    CONSTRAINT uq_presence_employe_date UNIQUE (employe_id, date_presence)
);

CREATE TABLE salaires_employes (
    id              SERIAL PRIMARY KEY,
    employe_id      INT NOT NULL REFERENCES employes(id) ON DELETE CASCADE,
    mois            INTEGER NOT NULL CHECK (mois BETWEEN 1 AND 12),
    annee           INTEGER NOT NULL,
    montant_base    DECIMAL(12,2),
    prime           DECIMAL(12,2),
    retenue         DECIMAL(12,2),
    montant_net     DECIMAL(12,2),
    statut_paiement VARCHAR(50) CHECK (statut_paiement IN ('En attente', 'Paye')),
    date_paiement   DATE,
    CONSTRAINT uq_salaire_employe_mois_annee UNIQUE (employe_id, mois, annee)
);

