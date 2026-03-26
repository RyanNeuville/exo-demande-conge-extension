package com.codexmaker.services.rest.utils;

/**
 * Centralise toutes les requêtes SQL utilisées par les repositories.
 * Optimisé pour la syntaxe SQLite. 
 * Les requêtes sont regroupées par entité ou par usage (Dashboard, Vérification).
 */
public final class SqlQueries {

        private SqlQueries() {
                /** Constructeur privé. **/
        }

        /** --- REQUETES UTILISATEUR --- **/
        
        public static final String INSERT_UTILISATEUR = "INSERT INTO utilisateur (id, nom, prenom, username, email, role, solde_conges) VALUES (?, ?, ?, ?, ?, ?, ?)";
        public static final String SELECT_UTILISATEUR_BY_ID = "SELECT id, nom, prenom, username, email, role, solde_conges FROM utilisateur WHERE id = ?";
        public static final String UPDATE_SOLDE_UTILISATEUR = "UPDATE utilisateur SET solde_conges = ? WHERE id = ?";
        public static final String SELECT_SOLDE_UTILISATEUR = "SELECT solde_conges FROM utilisateur WHERE id = ?";
        public static final String SELECT_ALL_UTILISATEURS = "SELECT id, nom, prenom, username, email, role, solde_conges FROM utilisateur ORDER BY nom";
        public static final String SELECT_ALL_RESPONSABLES = "SELECT id, nom, prenom, username, email, role, solde_conges FROM utilisateur WHERE role = 'RESPONSABLE' ORDER BY nom";
        public static final String EXISTS_UTILISATEUR_BY_ID = "SELECT COUNT(*) FROM utilisateur WHERE id = ?";
        public static final String EXISTS_UTILISATEUR_BY_EMAIL = "SELECT COUNT(*) FROM utilisateur WHERE email = ?";

        /** --- REQUETES TYPE_CONGE --- **/
        
        public static final String INSERT_TYPE_CONGE = "INSERT INTO type_conge (id, code, libelle, description, jours_max_par_an, deduction_solde) VALUES (?, ?, ?, ?, ?, ?)";
        public static final String SELECT_TYPE_CONGE_BY_ID = "SELECT id, code, libelle, description, jours_max_par_an, deduction_solde FROM type_conge WHERE id = ?";
        public static final String SELECT_ALL_TYPES_CONGE = "SELECT id, code, libelle, description, jours_max_par_an, deduction_solde FROM type_conge ORDER BY libelle";
        public static final String UPDATE_TYPE_CONGE = "UPDATE type_conge SET code = ?, libelle = ?, description = ?, jours_max_par_an = ?, deduction_solde = ? WHERE id = ?";
        public static final String SELECT_TYPE_CONGE_USED = "SELECT COUNT(*) FROM demande_conge WHERE type_conge_id = ?";
        public static final String DELETE_TYPE_CONGE = "DELETE FROM type_conge WHERE id = ?";

        /** --- REQUETES DEMANDE_CONGE --- **/
        
        public static final String INSERT_DEMANDE_CONGE = "INSERT INTO demande_conge (id, numero, user_id, date_debut, demi_journee_debut, date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, commentaire_valideur, valideur_id, date_soumission, date_modification, date_validation, solde_conges_avant, duree_jours_ouvres) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        public static final String UPDATE_DEMANDE_CONGE = "UPDATE demande_conge SET user_id = ?, date_debut = ?, demi_journee_debut = ?, date_fin = ?, demi_journee_fin = ?, type_conge_id = ?, statut = ?, motif = ?, commentaire_employe = ?, commentaire_valideur = ?, valideur_id = ?, date_modification = ?, solde_conges_avant = ?, duree_jours_ouvres = ? WHERE id = ?";
        public static final String SELECT_DEMANDE_BY_ID = "SELECT id, numero, user_id, date_debut, demi_journee_debut, date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, commentaire_valideur, valideur_id, date_soumission, date_modification, date_validation, solde_conges_avant, duree_jours_ouvres FROM demande_conge WHERE id = ?";
        public static final String SELECT_DEMANDES_BY_USER_ID = "SELECT id, numero, user_id, date_debut, demi_journee_debut, date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, commentaire_valideur, valideur_id, date_soumission, date_modification, date_validation, solde_conges_avant, duree_jours_ouvres FROM demande_conge WHERE user_id = ? ORDER BY date_soumission DESC";
        public static final String SELECT_ALL_DEMANDES = "SELECT id, numero, user_id, date_debut, demi_journee_debut, date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, commentaire_valideur, valideur_id, date_soumission, date_modification, date_validation, solde_conges_avant, duree_jours_ouvres FROM demande_conge ORDER BY date_soumission DESC";
        public static final String UPDATE_STATUT_DEMANDE = "UPDATE demande_conge SET statut = ?, commentaire_valideur = ?, date_modification = ?, date_validation = ? WHERE id = ?";
        public static final String CONSULTER_DEMANDE_A_TRAITER = "SELECT id, numero, user_id, date_debut, demi_journee_debut, date_fin, demi_journee_fin, type_conge_id, statut, motif, commentaire_employe, commentaire_valideur, valideur_id, date_soumission, date_modification, date_validation, solde_conges_avant, duree_jours_ouvres FROM demande_conge WHERE statut = 'EN_ATTENTE' AND valideur_id = ? ORDER BY date_soumission ASC";
        public static final String SUPPRIMER_DEMANDE_BY_ID = "DELETE FROM demande_conge WHERE id = ?";

        /** --- REQUETES HISTORIQUE --- **/
        
        public static final String INSERT_HISTORIQUE_ETAT = "INSERT INTO historique_etat (id, demande_id, statut_avant, statut_apres, date_changement, utilisateur_change, commentaire) VALUES (?, ?, ?, ?, ?, ?, ?)";
        public static final String GET_HISTORIQUE_ETAT_BY_DEMANDE_ID = "SELECT id, demande_id, statut_avant, statut_apres, date_changement, utilisateur_change, commentaire FROM historique_etat WHERE demande_id = ? ORDER BY date_changement ASC";

        /** --- REQUETES DASHBOARD & STATS --- **/
        
        public static final String DASH_EMPLOYE_TOTAL_DEMANDES = "SELECT COUNT(*) AS total_demandes FROM demande_conge WHERE user_id = ?";
        public static final String DASH_EMPLOYE_PAR_STATUT = "SELECT statut, COUNT(*) AS count FROM demande_conge WHERE user_id = ? GROUP BY statut";
        public static final String DASH_EMPLOYE_JOURS_PRIS_ANNEE = "SELECT SUM(duree_jours_ouvres) AS jours_pris_annee FROM demande_conge WHERE user_id = ? AND statut = 'VALIDEE' AND strftime('%Y', date_debut) = strftime('%Y', 'now')";
        public static final String DASH_RESP_DEMANDES_A_TRAITER = "SELECT COUNT(*) AS demandes_a_traiter FROM demande_conge WHERE valideur_id = ? AND statut = 'EN_ATTENTE'";
        public static final String DASH_ADMIN_TOTAL_DEMANDES = "SELECT COUNT(*) AS total_demandes FROM demande_conge";

        /** --- VERIFICATION --- **/
        
        public static final String CHECK_CHEVAUCHEMENT = "SELECT COUNT(*) FROM demande_conge WHERE user_id = ? AND statut NOT IN ('REFUSEE', 'ANNULEE') AND id != ? AND (date_debut <= ? AND date_fin >= ?) LIMIT 1";
}