-- ============================================================================
-- init_db.sql fichier d'initialisation de la base de données
-- ============================================================================

-- ============================================================================
-- Optimisation des performances des requetes (recherche par indexations)
-- ============================================================================
PRAGMA journal_mode = WAL;
PRAGMA synchronous = NORMAL;
PRAGMA cache_size = -20000;
PRAGMA temp_store = MEMORY;
PRAGMA mmap_size = 30000000;

-- ============================================================================
-- Tables utilisateur: pour la persistance des données utilisateur (qui viennent de depuis exoplatform)
-- ============================================================================
CREATE TABLE IF NOT EXISTS utilisateur (
    id TEXT PRIMARY KEY,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    username TEXT NOT NULL UNIQUE,
    email TEXT NOT NULL UNIQUE,
    role TEXT NOT NULL CHECK(role IN ('EMPLOYE', 'RESPONSABLE', 'ADMINISTRATEUR')),
    solde_conges INTEGER NOT NULL DEFAULT 25
);

-- ============================================================================
-- Tables type_conge: pour la gestion des types de congés (cote admin)
-- ============================================================================
CREATE TABLE IF NOT EXISTS type_conge (
    id TEXT PRIMARY KEY DEFAULT (
        lower(hex(randomblob(4))) || '-' || 
        lower(hex(randomblob(2))) || '-4' || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        substr('89ab', abs(random()) % 4 + 1, 1) || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        lower(hex(randomblob(6)))
    ),
    code TEXT NOT NULL UNIQUE,
    libelle TEXT NOT NULL,
    description TEXT,
    jours_max_par_an INTEGER NOT NULL,
    deduction_solde BOOLEAN NOT NULL DEFAULT 1
);

-- ================================================================================================
-- Tables demande_conge:  pour la gestion des demandes de congés (cote employe, responsable, admin)
-- ================================================================================================
CREATE TABLE IF NOT EXISTS demande_conge (
    id TEXT PRIMARY KEY DEFAULT (
        lower(hex(randomblob(4))) || '-' || 
        lower(hex(randomblob(2))) || '-4' || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        substr('89ab', abs(random()) % 4 + 1, 1) || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        lower(hex(randomblob(6)))
    ),
    numero TEXT NOT NULL UNIQUE,
    user_id TEXT NOT NULL,
    nom TEXT NOT NULL,
    prenom TEXT NOT NULL,
    date_debut DATE NOT NULL,
    demi_journee_debut BOOLEAN NOT NULL DEFAULT 0,
    date_fin DATE NOT NULL,
    demi_journee_fin BOOLEAN NOT NULL DEFAULT 0,
    type_conge_id TEXT NOT NULL,
    statut TEXT NOT NULL CHECK(statut IN ('BROUILLON', 'EN_ATTENTE', 'VALIDEE', 'REFUSEE', 'ANNULEE')),
    motif TEXT NOT NULL,
    commentaire_employe TEXT,
    commentaire_valideur TEXT,
    valideur_id TEXT,
    date_soumission DATE NOT NULL,
    date_modification DATE,
    date_validation DATE,
    solde_conges_avant INTEGER NOT NULL,
    duree_jours_ouvres INTEGER NOT NULL,
    FOREIGN KEY (user_id) REFERENCES utilisateur(id),
    FOREIGN KEY (type_conge_id) REFERENCES type_conge(id),
    FOREIGN KEY (valideur_id) REFERENCES utilisateur(id)
);

-- =====================================================================================================================================================
-- Tables historique_etat: pour la gestion de l'historique des demandes de congés (cote employe, responsable, admin) et avoir une meilleurs tracabilites
-- =====================================================================================================================================================
CREATE TABLE IF NOT EXISTS historique_etat (
    id TEXT PRIMARY KEY DEFAULT (
        lower(hex(randomblob(4))) || '-' || 
        lower(hex(randomblob(2))) || '-4' || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        substr('89ab', abs(random()) % 4 + 1, 1) || 
        substr(lower(hex(randomblob(2))),2) || '-' || 
        lower(hex(randomblob(6)))
    ),
    demande_id TEXT NOT NULL,
    statut_avant TEXT,
    statut_apres TEXT NOT NULL,
    date_changement DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    utilisateur_change TEXT NOT NULL,
    commentaire TEXT,
    FOREIGN KEY (demande_id) REFERENCES demande_conge(id),
    FOREIGN KEY (utilisateur_change) REFERENCES utilisateur(id)
);

-- ============================================================================
-- Creation des indexes pour optimiser les performances des requetes
-- ============================================================================
CREATE INDEX IF NOT EXISTS idx_demande_user_id ON demande_conge(user_id);
CREATE INDEX IF NOT EXISTS idx_demande_statut ON demande_conge(statut);
CREATE INDEX IF NOT EXISTS idx_demande_valideur_id ON demande_conge(valideur_id);
CREATE INDEX IF NOT EXISTS idx_historique_demande_id ON historique_etat(demande_id);