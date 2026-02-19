package com.codexmaker.services.rest.utils;

/**
 * Regroupe toutes les requêtes SQL constantes utilisées par les repositories.
 * Optimisé pour SQLite avec UUID en TEXT comme PK.
 */
public final class SqlQueries {

    private SqlQueries() {
    }

    /** UTILISATEUR */
    public static final String INSERT_UTILISATEUR = "INSERT INTO utilisateur (id, nom, prenom, username, email, role, solde_conges) "
            +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    public static final String SELECT_UTILISATEUR_BY_ID = "SELECT id, nom, prenom, username, email, role, solde_conges "
            +
            "FROM utilisateur WHERE id = ?";

    public static final String UPDATE_SOLDE_UTILISATEUR = "UPDATE utilisateur SET solde_conges = ? WHERE id = ?";

    /** TYPE_CONGE */
    public static final String INSERT_TYPE_CONGE = "INSERT INTO type_conge (id, code, libelle, description, jours_max_par_an, deduction_solde) "
            +
            "VALUES (?, ?, ?, ?, ?, ?)";

    public static final String SELECT_TYPE_CONGE_BY_ID = "SELECT id, code, libelle, description, jours_max_par_an, deduction_solde "
            +
            "FROM type_conge WHERE id = ?";

    public static final String SELECT_ALL_TYPES_CONGE = "SELECT id, code, libelle, description, jours_max_par_an, deduction_solde "
            +
            "FROM type_conge ORDER BY libelle";

    /** DEMANDE_CONGE */
    public static final String INSERT_DEMANDE_CONGE = "INSERT INTO demande_conge (" +
            "id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, " +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_DEMANDE_CONGE = "UPDATE demande_conge SET " +
            "user_id = ?, nom = ?, prenom = ?, date_debut = ?, demi_journee_debut = ?, " +
            "date_fin = ?, demi_journee_fin = ?, type_conge_id = ?, statut = ?, motif = ?, " +
            "commentaire_employe = ?, commentaire_valideur = ?, valideur_id = ?, " +
            "date_modification = ?, solde_conges_avant = ?, duree_jours_ouvres = ? " +
            "WHERE id = ?";

    public static final String SELECT_DEMANDE_BY_ID = "SELECT id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, "
            +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres " +
            "FROM demande_conge WHERE id = ?";

    public static final String SELECT_DEMANDES_BY_USER_ID = "SELECT id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, "
            +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres " +
            "FROM demande_conge WHERE user_id = ? ORDER BY date_soumission DESC";

    public static final String SELECT_ALL_DEMANDES = "SELECT id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, "
            +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres " +
            "FROM demande_conge ORDER BY date_soumission DESC";

    public static final String SELECT_DEMANDES_EN_ATTENTE = "SELECT id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, "
            +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres " +
            "FROM demande_conge WHERE statut = 'EN_ATTENTE' ORDER BY date_soumission ASC";

    public static final String UPDATE_STATUT_DEMANDE = "UPDATE demande_conge SET " +
            "statut = ?, commentaire_valideur = ?, date_modification = ?, date_validation = ? " +
            "WHERE id = ?";

    /** HISTORIQUE_ETAT */
    public static final String INSERT_HISTORIQUE_ETAT = "INSERT INTO historique_etat (" +
            "id, demande_id, statut_avant, statut_apres, date_changement, " +
            "utilisateur_change, commentaire) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    /** Requêtes utilitaires */
    public static final String COUNT_DEMANDES_BY_USER_AND_STATUT = "SELECT COUNT(*) FROM demande_conge " +
            "WHERE user_id = ? AND statut = ?";

    public static final String SELECT_SOLDE_BY_USER_ID = "SELECT solde_conges FROM utilisateur WHERE id = ?";
}