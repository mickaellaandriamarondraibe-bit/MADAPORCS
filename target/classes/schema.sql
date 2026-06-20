-- =====================================================
-- BASE DE DONNEES : MADAPORC
-- Spring Boot MVC + JSP + PostgreSQL
-- =====================================================

-- DROP DATABASE IF EXISTS madaporc;
-- CREATE DATABASE madaporc;

-- \c madaporc;

-- =====================================================
-- NETTOYAGE
-- =====================================================

DROP TABLE IF EXISTS imports_exports CASCADE;

DROP TABLE IF EXISTS mouvements_stock_aliment CASCADE;
DROP TABLE IF EXISTS ingredients CASCADE;

DROP TABLE IF EXISTS depenses CASCADE;
DROP TABLE IF EXISTS categories_depenses CASCADE;

DROP TABLE IF EXISTS details_vente CASCADE;
DROP TABLE IF EXISTS ventes CASCADE;
DROP TABLE IF EXISTS clients CASCADE;

DROP TABLE IF EXISTS suivis_sanitaires CASCADE;
DROP TABLE IF EXISTS traitements CASCADE;
DROP TABLE IF EXISTS maladies CASCADE;
DROP TABLE IF EXISTS vaccinations CASCADE;
DROP TABLE IF EXISTS vaccins CASCADE;

DROP TABLE IF EXISTS alertes_reproduction CASCADE;
DROP TABLE IF EXISTS analyses_reproduction_lots CASCADE;
DROP TABLE IF EXISTS groupes_reproduction CASCADE;
DROP TABLE IF EXISTS repartitions_reproductives_lots CASCADE;
DROP TABLE IF EXISTS statuts_reproductifs CASCADE;

DROP TABLE IF EXISTS pesees_lots CASCADE;
DROP TABLE IF EXISTS mouvements_lots_porcs CASCADE;
DROP TABLE IF EXISTS lots_porcs CASCADE;

DROP TABLE IF EXISTS parametres_reproduction_race CASCADE;
DROP TABLE IF EXISTS races CASCADE;

DROP TABLE IF EXISTS utilisateurs CASCADE;
DROP TABLE IF EXISTS roles CASCADE;

-- =====================================================
-- SECURITE
-- =====================================================

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE utilisateurs (
    id BIGSERIAL PRIMARY KEY,

    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100),
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,

    actif BOOLEAN NOT NULL DEFAULT TRUE,

    role_id BIGINT NOT NULL REFERENCES roles(id),

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP
);

-- =====================================================
-- REFERENCES METIER
-- =====================================================

CREATE TABLE races (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL,
    description TEXT
);

CREATE TABLE parametres_reproduction_race (
    id BIGSERIAL PRIMARY KEY,

    race_id BIGINT NOT NULL REFERENCES races(id) ON DELETE CASCADE,

    age_min_reproduction_mois INTEGER NOT NULL DEFAULT 8,
    age_max_reproduction_mois INTEGER NOT NULL DEFAULT 60,

    nombre_max_portees INTEGER NOT NULL DEFAULT 7,

    seuil_fertilite_min NUMERIC(5,2) NOT NULL DEFAULT 60,
    seuil_survie_min NUMERIC(5,2) NOT NULL DEFAULT 70,

    duree_gestation_jours INTEGER NOT NULL DEFAULT 114,
    jours_alerte_mise_bas INTEGER NOT NULL DEFAULT 5,

    UNIQUE (race_id),

    CHECK (age_min_reproduction_mois > 0),
    CHECK (age_max_reproduction_mois > age_min_reproduction_mois),
    CHECK (nombre_max_portees > 0),
    CHECK (seuil_fertilite_min BETWEEN 0 AND 100),
    CHECK (seuil_survie_min BETWEEN 0 AND 100),
    CHECK (duree_gestation_jours BETWEEN 100 AND 130),
    CHECK (jours_alerte_mise_bas >= 0)
);

-- =====================================================
-- CHEPTEL : GESTION PAR LOTS
-- =====================================================

CREATE TABLE lots_porcs (
    id BIGSERIAL PRIMARY KEY,

    code_lot VARCHAR(50) UNIQUE NOT NULL,

    date_creation DATE NOT NULL DEFAULT CURRENT_DATE,

    race_id BIGINT REFERENCES races(id),

    sexe VARCHAR(10) NOT NULL,
    objectif VARCHAR(30) NOT NULL,
    origine VARCHAR(30) NOT NULL DEFAULT 'ACHAT',

    effectif_initial INTEGER NOT NULL,
    effectif_actuel INTEGER NOT NULL,

    statut VARCHAR(30) NOT NULL DEFAULT 'ACTIF',

    lot_parent_id BIGINT REFERENCES lots_porcs(id),

    groupe_reproduction_origine_id BIGINT,

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CHECK (sexe IN ('MALE', 'FEMELLE')),

    CHECK (
        objectif IN (
            'REPRODUCTION',
            'ENGRAISSEMENT',
            'CROISSANCE',
            'VENTE'
        )
    ),

    CHECK (
        origine IN (
            'ACHAT',
            'NAISSANCE',
            'TRANSFERT',
            'AJUSTEMENT'
        )
    ),

    CHECK (effectif_initial > 0),
    CHECK (effectif_actuel >= 0),

    CHECK (
        statut IN (
            'ACTIF',
            'ARCHIVE',
            'VIDE'
        )
    ),

    CHECK (
        lot_parent_id IS NULL
        OR lot_parent_id <> id
    )
);

-- Règle métier :
-- Un lot est soit MALE, soit FEMELLE.
-- Aucun lot mixte n'est autorisé.

-- =====================================================
-- MOUVEMENTS DES LOTS
-- =====================================================

CREATE TABLE mouvements_lots_porcs (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    type_mouvement VARCHAR(50) NOT NULL,
    quantite INTEGER NOT NULL,

    date_mouvement DATE NOT NULL DEFAULT CURRENT_DATE,

    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        type_mouvement IN (
            'ENTREE',
            'NAISSANCE',
            'DECES',
            'VENTE',
            'TRANSFERT_ENTREE',
            'TRANSFERT_SORTIE',
            'AJUSTEMENT'
        )
    ),

    CHECK (quantite > 0)
);

-- =====================================================
-- PESEES DES LOTS
-- =====================================================

CREATE TABLE pesees_lots (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    date_pesee DATE NOT NULL,
    poids_moyen NUMERIC(10,2) NOT NULL,
    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (poids_moyen > 0)
);

-- =====================================================
-- STATUTS REPRODUCTIFS
-- =====================================================

CREATE TABLE statuts_reproductifs (
    code VARCHAR(50) PRIMARY KEY,
    libelle VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE repartitions_reproductives_lots (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    statut_reproductif VARCHAR(50) NOT NULL REFERENCES statuts_reproductifs(code),

    quantite INTEGER NOT NULL DEFAULT 0,

    date_mise_a_jour TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    UNIQUE (lot_id, statut_reproductif),

    CHECK (quantite >= 0)
);

-- =====================================================
-- GROUPES DE REPRODUCTION
-- =====================================================

CREATE TABLE groupes_reproduction (
    id BIGSERIAL PRIMARY KEY,

    code_groupe VARCHAR(50) UNIQUE NOT NULL,

    lot_femelle_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,
    lot_male_id BIGINT REFERENCES lots_porcs(id) ON DELETE SET NULL,

    nombre_femelles_concernees INTEGER NOT NULL,
    nombre_males_utilises INTEGER NOT NULL DEFAULT 1,

    date_saillie DATE NOT NULL,

    duree_gestation_jours INTEGER NOT NULL DEFAULT 114,

    date_prevue_mise_bas DATE GENERATED ALWAYS AS
        (date_saillie + duree_gestation_jours) STORED,

    date_mise_bas_reelle DATE,

    nb_femelles_gestantes INTEGER NOT NULL DEFAULT 0,
    nb_femelles_non_gestantes INTEGER NOT NULL DEFAULT 0,
    nb_femelles_mise_bas INTEGER NOT NULL DEFAULT 0,

    nb_porcelets_nes INTEGER NOT NULL DEFAULT 0,
    nb_porcelets_vivants INTEGER NOT NULL DEFAULT 0,
    nb_porcelets_morts INTEGER NOT NULL DEFAULT 0,

    statut VARCHAR(40) NOT NULL DEFAULT 'SAILLIE',

    observation TEXT,

    created_by BIGINT REFERENCES utilisateurs(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CHECK (nombre_femelles_concernees > 0),
    CHECK (nombre_males_utilises >= 0),

    CHECK (duree_gestation_jours BETWEEN 100 AND 130),

    CHECK (nb_femelles_gestantes >= 0),
    CHECK (nb_femelles_non_gestantes >= 0),
    CHECK (nb_femelles_mise_bas >= 0),

    CHECK (
        nb_femelles_gestantes + nb_femelles_non_gestantes
        <= nombre_femelles_concernees
    ),

    CHECK (nb_femelles_mise_bas <= nb_femelles_gestantes),

    CHECK (nb_porcelets_nes >= 0),
    CHECK (nb_porcelets_vivants >= 0),
    CHECK (nb_porcelets_morts >= 0),

    CHECK (
        nb_porcelets_vivants + nb_porcelets_morts
        <= nb_porcelets_nes
    ),

    CHECK (
        statut IN (
            'SAILLIE',
            'EN_GESTATION',
            'MISE_BAS_PROCHE',
            'MISE_BAS_CONFIRMEE',
            'ECHEC',
            'CLOTURE'
        )
    ),

    CHECK (
        date_mise_bas_reelle IS NULL
        OR date_mise_bas_reelle >= date_saillie
    )
);

ALTER TABLE lots_porcs
ADD CONSTRAINT fk_lots_groupe_reproduction_origine
FOREIGN KEY (groupe_reproduction_origine_id)
REFERENCES groupes_reproduction(id)
ON DELETE SET NULL;

-- =====================================================
-- ANALYSE REPRODUCTIVE
-- =====================================================

CREATE TABLE analyses_reproduction_lots (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    date_analyse TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    nb_pretes_jamais_saillies INTEGER NOT NULL DEFAULT 0,
    nb_deja_reproductrices_aptes INTEGER NOT NULL DEFAULT 0,
    nb_en_cycle INTEGER NOT NULL DEFAULT 0,
    nb_a_surveiller INTEGER NOT NULL DEFAULT 0,
    nb_a_retirer_reproduction INTEGER NOT NULL DEFAULT 0,

    nb_femelles_total INTEGER NOT NULL,

    nb_femelles_saillies_total INTEGER NOT NULL DEFAULT 0,
    nb_femelles_gestantes_total INTEGER NOT NULL DEFAULT 0,

    taux_aptitude_global NUMERIC(5,2),
    taux_recommande NUMERIC(5,2),
    taux_fertilite_observe NUMERIC(5,2),

    decision TEXT,
    commentaire TEXT,

    CHECK (nb_femelles_total > 0),

    CHECK (nb_pretes_jamais_saillies >= 0),
    CHECK (nb_deja_reproductrices_aptes >= 0),
    CHECK (nb_en_cycle >= 0),
    CHECK (nb_a_surveiller >= 0),
    CHECK (nb_a_retirer_reproduction >= 0),

    CHECK (nb_femelles_saillies_total >= 0),
    CHECK (nb_femelles_gestantes_total >= 0),

    CHECK (taux_aptitude_global IS NULL OR taux_aptitude_global BETWEEN 0 AND 100),
    CHECK (taux_recommande IS NULL OR taux_recommande BETWEEN 0 AND 100),
    CHECK (taux_fertilite_observe IS NULL OR taux_fertilite_observe BETWEEN 0 AND 100)
);

-- =====================================================
-- ALERTES REPRODUCTION
-- =====================================================

CREATE TABLE alertes_reproduction (
    id BIGSERIAL PRIMARY KEY,

    groupe_reproduction_id BIGINT REFERENCES groupes_reproduction(id) ON DELETE CASCADE,
    lot_id BIGINT REFERENCES lots_porcs(id) ON DELETE CASCADE,

    type_alerte VARCHAR(50) NOT NULL,
    message TEXT NOT NULL,

    date_alerte DATE NOT NULL DEFAULT CURRENT_DATE,

    statut VARCHAR(30) NOT NULL DEFAULT 'NON_LUE',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        type_alerte IN (
            'MISE_BAS_PROCHE',
            'RETARD_MISE_BAS',
            'SURVEILLANCE_LOT',
            'REFORME_RECOMMANDEE',
            'STOCK_FAIBLE',
            'VACCINATION_A_VENIR'
        )
    ),

    CHECK (
        statut IN (
            'NON_LUE',
            'LUE',
            'TRAITEE'
        )
    ),

    CHECK (
        groupe_reproduction_id IS NOT NULL
        OR lot_id IS NOT NULL
    )
);

-- =====================================================
-- SANTE
-- =====================================================

CREATE TABLE vaccins (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE vaccinations (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    vaccin_id BIGINT NOT NULL REFERENCES vaccins(id),

    date_vaccination DATE NOT NULL,
    date_rappel DATE,

    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        date_rappel IS NULL
        OR date_rappel >= date_vaccination
    )
);

CREATE TABLE maladies (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE traitements (
    id BIGSERIAL PRIMARY KEY,

    maladie_id BIGINT REFERENCES maladies(id),

    nom VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE suivis_sanitaires (
    id BIGSERIAL PRIMARY KEY,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id) ON DELETE CASCADE,

    maladie_id BIGINT REFERENCES maladies(id),
    traitement_id BIGINT REFERENCES traitements(id),

    date_diagnostic DATE NOT NULL,
    date_traitement DATE,
    date_guerison DATE,

    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        date_traitement IS NULL
        OR date_traitement >= date_diagnostic
    ),

    CHECK (
        date_guerison IS NULL
        OR date_guerison >= date_diagnostic
    )
);

-- =====================================================
-- COMMERCE
-- =====================================================

CREATE TABLE clients (
    id BIGSERIAL PRIMARY KEY,

    nom VARCHAR(150) NOT NULL,
    telephone VARCHAR(30),
    adresse TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE ventes (
    id BIGSERIAL PRIMARY KEY,

    client_id BIGINT REFERENCES clients(id),

    date_vente DATE NOT NULL DEFAULT CURRENT_DATE,

    montant_total NUMERIC(12,2) NOT NULL DEFAULT 0,

    statut VARCHAR(30) NOT NULL DEFAULT 'BROUILLON',

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CHECK (montant_total >= 0),

    CHECK (
        statut IN (
            'BROUILLON',
            'VALIDEE',
            'ANNULEE'
        )
    )
);

CREATE TABLE details_vente (
    id BIGSERIAL PRIMARY KEY,

    vente_id BIGINT NOT NULL REFERENCES ventes(id) ON DELETE CASCADE,

    lot_id BIGINT NOT NULL REFERENCES lots_porcs(id),

    quantite INTEGER NOT NULL,

    poids_total NUMERIC(12,2),
    prix_unitaire NUMERIC(12,2),
    montant NUMERIC(12,2),

    CHECK (quantite > 0),
    CHECK (poids_total IS NULL OR poids_total > 0),
    CHECK (prix_unitaire IS NULL OR prix_unitaire >= 0),
    CHECK (montant IS NULL OR montant >= 0)
);

-- =====================================================
-- FINANCE
-- =====================================================

CREATE TABLE categories_depenses (
    id BIGSERIAL PRIMARY KEY,
    nom VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE depenses (
    id BIGSERIAL PRIMARY KEY,

    categorie_id BIGINT REFERENCES categories_depenses(id),

    date_depense DATE NOT NULL DEFAULT CURRENT_DATE,

    montant NUMERIC(12,2) NOT NULL,

    description TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (montant > 0)
);

-- =====================================================
-- STOCKS ALIMENTAIRES
-- =====================================================

CREATE TABLE ingredients (
    id BIGSERIAL PRIMARY KEY,

    nom VARCHAR(150) NOT NULL,
    unite VARCHAR(20),

    stock_actuel NUMERIC(12,2) NOT NULL DEFAULT 0,
    seuil_alerte NUMERIC(12,2) NOT NULL DEFAULT 0,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP,

    CHECK (stock_actuel >= 0),
    CHECK (seuil_alerte >= 0)
);

CREATE TABLE mouvements_stock_aliment (
    id BIGSERIAL PRIMARY KEY,

    ingredient_id BIGINT NOT NULL REFERENCES ingredients(id) ON DELETE CASCADE,

    type_mouvement VARCHAR(20) NOT NULL,

    quantite NUMERIC(12,2) NOT NULL,

    date_mouvement DATE NOT NULL DEFAULT CURRENT_DATE,

    observation TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CHECK (
        type_mouvement IN (
            'ENTREE',
            'SORTIE',
            'AJUSTEMENT'
        )
    ),

    CHECK (quantite > 0)
);

-- =====================================================
-- IMPORT / EXPORT
-- =====================================================

CREATE TABLE imports_exports (
    id BIGSERIAL PRIMARY KEY,

    utilisateur_id BIGINT REFERENCES utilisateurs(id),

    type_operation VARCHAR(20) NOT NULL,

    format_fichier VARCHAR(20) NOT NULL,

    module VARCHAR(100) NOT NULL,

    nom_fichier VARCHAR(255),

    date_operation TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    statut VARCHAR(20) NOT NULL DEFAULT 'SUCCES',

    message TEXT,

    CHECK (
        type_operation IN (
            'IMPORT',
            'EXPORT'
        )
    ),

    CHECK (
        format_fichier IN (
            'EXCEL',
            'PDF'
        )
    ),

    CHECK (
        statut IN (
            'SUCCES',
            'ECHEC'
        )
    )
);

-- =====================================================
-- INDEX
-- =====================================================

CREATE INDEX idx_utilisateur_email
ON utilisateurs(email);

CREATE INDEX idx_lot_code
ON lots_porcs(code_lot);

CREATE INDEX idx_lot_sexe
ON lots_porcs(sexe);

CREATE INDEX idx_lot_objectif
ON lots_porcs(objectif);

CREATE INDEX idx_lot_statut
ON lots_porcs(statut);

CREATE INDEX idx_mouvement_lot
ON mouvements_lots_porcs(lot_id);

CREATE INDEX idx_mouvement_date
ON mouvements_lots_porcs(date_mouvement);

CREATE INDEX idx_pesee_lot
ON pesees_lots(lot_id);

CREATE INDEX idx_repartition_lot
ON repartitions_reproductives_lots(lot_id);

CREATE INDEX idx_groupe_lot_femelle
ON groupes_reproduction(lot_femelle_id);

CREATE INDEX idx_groupe_lot_male
ON groupes_reproduction(lot_male_id);

CREATE INDEX idx_groupe_date_saillie
ON groupes_reproduction(date_saillie);

CREATE INDEX idx_groupe_date_prevue_mise_bas
ON groupes_reproduction(date_prevue_mise_bas);

CREATE INDEX idx_groupe_statut
ON groupes_reproduction(statut);

CREATE INDEX idx_analyse_lot
ON analyses_reproduction_lots(lot_id);

CREATE INDEX idx_analyse_date
ON analyses_reproduction_lots(date_analyse);

CREATE INDEX idx_alerte_statut
ON alertes_reproduction(statut);

CREATE INDEX idx_alerte_type
ON alertes_reproduction(type_alerte);

CREATE INDEX idx_vaccination_lot
ON vaccinations(lot_id);

CREATE INDEX idx_vaccination_rappel
ON vaccinations(date_rappel);

CREATE INDEX idx_suivi_sanitaire_lot
ON suivis_sanitaires(lot_id);

CREATE INDEX idx_vente_date
ON ventes(date_vente);

CREATE INDEX idx_detail_vente_lot
ON details_vente(lot_id);

CREATE INDEX idx_depense_date
ON depenses(date_depense);

CREATE INDEX idx_ingredient_stock
ON ingredients(stock_actuel);

CREATE INDEX idx_mouvement_stock_ingredient
ON mouvements_stock_aliment(ingredient_id);

CREATE INDEX idx_import_export_date
ON imports_exports(date_operation);

-- =====================================================
-- FIN DU SCRIPT
-- =====================================================