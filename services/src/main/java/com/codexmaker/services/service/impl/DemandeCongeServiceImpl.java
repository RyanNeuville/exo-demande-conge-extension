package com.codexmaker.services.service.impl;

import com.codexmaker.services.service.dao.DemandeCongeDAO;
import com.codexmaker.services.service.dao.UtilisateurDAO;
import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.entity.DemandeCongeResponse;
import com.codexmaker.services.model.entity.Utilisateur;
import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.Role;
import com.codexmaker.services.service.DemandeCongeService;
import com.codexmaker.services.api.impl.DemandeCongeAPiServiceImpl;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.organization.User;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Implémentation du service de gestion des demandes de congé.
 * Cette classe contient la logique métier et orchestre les opérations entre les DAOs
 * pour la persistance et ExoUserService pour l'intégration avec eXo Platform.
 */
public class DemandeCongeServiceImpl implements DemandeCongeService {

    private static final Log LOG = ExoLogger.getLogger(DemandeCongeServiceImpl.class);

    /** Injection des DAOs et des services externes via CDI */
    private final DemandeCongeDAO demandeCongeDAO = new DemandeCongeDAO();

    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    private final DemandeCongeAPiServiceImpl exoUserService = new DemandeCongeAPiServiceImpl();

    /** Constantes pour les messages d'erreur JSON (similaires à l'exemple du professeur) */
    private static final String FAILED_TITLE = "title";
    private static final String FAILED_RESPONSE_MESSAGE = "message";
    private static final String FAILED_RESPONSE_CODE = "statusCode";
    private static final String DEMANDE_NOT_FOUND = "Demande introuvable";
    private static final String INSUFFICIENT_BALANCE = "Solde de congés insuffisant";
    private static final String UNAUTHORIZED = "Non autorisé";

    public DemandeCongeServiceImpl() throws SQLException {
    }

    /**
     * @PostConstruct ou une méthode d'initialisation peut être ajoutée ici si nécessaire
     * pour configurer les DAOs ou initialiser la base de données de test,
     * mais l'instruction demande "pas d'initialisation machin truc".
     * Donc, on suppose que les tables existent et que les DAOs sont prêts.
     */

    @Override
    public DemandeCongeResponse soumettreDemande(DemandeConge demande, String currentUserId) {
        /** Validation basique des données d'entrée */
        if (demande == null || currentUserId == null || currentUserId.isEmpty()) {
            LOG.warn("Soumission de demande invalide : données manquantes.");
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Données invalides", "La demande ou l'ID utilisateur est manquant.", 400))
                    .build();
        }
        if (demande.getDateDebut() == null || demande.getDateFin() == null || demande.getTypeConge() == null) {
            LOG.warn("Soumission de demande invalide pour {}: champs de date ou type de congé manquants.", currentUserId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Données invalides", "Les dates de début/fin ou le type de congé sont manquants.", 400))
                    .build();
        }
        if (demande.getDateDebut().isAfter(demande.getDateFin())) {
            LOG.warn("Soumission de demande invalide pour {}: date de début après date de fin.", currentUserId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Dates invalides", "La date de début ne peut pas être après la date de fin.", 400))
                    .build();
        }
        if (demande.getDateDebut().isBefore(LocalDate.now())) {
            LOG.warn("Soumission de demande invalide pour {}: date de début dans le passé.", currentUserId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Dates invalides", "La date de début ne peut pas être dans le passé.", 400))
                    .build();
        }

        /** 1. Récupérer ou créer l'utilisateur dans notre base de données locale */
        Utilisateur utilisateur = utilisateurDAO.findById(currentUserId);
        if (utilisateur == null) {
            /** L'utilisateur n'existe pas dans notre base, on le récupère d'eXo et on le persiste */
            User exoUser = exoUserService.findExoUserById(currentUserId);
            if (exoUser != null) {
                /** Créer un nouvel utilisateur dans notre base avec des valeurs par défaut */
                utilisateur = new Utilisateur(currentUserId, exoUser.getFirstName(), exoUser.getLastName(), exoUser.getEmail(), Role.EMPLOYE, 25); // 25 jours par défaut
                utilisateurDAO.save(utilisateur);
                LOG.info("Utilisateur eXo '{}' créé dans la base locale.", currentUserId);
            } else {
                LOG.warn("Utilisateur eXo introuvable '{}'. Ne peut pas soumettre de demande.", currentUserId);
                return DemandeCongeResponse.builder()
                        .response(null)
                        .status(404)
                        .message(createErrorResponse("Utilisateur introuvable", "L'utilisateur eXo n'a pas pu être trouvé ou créé.", 404))
                        .build();
            }
        }

        /** 2. Calculer la durée du congé */
        int dureeDemandeJoursOuvres = calculerJoursOuvres(demande.getDateDebut(), demande.getDateFin());
        if (dureeDemandeJoursOuvres <= 0) {
            LOG.warn("Soumission de demande invalide pour {}: durée du congé <= 0 jours ouvrés.", currentUserId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Durée invalide", "La demande doit couvrir au moins un jour ouvré.", 400))
                    .build();
        }

        /** 3. Vérifier le solde de congés */
        if (utilisateur.getSoldeDemande() < dureeDemandeJoursOuvres) {
            LOG.warn("Soumission de demande refusée pour {}: solde insuffisant ({} < {}).", currentUserId, utilisateur.getSoldeDemande(), dureeDemandeJoursOuvres);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse(INSUFFICIENT_BALANCE, "Solde de congés insuffisant pour cet utilisateur.", 400))
                    .build();
        }

        /** 4. Préparer l'entité DemandeConge et la persister */
        demande.setUserId(currentUserId);
        demande.setNom(utilisateur.getNom());
        demande.setPrenom(utilisateur.getPrenom());
        demande.setStatut(Statut.EN_ATTENTE);
        demande.setDateSoumission(LocalDate.now());
        /** Date de modification initiale */
        demande.setDateModification(LocalDate.now());
        demande.setDureeJoursOuvres(dureeDemandeJoursOuvres);
        /** Pas de commentaires manager à la soumission */
        demande.setCommentairesManager(null);
        /** Stocke le solde de l'utilisateur au moment de la demande (pour historique) */
        demande.setSoldeDemande(utilisateur.getSoldeDemande());

        try {
            DemandeConge savedDemande = demandeCongeDAO.save(demande);
            LOG.info("Nouvelle demande soumise avec l'ID {} par l'utilisateur {}.", savedDemande.getId(), currentUserId);
            return DemandeCongeResponse.builder()
                    .response(savedDemande)
                    .status(201)
                    .message("Demande de congé soumise avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de la persistance de la demande pour l'utilisateur {}: {}", currentUserId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de sauvegarder la demande.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse approuverDemande(int demandeId, String commentaires) {
        DemandeConge demande = demandeCongeDAO.findById(demandeId);
        if (demande == null) {
            LOG.warn("Tentative d'approbation d'une demande introuvable avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(404)
                    .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande à approuver est introuvable.", 404))
                    .build();
        }

        if (!Statut.EN_ATTENTE.equals(demande.getStatut())) {
            LOG.warn("Tentative d'approbation d'une demande avec un statut incorrect (ID : {}, Statut actuel : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409)
                    .message(createErrorResponse("Statut incorrect", "La demande doit être en attente pour être approuvée.", 409))
                    .build();
        }

        /** 1. Mettre à jour le statut de la demande et les commentaires */
        try {
            demandeCongeDAO.updateStatut((long) demandeId, Statut.APPROUVEE, commentaires, LocalDate.now());
            LOG.info("Demande ID {} approuvée.", demandeId);

            /** 2. Mettre à jour le solde de l'utilisateur */
            Utilisateur utilisateur = utilisateurDAO.findById(demande.getUserId());
            if (utilisateur == null) {
                LOG.error("Utilisateur (ID: {}) introuvable pour la demande approuvée (ID: {}). Solde non mis à jour.", demande.getUserId(), demandeId);
            } else {
                utilisateur.setSoldeDemande(utilisateur.getSoldeDemande() - demande.getDureeJoursOuvres());
                utilisateurDAO.updateSolde(utilisateur.getId(), utilisateur.getSoldeDemande());
                LOG.info("Solde de l'utilisateur {} mis à jour à {}.", utilisateur.getId(), utilisateur.getSoldeDemande());
            }

            /** Récupérer la demande mise à jour pour la réponse */
            DemandeConge updatedDemande = demandeCongeDAO.findById(demandeId);
            return DemandeCongeResponse.builder()
                    .response(updatedDemande)
                    .status(200)
                    .message("Demande de congé approuvée avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de l'approbation de la demande ID {}: {}", demandeId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible d'approuver la demande.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse refuserDemande(int demandeId, String commentaires) {
        DemandeConge demande = demandeCongeDAO.findById(demandeId);
        if (demande == null) {
            LOG.warn("Tentative de refus d'une demande introuvable avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(404)
                    .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande à refuser est introuvable.", 404))
                    .build();
        }
        if (!Statut.EN_ATTENTE.equals(demande.getStatut())) {
            LOG.warn("Tentative de refus d'une demande avec un statut incorrect (ID : {}, Statut actuel : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409)
                    .message(createErrorResponse("Statut incorrect", "La demande doit être en attente pour être refusée.", 409))
                    .build();
        }

        try {
            demandeCongeDAO.updateStatut((long) demandeId, Statut.REFUSEE, commentaires, LocalDate.now());
            LOG.info("Demande ID {} refusée.", demandeId);

            DemandeConge updatedDemande = demandeCongeDAO.findById(demandeId);
            return DemandeCongeResponse.builder()
                    .response(updatedDemande)
                    .status(200)
                    .message("Demande de congé refusée avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors du refus de la demande ID {}: {}", demandeId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de refuser la demande.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse annulerDemande(int demandeId, String userId) {
        DemandeConge demande = demandeCongeDAO.findById(demandeId);
        if (demande == null) {
            LOG.warn("Tentative d'annulation d'une demande introuvable avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(404)
                    .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande à annuler est introuvable.", 404))
                    .build();
        }
        if (!demande.getUserId().equals(userId)) {
            LOG.warn("Tentative d'annulation d'une demande par un utilisateur non autorisé (ID demande : {}, UserID : {}).",
                    demandeId, userId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(403)
                    .message(createErrorResponse(UNAUTHORIZED, "Vous n'êtes pas autorisé à annuler cette demande.", 403))
                    .build();
        }
        if (!Statut.EN_ATTENTE.equals(demande.getStatut())) {
            LOG.warn("Tentative d'annulation d'une demande avec un statut incorrect (ID : {}, Statut actuel : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409)
                    .message(createErrorResponse("Statut incorrect", "Seules les demandes en attente peuvent être annulées.", 409))
                    .build();
        }

        try {
            demandeCongeDAO.updateStatut((long) demandeId, Statut.ANNULEE, "Annulée par l'employé.", LocalDate.now());
            LOG.info("Demande ID {} annulée par l'utilisateur {}.", demandeId, userId);

            DemandeConge updatedDemande = demandeCongeDAO.findById(demandeId);
            return DemandeCongeResponse.builder()
                    .response(updatedDemande)
                    .status(200)
                    .message("Demande de congé annulée avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de l'annulation de la demande ID {}: {}", demandeId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible d'annuler la demande.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse getDemandesUtilisateur(String userId) {
        if (userId == null || userId.isEmpty()) {
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Données invalides", "L'ID utilisateur est manquant.", 400))
                    .build();
        }
        try {
            List<DemandeConge> userDemandes = demandeCongeDAO.findByUserId(userId);
            LOG.info("Récupération de {} demandes pour l'utilisateur {}.", userDemandes.size(), userId);
            return DemandeCongeResponse.builder()
                    .response(userDemandes)
                    .status(200)
                    .message("Demandes de l'utilisateur récupérées avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des demandes pour l'utilisateur {}: {}", userId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de récupérer les demandes de l'utilisateur.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse getDemandeById(int demandeId) {
        try {
            DemandeConge demande = demandeCongeDAO.findById(demandeId);
            if (demande == null) {
                LOG.warn("Tentative de récupération d'une demande introuvable avec l'ID {}.", demandeId);
                return DemandeCongeResponse.builder()
                        .response(null)
                        .status(404)
                        .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande est introuvable.", 404))
                        .build();
            }
            LOG.info("Récupération de la demande avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(demande)
                    .status(200)
                    .message("Demande récupérée avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération de la demande ID {}: {}", demandeId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de récupérer la demande.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse getDemandesEnAttente(String approverId) {
        /** Vérification des permissions de l'approbateur (Admin) */
        /** if (exoUserService.hasExoRole(approverId, Constants.ROLE_ADMIN)) {
            LOG.warn("Tentative d'accéder aux demandes en attente par un utilisateur non autorisé : {}.", approverId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(403)
                    .message(createErrorResponse(UNAUTHORIZED, "Vous n'êtes pas autorisé à consulter les demandes en attente.", 403))
                    .build();
        } */

        try {
            List<DemandeConge> pendingDemandes = demandeCongeDAO.findByStatus(Statut.EN_ATTENTE);
            LOG.info("Récupération de {} demandes en attente par l'approbateur {}.", pendingDemandes.size(), approverId);
            return DemandeCongeResponse.builder()
                    .response(pendingDemandes)
                    .status(200)
                    .message("Demandes en attente récupérées avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération des demandes en attente pour {}: {}", approverId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de récupérer les demandes en attente.", 500))
                    .build();
        }
    }

    @Override
    public DemandeCongeResponse getAllDemandes(String adminId) {
        /** Vérification des permissions (Admin) */
        /** if (exoUserService.hasExoRole(adminId, Constants.ROLE_ADMIN)) {
            LOG.warn("Tentative d'accéder à toutes les demandes par un utilisateur non autorisé : {}.", adminId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(403)
                    .message(createErrorResponse(UNAUTHORIZED, "Seuls les administrateurs ou RH peuvent consulter toutes les demandes.", 403))
                    .build();
        }
         */

        try {
            List<DemandeConge> allDemandes = demandeCongeDAO.findAll();
            LOG.info("Récupération de toutes les {} demandes par l'administrateur {}.", allDemandes.size(), adminId);
            return DemandeCongeResponse.builder()
                    .response(allDemandes)
                    .status(200)
                    .message("Toutes les demandes récupérées avec succès.")
                    .build();
        } catch (Exception e) {
            LOG.error("Erreur lors de la récupération de toutes les demandes pour {}: {}", adminId, e.getMessage(), e);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(500)
                    .message(createErrorResponse("Erreur interne du serveur", "Impossible de récupérer toutes les demandes.", 500))
                    .build();
        }
    }

    /**
     * Calcule le nombre de jours ouvrés entre deux dates (inclusif).
     * Les week-ends (samedi et dimanche) sont exclus.
     * @param debut Date de début.
     * @param fin Date de fin.
     * @return Nombre de jours ouvrés.
     */
    private int calculerJoursOuvres(LocalDate debut, LocalDate fin) {
        if (debut == null || fin == null || debut.isAfter(fin)) {
            return 0;
        }
        int joursOuvres = 0;
        LocalDate dateCourante = debut;
        while (!dateCourante.isAfter(fin)) {
            if (dateCourante.getDayOfWeek() != DayOfWeek.SATURDAY && dateCourante.getDayOfWeek() != DayOfWeek.SUNDAY) {
                joursOuvres++;
            }
            dateCourante = dateCourante.plusDays(1);
        }
        return joursOuvres;
    }

    /**
     * Crée une réponse d'erreur formatée en JSON.
     * @param title Titre de l'erreur.
     * @param message Message détaillé de l'erreur.
     * @param statusCode Code HTTP de l'erreur.
     * @return Une chaîne JSON représentant l'erreur.
     */
    private String createErrorResponse(String title, String message, int statusCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FAILED_TITLE, title);
        jsonObject.put(FAILED_RESPONSE_MESSAGE, message);
        jsonObject.put(FAILED_RESPONSE_CODE, statusCode);
        return jsonObject.toString();
    }
}
