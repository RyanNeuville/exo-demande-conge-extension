package com.codexmaker.services.rest.utils;

/**
 * Regroupe toutes les constantes du projet pour éviter les chaînes de
 * caractères en dur.
 * Toutes les valeurs sont en français pour faciliter la lecture et la
 * maintenance.
 */
public final class Constants {

    private Constants() {
    }

    /** CATÉGORIE : ENDPOINTS API (REST) */

    public static final String API_BASE = "/api";

    /** 1. GESTION DES DEMANDES (Base & Employé) */
    /** POST : Soumettre une demande ("Soumettre une demande") */
    /**
     * GET : Consulter l'historique de SES demandes ("Consulter l'historique de ses
     * demandes")
     */
    public static final String API_DEMANDES = API_BASE + "/demandes";
    /** GET : Lire une demande spécifique */
    /**
     * PUT : Modifier une demande en attente ou un brouillon ("Modifier ses demandes
     * en attente")
     */
    /**
     * DELETE : Supprimer une demande (Réservé Administrateur : "Supprimer une
     * demande")
     */
    public static final String API_DEMANDES_BY_ID = API_DEMANDES + "/{id}";
    /** PUT : Annuler une demande en attente ("Annuler ses demandes en attente") */
    public static final String API_DEMANDES_ANNULER = API_DEMANDES_BY_ID + "/annuler";
    /**
     * GET : Consulter l'historique des états d'une demande (Classe
     * "HistoriqueEtat")
     */
    public static final String API_DEMANDES_HISTORIQUE = API_DEMANDES_BY_ID + "/historique";


    /** 2. GESTION MANAGÉRIALE (Responsable) */
    /**
     * GET : Consulter les demandes de son équipe ("Consulter demandes à traiter")
     */
    public static final String API_DEMANDES_A_TRAITER = API_DEMANDES + "/a-traiter";
    /**
     * PUT : Valider une demande ("Valider une demande" + "Ajouter commentaire de
     * validation")
     */
    public static final String API_DEMANDES_VALIDER = API_DEMANDES_BY_ID + "/valider";
    /** PUT : Refuser une demande ("Refuser une demande") */
    public static final String API_DEMANDES_REFUSER = API_DEMANDES_BY_ID + "/refuser";


    /** 3. GESTION ADMINISTRATIVE (Administrateur) */
    /**
     * GET : Consulter absolument toutes les demandes du système ("Consulter toutes
     * les demandes")
     */
    public static final String API_DEMANDES_TOUTES = API_DEMANDES + "/toutes";
    /** GET : Exporter les rapports ("Exporter les rapports") */
    public static final String API_DEMANDES_EXPORTER = API_DEMANDES + "/exporter";

    /** 4. GESTION DES UTILISATEURS / SOLDES (Employé) */

    /**
     * GET : Consulter le solde de l'utilisateur connecté ("Consulter solde congés")
     */
    public static final String API_UTILISATEUR_ME_SOLDE = API_BASE + "/utilisateurs/me/solde";
    /** GET : Consulter les informations personnelles de l'utilisateur connecté */
    public static final String API_UTILISATEUR_ME = API_BASE + "/utilisateurs/me";

    /** 5. GESTION DES TYPES DE CONGÉS (Administrateur & Employé) */

    /** GET : Lister les types de congés (Pour formulaire) */
    /**
     * POST : Créer un nouveau type de congé (Admin : "Gérer les types de congés")
     */
    public static final String API_TYPES_CONGES = API_BASE + "/types-conges";
    /** PUT : Modifier un type de congé */
    /** DELETE : Supprimer un type de congé */
    public static final String API_TYPES_CONGES_BY_ID = API_TYPES_CONGES + "/{id}";

    /** CATÉGORIE : MESSAGES DE SUCCÈS (français, clairs et réutilisables) */

    public static final String SUCCES_DEMANDE_SOUMISE = "Votre demande de congé a été soumise avec succès.";
    public static final String SUCCES_DEMANDE_APPROUVEE = "La demande de congé a été approuvée.";
    public static final String SUCCES_DEMANDE_REFUSEE = "La demande de congé a été refusée.";
    public static final String SUCCES_DEMANDE_ANNULEE = "Votre demande de congé a été annulée.";
    public static final String SUCCES_SOLDE_MIS_A_JOUR = "Solde de congés mis à jour avec succès.";
    public static final String SUCCES_OPERATION_EFFECTUEE = "Opération effectuée avec succès.";

    /** CATÉGORIE : MESSAGES D'ERREUR (français, explicites et réutilisables) */

    public static final String ERREUR_UTILISATEUR_NON_TROUVE = "Utilisateur non trouvé.";
    public static final String ERREUR_DEMANDE_NON_TROUVEE = "Demande de congé non trouvée.";
    public static final String ERREUR_SOLDE_INSUFFISANT = "Solde de congés insuffisant pour cette demande.";
    public static final String ERREUR_CHEVAUCHEMENT_DATES = "Chevauchement détecté avec une autre demande.";
    public static final String ERREUR_STATUT_INCORRECT = "Le statut de la demande ne permet pas cette action.";
    public static final String ERREUR_NON_AUTORISE = "Vous n'êtes pas autorisé à effectuer cette action.";
    public static final String ERREUR_DATE_INVALIDE = "La date de début doit être antérieure ou égale à la date de fin.";
    public static final String ERREUR_MOTIF_VIDE = "Le motif de la demande est obligatoire.";
    public static final String ERREUR_OPERATION_ECHOUEE = "L'opération a échoué. Veuillez réessayer.";
    public static final String ERREUR_TECHNIQUE = "Une erreur technique s'est produite. Contactez l'administrateur.";

    /** CATÉGORIE : MESSAGES DE LOG (pour Logger) */

    public static final String LOG_DEMANDE_SOUMISE = "Demande de congé soumise avec succès - ID: {}";
    public static final String LOG_DEMANDE_APPROUVEE = "Demande approuvée - ID: {} - Valideur: {}";
    public static final String LOG_DEMANDE_REFUSEE = "Demande refusée - ID: {} - Valideur: {}";
    public static final String LOG_DEMANDE_ANNULEE = "Demande annulée par l'utilisateur - ID: {}";
    public static final String LOG_SOLDE_MIS_A_JOUR = "Solde mis à jour pour utilisateur {} : {} jours restants";
    public static final String LOG_CHEVAUCHEMENT_DETECTE = "Tentative de soumission avec chevauchement - User: {}";
    public static final String LOG_ERREUR_TECHNIQUE = "Erreur technique lors de {} - Détail: {}";

    /** CATÉGORIE : VARIABLES STATIQUES / VALEURS FIXES */

    public static final int SOLDE_INITIAL_PAR_DEFAUT = 25;
    public static final int MAX_JOURS_DEMANDE_AUTORISE = 30;
    public static final String FORMAT_NUMERO_DEMANDE = "DC-%d-%04d"; // ex: DC-2025-0001
    public static final String FORMAT_DATE_SQLITE = "yyyy-MM-dd";
    public static final String FORMAT_DATE_HEURE_SQLITE = "yyyy-MM-dd HH:mm:ss";

    /** CATÉGORIE : PATTERNS / REGEX / FORMATS */

    public static final String PATTERN_EMAIL = "^[A-Za-z0-9+_.-]+@(.+)$";
    public static final String PATTERN_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[1-5][0-9a-fA-F]{3}-[89abAB][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    public static final String PATTERN_DATE = "\\d{4}-\\d{2}-\\d{2}";

    /** CATÉGORIE : AUTRES CATÉGORIES UTILES */

    public static final String ROLE_EMPLOYE = "EMPLOYE";
    public static final String ROLE_RESPONSABLE = "RESPONSABLE";
    public static final String ROLE_ADMINISTRATEUR = "ADMINISTRATEUR";

    public static final String STATUT_BROUILLON = "BROUILLON";
    public static final String STATUT_EN_ATTENTE = "EN_ATTENTE";
    public static final String STATUT_VALIDEE = "VALIDEE";
    public static final String STATUT_REFUSEE = "REFUSEE";
    public static final String STATUT_ANNULEE = "ANNULEE";

    public static final String MESSAGE_VALIDATION_OK = "Demande validée avec succès.";
    public static final String MESSAGE_REFUS_OK = "Demande refusée avec succès.";
}