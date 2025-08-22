package com.codexmaker.services.utils;

/**
 * Classe contenant les constantes utilisées dans l'application.
 * Ces constantes sont utilisées pour les messages de succès et d'erreur,
 * ainsi que pour d'autres valeurs statiques.
 */

public class Constants {

    /** constantes pour les requetes SQL */
    public static final String SQL_GET_ALL_LEAVE_REQUESTS = "SELECT * FROM %s";
    public static final String SQL_GET_LEAVE_REQUEST_BY_ID = "SELECT * FROM %s WHERE id = ?";
    public static final String SQL_CREATE_LEAVE_REQUEST = "INSERT INTO %s (user_id, date_debut, date_fin, type_conge, statut, motif, date_soumission, date_modification, solde_demande, duree_jours_ouvres) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String SQL_UPDATE_LEAVE_REQUEST = "UPDATE %s SET date_debut = ?, date_fin = ?, type_conge = ?, statut = ?, motif = ?, date_modification = ?, solde_demande = ?, duree_jours_ouvres = ? WHERE id = ?";
    public static final String SQL_DELETE_LEAVE_REQUEST = "DELETE FROM %s WHERE id = ?";
    public static final String SQL_APPROVE_LEAVE_REQUEST = "UPDATE %s SET statut = 'APPROVED' WHERE id = ?";
    public static final String SQL_REJECT_LEAVE_REQUEST = "UPDATE %s SET statut = 'REJECTED' WHERE id = ?";
    public static final String SQL_GET_LEAVE_REQUEST_BY_USER_ID = "SELECT * FROM %s WHERE user_id = ?";
    public static final String SQL_GET_LEAVE_REQUEST_BY_STATUS = "SELECT * FROM %s WHERE statut = ?";
    public static final String SQL_GET_LEAVE_REQUEST_BY_DATE_RANGE = "SELECT * FROM %s WHERE date_debut >= ? AND date_fin <= ?";
    public static final String SQL_GET_LEAVE_REQUEST_BY_TYPE = "SELECT * FROM %s WHERE type_conge = ?";
    public static final String SQL_GET_LEAVE_REQUEST_BALANCE = "SELECT SUM(duree_jours_ouvres) FROM %s WHERE user_id = ? AND statut = 'APPROVED' AND date_debut <= ? AND date_fin >= ?";
    public static final String SQL_GET_LEAVE_REQUEST_BY_MOTIF = "SELECT * FROM %s WHERE motif LIKE ?";

    /** creation d'une vue SQL pour les demandes de conge d'un utilisateur */
    public static final String SQL_CREATE_VIEW_LEAVE_REQUESTS_BY_USER = "CREATE OR REPLACE VIEW vue_demande_conge_utilisateur AS " +
            "SELECT d.id, d.date_debut, d.date_fin, d.type_conge, d.statut, d.motif, d.date_soumission, d.date_modification, d.solde_demande, d.duree_jours_ouvres, " +
            "u.nom, u.prenom " +
            "FROM %s d " +
            "JOIN utilisateur u ON d.user_id = u.id";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_USER = "SELECT * FROM vue_demande_conge_utilisateur WHERE user_id = ?";

    /** requetes sql pour une approche statistiques */
    public static final String SQL_GET_LEAVE_REQUESTS_COUNT_BY_STATUS = "SELECT statut, COUNT(*) AS count FROM %s GROUP BY statut";
    public static final String SQL_GET_LEAVE_REQUESTS_COUNT_BY_TYPE = "SELECT type_conge, COUNT(*) AS count FROM %s GROUP BY type_conge";
    public static final String SQL_GET_LEAVE_REQUESTS_COUNT_BY_USER = "SELECT user_id, COUNT(*) AS count FROM %s GROUP BY user_id";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_DATE = "SELECT * FROM %s WHERE date_debut >= ? AND date_fin <= ?";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_USER_AND_STATUS = "SELECT * FROM %s WHERE user_id = ? AND statut = ?";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_USER_AND_TYPE = "SELECT * FROM %s WHERE user_id = ? AND type_conge = ?";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_USER_AND_DATE_RANGE = "SELECT * FROM %s WHERE user_id = ? AND date_debut >= ? AND date_fin <= ?";
    public static final String SQL_GET_LEAVE_REQUESTS_BY_USER_AND_MOTIF = "SELECT * FROM %s WHERE user_id = ? AND motif LIKE ?";
    

    /** constantes pour la connexion a la base de donnees */
    public static final String DB_URL = "jdbc:mysql://localhost:3306/exo_demande_conges";
    public static final String DB_USER = "root";
    public static final String DB_PASSWORD = "NOUVEAU_MOT_DE_PASSE";
    public static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    /** constantes pour les messages */
    public static final String NO_LEAVE_REQUEST = "Aucune demande de congé trouvée";
    public static final String ERROR_MESSAGE = "Une erreur s'est produite: %s";
    public static final String GET_ALL_LEAVE_REQUESTS = "Récupération de toutes les demandes de congé";
    public static final int MAX_LEAVE_REQUEST_BALANCE = 30;
    public static final String CLOSING_CONNECTION = "Fermeture de la connexion à la base de données";

    /** messages de success */
    public static final String SUCCESS_MESSAGE = "Opération réussie";
    public static final String SUCCESS_LEAVE_REQUEST_CREATED = "Demande de congé créée avec succès";
    public static final String SUCCESS_LEAVE_REQUEST_UPDATED = "Demande de congé mise à jour avec succès";
    public static final String SUCCESS_LEAVE_REQUEST_DELETED = "Demande de congé supprimée avec succès";
    public static final String SUCCESS_APROUVED_LEAVE_REQUEST = "Demande de congé approuvée avec succès";
    public static final String SUCCESS_REJECTED_LEAVE_REQUEST = "Demande de congé refusee avec succès";
    public static final String SUCCESS_CONNECTION = "Connexion à la base de données établie avec succès.";
    public static final String SUCCESS_CLOSE_CONNECTION = "Connexion à la base de données fermée avec succès";

    /** messages d'erreur */
    public static final String ERROR_MESSAGE_INVALID_DATE = "La date de début ne peut pas être après la date de fin.";
    public static final String ERROR_MESSAGE_INVALID_DURATION = "La demande doit couvrir au moins un jour.";
    public static final String USER_ERROR_NOT_FOUND = "Utilisateur non trouvé";
    public static final String ERROR_REQUEST_NOT_FOUND = "Demande non trouvée";
    public static final String ERROR_REQUEST_ALREADY_EXISTS = "Une demande de congé existe déjà pour cette période.";
    public static final String ERROR_REQUEST_NOT_PENDING = "Seules les demandes en attente peuvent être approuvées.";
    public static final String ERROR_REQUEST_NOT_APPROVED = "Seules les demandes approuvées peuvent être refusées.";
    public static final String ERROR_DATABASE_CONNECTION = "Erreur de connexion à la base de données";
    public static final String FAILED_TO_CONNECT_TO_DATABASE = "Échec de la connexion à la base de données. Veuillez vérifier les paramètres de connexion.";
    public static final String ERROR_DRIVER_NOT_FOUND = "Le pilote de base de données n'a pas été trouvé. Veuillez vérifier la configuration du projet.";
    public static final String ERROR_CLOSE_CONNECTION = "Erreur lors de la fermeture de la connexion à la base de données";


}