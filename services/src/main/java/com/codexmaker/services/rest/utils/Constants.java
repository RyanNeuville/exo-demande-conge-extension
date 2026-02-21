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

    /** CATÉGORIE : MESSAGES DE SUCCÈS */

    public static final String SUCCES_DEMANDE_BROUILLON_CREEE = "Brouillon de demande de congé créé avec succès.";
    public static final String SUCCES_DEMANDE_SOUmise = "Votre demande de congé a été soumise avec succès.";
    public static final String SUCCES_DEMANDE_MODIFIEE = "Demande de congé modifiée avec succès.";
    public static final String SUCCES_DEMANDE_ANNULEE = "Votre demande de congé a été annulée avec succès.";
    public static final String SUCCES_DEMANDE_VALIDEE = "La demande de congé a été validée avec succès.";
    public static final String SUCCES_DEMANDE_REFUSEE = "La demande de congé a été refusée avec succès.";
    public static final String SUCCES_COMMENTAIRE_AJOUTE = "Commentaire de validation ajouté avec succès.";
    public static final String SUCCES_DEMANDE_SUPPRIMEE = "Demande de congé supprimée avec succès.";
    public static final String SUCCES_TYPE_CONGE_CREE = "Nouveau type de congé créé avec succès.";
    public static final String SUCCES_TYPE_CONGE_MODIFIE = "Type de congé modifié avec succès.";
    public static final String SUCCES_TYPE_CONGE_SUPPRIME = "Type de congé supprimé avec succès.";
    public static final String SUCCES_SOLDE_MIS_A_JOUR = "Solde de congés mis à jour avec succès.";
    public static final String SUCCES_OPERATION_EFFECTUEE = "Opération effectuée avec succès.";
    public static final String SUCCES_RAPPORT_EXPORT = "Rapport exporté avec succès.";

    /** CATÉGORIE : MESSAGES D'ERREUR */

    public static final String ERREUR_UTILISATEUR_NON_TROUVE = "Utilisateur non trouvé.";
    public static final String ERREUR_DEMANDE_NON_TROUVEE = "Demande de congé non trouvée.";
    public static final String ERREUR_DEMANDE_NON_MODIFIABLE = "Cette demande ne peut plus être modifiée (statut non autorisé).";
    public static final String ERREUR_DEMANDE_NON_ANNULABLE = "Cette demande ne peut plus être annulée (statut non autorisé).";
    public static final String ERREUR_SOLDE_INSUFFISANT = "Solde de congés insuffisant pour cette demande.";
    public static final String ERREUR_CHEVAUCHEMENT_DATES = "Chevauchement détecté avec une autre demande existante.";
    public static final String ERREUR_STATUT_INCORRECT = "Le statut actuel de la demande ne permet pas cette action.";
    public static final String ERREUR_NON_AUTORISE_VALIDER = "Vous n'êtes pas autorisé à valider cette demande.";
    public static final String ERREUR_NON_AUTORISE_REFUSER = "Vous n'êtes pas autorisé à refuser cette demande.";
    public static final String ERREUR_NON_AUTORISE_SUPPRIMER = "Vous n'êtes pas autorisé à supprimer cette demande.";
    public static final String ERREUR_DATE_INVALIDE = "La date de début doit être antérieure ou égale à la date de fin.";
    public static final String ERREUR_MOTIF_VIDE = "Le motif de la demande est obligatoire.";
    public static final String ERREUR_TYPE_CONGE_INEXISTANT = "Type de congé non trouvé ou mise à jour impossible";
    public static final String ERREUR_OPERATION_ECHOUEE = "L'opération a échoué. Veuillez réessayer.";
    public static final String ERREUR_TECHNIQUE = "Une erreur technique s'est produite. Contactez l'administrateur.";
    public static final String ERREUR_VALIDATION_COMMENTAIRE_VIDE = "Le commentaire de validation est obligatoire.";
    public static final String ERREUR_DEMANDE_DEJA_TRAITEE = "Cette demande a déjà été traitée.";
    public static final String ERREUR_TYPE_CONGE_INSERTION_ECHOUEE = "Échec insertion type congé";
    public static final String ERREUR_TYPE_CONGE_CREATION_ECHOUEE = "Échec création type congé";
    public static final String ERREUR_TYPE_CONGE_MODIFICATION_ECHOUEE = "Échec mise à jour type congé";
    public static final String ERREUR_TYPE_CONGE_RECUPERATION_ECHOUEE = "Échec récupération type congé";
    public static final String ERREUR_TYPE_CONGE_SUPPRESSION_ECHOUEE = "Échec suppression type congé";

    /** CATÉGORIE : MESSAGES DE LOG */

    /** Logs généraux opérationnels */
    public static final String LOG_DEMANDE_BROUILLON_CREE = "Brouillon de demande créé - Utilisateur: {} - ID temporaire: {}";
    public static final String LOG_DEMANDE_SOUMISE = "Demande de congé soumise avec succès - ID: {} - Utilisateur: {}";
    public static final String LOG_DEMANDE_MODIFIEE = "Demande modifiée avec succès - ID: {} - Utilisateur: {}";
    public static final String LOG_DEMANDE_ANNULEE = "Demande annulée par l'utilisateur - ID: {} - Utilisateur: {}";
    public static final String LOG_DEMANDE_VALIDEE = "Demande validée - ID: {} - Valideur: {} - Commentaire: {}";
    public static final String LOG_DEMANDE_REFUSEE = "Demande refusée - ID: {} - Valideur: {} - Commentaire: {}";
    public static final String LOG_COMMENTAIRE_AJOUTE = "Commentaire de validation ajouté - Demande ID: {} - Valideur: {}";
    public static final String LOG_DEMANDE_SUPPRIMEE = "Demande supprimée - ID: {} - Par: {} (Administrateur)";
    public static final String LOG_TYPE_CONGE_CREE = "Nouveau type de congé créé - ID: {} - Libellé: {}";
    public static final String LOG_TYPE_CONGE_MODIFIE = "Type de congé modifié - ID: {} - Libellé: {}";
    public static final String LOG_TYPE_CONGE_SUPPRIME = "Type de congé supprimé - ID: {}";
    public static final String LOG_SOLDE_MIS_A_JOUR = "Solde congés mis à jour - Utilisateur: {} - Nouveau solde: {}";
    public static final String LOG_RAPPORT_EXPORT = "Rapport des congés exporté - Par: {} (Administrateur)";
    public static final String LOG_ERREUR_TYPE_CONGE_RECUPERATION = "Erreur lors de la récupération des types de congés ID={} : {}";
    public static final String LOG_ERREUR_TYPE_CONGE_RECUPERATION_TOUS = "Erreur lors de la récupération des types de congés : {}";
    public static final String LOG_ERREUR_TYPE_CONGE_SUPPRESSION = "Erreur lors de la suppression du type de congé ID={} : {}";
    public static final String LOG_UTILISATEUR_CREE = "Utilisateur créé avec succès - ID: {} - Email: {}";

    /** Logs de vérification et erreurs métier */
    public static final String LOG_SOLDE_INSUFFISANT = "Solde insuffisant pour soumission - Utilisateur: {} - Solde: {} - Demandé: {}";
    public static final String LOG_CHEVAUCHEMENT_DETECTE = "Chevauchement de dates détecté - Utilisateur: {} - Demande ID: {}";
    public static final String LOG_STATUT_INCORRECT = "Statut incorrect pour l'action - Demande ID: {} - Statut actuel: {}";
    public static final String LOG_NON_AUTORISE = "Action non autorisée - Utilisateur: {} - Rôle: {} - Action: {}";
    public static final String LOG_DATE_INVALIDE = "Dates invalides - Début: {} - Fin: {} - Utilisateur: {}";
    public static final String LOG_ERREUR_TECHNIQUE = "Erreur technique lors de {} - Utilisateur: {} - Détail: {}";
    public static final String LOG_ERREUR_TYPE_CONGE_CREATION = "Erreur lors de la création du type de congé : {}";
    public static final String LOG_ERREUR_TYPE_CONGE_MODIFICATION = "Erreur lors de la modification du type de congé ID={} : {}";
    public static final String LOG_ERREUR_TYPE_CONGE_UTILISATION = "Erreur lors de la vérification d'utilisation du type de congé ID={} : {}";
    public static final String LOG_ERREUR_UTILISATEUR_CREATION = "Erreur lors de la création de l'utilisateur ID={} : {}";
    public static final String LOG_UTILISATEUR_RECUPERE = "Utilisateur récupéré avec succès - ID: {}";
    public static final String LOG_UTILISATEUR_NON_TROUVE = "Aucun utilisateur trouvé pour l'ID: {}";
    public static final String LOG_SOLDE_RECUPERE = "Solde récupéré pour l'utilisateur ID: {} - Solde: {}";
    public static final String LOG_UTILISATEURS_RECUPERES = "Récupérés {} utilisateurs";
    public static final String LOG_RESPONSABLES_RECUPERES = "Récupérés {} responsables";
    public static final String LOG_UTILISATEUR_EXISTE = "Vérification existence utilisateur ID={} : {}";
    public static final String LOG_EMAIL_EXISTE = "Vérification existence email={} : {}";
    public static final String LOG_ERREUR_UTILISATEUR_RECUPERATION = "Erreur lors de la récupération de l'utilisateur ID={} : {}";
    public static final String LOG_ERREUR_UTILISATEUR_SOLDE = "Erreur lors de la mise à jour du solde utilisateur ID={} : {}";
    public static final String LOG_ERREUR_UTILISATEUR_SOLDE_LECTURE = "Erreur lors de la lecture du solde utilisateur ID={} : {}";
    public static final String LOG_ERREUR_UTILISATEURS_RECUPERATION = "Erreur lors de la récupération de tous les utilisateurs : {}";
    public static final String LOG_ERREUR_RESPONSABLES_RECUPERATION = "Erreur lors de la récupération des responsables : {}";
    public static final String LOG_ERREUR_UTILISATEUR_EXISTENCE = "Erreur lors de la vérification d'existence de l'utilisateur ID={} : {}";
    public static final String LOG_ERREUR_EMAIL_EXISTENCE = "Erreur lors de la vérification d'existence de l'email={} : {}";
    /** LOG pour le Debug */
    public static final String LOG_TYPE_CONGE_RECUPERER = "Récupérés {} types de congé";

    /** Constatnts pour les exceptions */
    public static final String EXCEPTION_TYPE_CONGE_SUPPRESSION_IMPOSSIBLE = "Impossible de supprimer : type de congé utilisé dans au moins une demande";
    public static final String EXCEPTION_TYPE_CONGE_NON_TROUVE = "Type de congé non trouvé : ";
    public static final String EXCEPTION_TYPE_CONGE_UTILISATION_ECHOUEE = "Échec vérification d'utilisation du type de congé";
    public static final String EXCEPTION_UTILISATEUR_INSERTION_ECHOUEE = "Échec insertion utilisateur";
    public static final String EXCEPTION_UTILISATEUR_RECUPERATION_ECHOUEE = "Échec récupération utilisateur";
    public static final String EXCEPTION_UTILISATEUR_SOLDE_ECHOUE = "Échec mise à jour du solde utilisateur";
    public static final String EXCEPTION_UTILISATEUR_SOLDE_LECTURE_ECHOUEE = "Échec lecture du solde utilisateur";
    public static final String EXCEPTION_UTILISATEURS_RECUPERATION_ECHOUEE = "Échec récupération de la liste des utilisateurs";
    public static final String EXCEPTION_RESPONSABLES_RECUPERATION_ECHOUEE = "Échec récupération de la liste des responsables";
    public static final String EXCEPTION_UTILISATEUR_EXISTENCE_ECHOUEE = "Échec vérification d'existence utilisateur";
    public static final String EXCEPTION_EMAIL_EXISTENCE_ECHOUEE = "Échec vérification d'existence email";

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