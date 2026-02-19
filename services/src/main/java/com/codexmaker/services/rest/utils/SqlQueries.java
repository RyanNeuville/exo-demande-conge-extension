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

    public static final String SELECT_SOLDE_UTILISATEUR = "SELECT solde_conges FROM utilisateur WHERE id = ?";

    public static final String SELECT_ALL_UTILISATEURS = "SELECT id, nom, prenom, username, email, role, solde_conges "
            +
            "FROM utilisateur ORDER BY nom";

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

    public static final String UPDATE_TYPE_CONGE = "UPDATE type_conge SET " +
            "code = ?, libelle = ?, description = ?, jours_max_par_an = ?, deduction_solde = ? " +
            "WHERE id = ?";

    public static final String DELETE_TYPE_CONGE = "DELETE FROM type_conge WHERE id = ?";

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

    public static final String ANNULER_DEMANDE_EN_ATTENTE = "UPDATE demande_conge SET " +
            "statut = 'ANNULEE', date_modification = ?, date_validation = ? " +
            "WHERE id = ?";

    public static final String SOUMETTRE_DEMANDE_EN_ATTENTE = "UPDATE demande_conge SET " +
            "statut = 'EN_ATTENTE', date_modification = ?, date_validation = ? " +
            "WHERE id = ?";

    public static final String CONSULTER_DEMANDE_A_TRAITER = "SELECT id, numero, user_id, nom, prenom, date_debut, demi_journee_debut, "
            +
            "date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, " +
            "commentaire_valideur, valideur_id, date_soumission, date_modification, " +
            "date_validation, solde_conges_avant, duree_jours_ouvres " +
            "FROM demande_conge WHERE statut = 'EN_ATTENTE' AND valideur_id = ? ORDER BY date_soumission ASC";

    public static final String UPDATE_STATUT_VALIDE = "UPDATE demande_conge SET " +
            "statut = 'VALIDE', date_modification = ?, date_validation = ?, commentaire_valideur = ? " +
            "WHERE id = ?";

    public static final String UPDATE_STATUT_REFUSE = "UPDATE demande_conge SET " +
            "statut = 'REFUSE', date_modification = ?, date_validation = ?, commentaire_valideur = ? " +
            "WHERE id = ?";

    public static final String AJOUTER_COMMENTAIRE_VALIDATION = "UPDATE demande_conge SET " +
            "commentaire_valideur = ? " +
            "WHERE id = ?" + " AND numero = ?";

    public static final String SUPPRIMER_DEMANDE = "DELETE FROM demande_conge WHERE id = ? AND numero = ?";

    public static final String SELECT_DEMANDES_PAR_VALIDEUR = "SELECT * FROM demande_conge WHERE valideur_id = ? ORDER BY date_soumission DESC";

    /** HISTORIQUE_ETAT */
    public static final String INSERT_HISTORIQUE_ETAT = "INSERT INTO historique_etat (" +
            "id, demande_id, statut_avant, statut_apres, date_changement, " +
            "utilisateur_change, commentaire) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?)";

    /** Requêtes utilitaires */
    public static final String COUNT_DEMANDES_BY_USER_AND_STATUT = "SELECT COUNT(*) FROM demande_conge " +
            "WHERE user_id = ? AND statut = ?";

    public static final String SELECT_SOLDE_BY_USER_ID = "SELECT solde_conges FROM utilisateur WHERE id = ?";

    /**
     * REQUETES POUR UNE VUE STATISTIQUES COTE FRONTEND (EMPLOYE, RESPONSABLE,
     * ADMINISTATEUR)
     */

    /** DASHBOARD EMPLOYE */
    public static final String DASH_EMPLOYE_TOTAL_DEMANDES = "SELECT COUNT(*) AS total_demandes FROM demande_conge WHERE user_id = ?";

    public static final String DASH_EMPLOYE_PAR_STATUT = "SELECT statut, COUNT(*) AS count FROM demande_conge WHERE user_id = ? GROUP BY statut";

    public static final String DASH_EMPLOYE_SOLDE = "SELECT solde_conges FROM utilisateur WHERE id = ?";

    public static final String DASH_EMPLOYE_LAST_5 = "SELECT id, numero, date_soumission, statut, type_conge_id, motif, duree_jours_ouvres "
            +
            "FROM demande_conge WHERE user_id = ? ORDER BY date_soumission DESC LIMIT 5";

    public static final String DASH_EMPLOYE_JOURS_PRIS_ANNEE = "SELECT SUM(duree_jours_ouvres) AS jours_pris_annee FROM demande_conge "
            +
            "WHERE user_id = ? AND statut = 'VALIDEE' " +
            "AND strftime('%Y', date_debut) = strftime('%Y', 'now')";

    /** DASHBOARD RESPONSABLE */
    public static final String DASH_RESP_DEMANDES_A_TRAITER = "SELECT COUNT(*) AS demandes_a_traiter FROM demande_conge "
            +
            "WHERE valideur_id = ? AND statut = 'EN_ATTENTE'";

    public static final String DASH_RESP_PAR_TYPE = "SELECT tc.libelle, COUNT(*) AS count FROM demande_conge dc " +
            "JOIN type_conge tc ON dc.type_conge_id = tc.id " +
            "WHERE dc.valideur_id = ? AND dc.statut = 'EN_ATTENTE' GROUP BY tc.libelle";

    public static final String DASH_RESP_VALIDEE_REFUSEE_MOIS = "SELECT statut, COUNT(*) AS count FROM demande_conge " +
            "WHERE valideur_id = ? " +
            "AND strftime('%Y-%m', date_validation) = strftime('%Y-%m', 'now') " +
            "GROUP BY statut";

    public static final String DASH_RESP_LAST_5_A_TRAITER = "SELECT id, numero, user_id, nom, prenom, date_soumission, motif, duree_jours_ouvres "
            +
            "FROM demande_conge WHERE valideur_id = ? AND statut = 'EN_ATTENTE' " +
            "ORDER BY date_soumission ASC LIMIT 5";

    public static final String DASH_RESP_NB_EMPLOYES = "SELECT COUNT(DISTINCT user_id) AS nb_employes FROM demande_conge WHERE valideur_id = ?";

    /** DASHBOARD ADMINISTRATEUR */
    public static final String DASH_ADMIN_TOTAL_DEMANDES = "SELECT COUNT(*) AS total_demandes FROM demande_conge";

    public static final String DASH_ADMIN_PAR_STATUT = "SELECT statut, COUNT(*) AS count FROM demande_conge GROUP BY statut";

    public static final String DASH_ADMIN_PAR_TYPE_CONGE = "SELECT tc.libelle, COUNT(*) AS count FROM demande_conge dc "
            +
            "JOIN type_conge tc ON dc.type_conge_id = tc.id GROUP BY tc.libelle";

    public static final String DASH_ADMIN_JOURS_PAR_MOIS = "SELECT strftime('%Y-%m', date_debut) AS mois, SUM(duree_jours_ouvres) AS jours_pris "
            +
            "FROM demande_conge WHERE statut = 'VALIDEE' " +
            "AND strftime('%Y', date_debut) = strftime('%Y', 'now') " +
            "GROUP BY mois ORDER BY mois";

    public static final String DASH_ADMIN_TOP_5_USERS = "SELECT user_id, nom, prenom, SUM(duree_jours_ouvres) AS total_jours "
            +
            "FROM demande_conge WHERE statut = 'VALIDEE' " +
            "GROUP BY user_id, nom, prenom ORDER BY total_jours DESC LIMIT 5";

    public static final String DASH_ADMIN_SOLDE_MOYEN_PAR_ROLE = "SELECT role, AVG(solde_conges) AS solde_moyen FROM utilisateur GROUP BY role";

    /** VERIFICATION DES CHEVAUCHEMENT DE CONGES */
    public static final String CHECK_CHEVAUCHEMENT = "SELECT COUNT(*) FROM demande_conge " +
            "WHERE user_id = ? " +
            "AND statut NOT IN ('REFUSEE', 'ANNULEE') " +
            "AND id != ? " +
            "AND (date_debut <= ? AND date_fin >= ?) " +
            "LIMIT 1";
}