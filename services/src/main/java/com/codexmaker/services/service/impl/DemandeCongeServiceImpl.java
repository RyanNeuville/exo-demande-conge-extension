package com.codexmaker.services.service.impl;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.entity.DemandeCongeResponse;
import com.codexmaker.services.service.DemandeCongeService;
import com.codexmaker.services.utils.Constants;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class DemandeCongeServiceImpl implements DemandeCongeService {

    private static final Log LOG = ExoLogger.getLogger(DemandeCongeServiceImpl.class);
    // Simulation d'une base de données en mémoire
    private final Map<Integer, DemandeConge> demandes = new HashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(0);

    private static final String FAILED_TITLE = "title";
    private static final String FAILED_RESPONSE_MESSAGE = "message";
    private static final String FAILED_RESPONSE_CODE = "statusCode";
    private static final String FAILED_REASON = "reason";
    private static final String DEMANDE_NOT_FOUND = "Demande introuvable";
    private static final String INSUFFICIENT_BALANCE = "Solde de congés insuffisant";

    @Override
    public DemandeCongeResponse soumettreDemande(DemandeConge demande, String currentUserId) {
        if (demande == null || currentUserId == null || currentUserId.isEmpty()) {
            LOG.warn("Soumission de demande invalide : données manquantes.");
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse("Données invalides", "La demande ou l'ID utilisateur est manquant.", 400))
                    .build();
        }

        // Simuler la vérification du solde de congés
        if (demande.getNombreJours() <= 0) {
            LOG.warn("Soumission de demande invalide : nombre de jours <= 0 pour l'utilisateur {}.", currentUserId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(400)
                    .message(createErrorResponse(INSUFFICIENT_BALANCE, "Le nombre de jours demandés est invalide.", 400))
                    .build();
        }

        // Simuler un solde suffisant pour la démo
        demande.setId(idGenerator.incrementAndGet());
        demande.setUserId(currentUserId);
        demande.setStatut("EN_ATTENTE");
        demande.setDateSoumission(LocalDate.now());

        demandes.put(demande.getId(), demande);
        LOG.info("Nouvelle demande soumise avec l'ID {} par l'utilisateur {}.", demande.getId(), currentUserId);
        return DemandeCongeResponse.builder()
                .response(demande)
                .status(201)
                .message("Demande de congé soumise avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse approuverDemande(int demandeId, String commentaires) {
        DemandeConge demande = demandes.get(demandeId);

        if (demande == null) {
            LOG.warn("Tentative d'approbation d'une demande introuvable avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(404)
                    .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande à approuver est introuvable.", 404))
                    .build();
        }

        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            LOG.warn("Tentative d'approbation d'une demande avec un statut incorrect (ID : {}, Statut : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409) // Conflict
                    .message(createErrorResponse("Statut incorrect", "La demande doit être en attente pour être approuvée.", 409))
                    .build();
        }

        demande.setStatut("APPROUVEE");
        demande.setCommentairesManager(commentaires);
        LOG.info("Demande ID {} approuvée.", demandeId);

        return DemandeCongeResponse.builder()
                .response(demande)
                .status(200)
                .message("Demande de congé approuvée avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse refuserDemande(int demandeId, String commentaires) {
        DemandeConge demande = demandes.get(demandeId);
        if (demande == null) {
            LOG.warn("Tentative de refus d'une demande introuvable avec l'ID {}.", demandeId);
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(404)
                    .message(createErrorResponse(DEMANDE_NOT_FOUND, "La demande à refuser est introuvable.", 404))
                    .build();
        }
        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            LOG.warn("Tentative de refus d'une demande avec un statut incorrect (ID : {}, Statut : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409)
                    .message(createErrorResponse("Statut incorrect", "La demande doit être en attente pour être refusée.", 409))
                    .build();
        }

        demande.setStatut("REFUSEE");
        demande.setCommentairesManager(commentaires);
        LOG.info("Demande ID {} refusée.", demandeId);
        return DemandeCongeResponse.builder()
                .response(demande)
                .status(200)
                .message("Demande de congé refusée avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse annulerDemande(int demandeId, String userId) {
        DemandeConge demande = demandes.get(demandeId);
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
                    .status(403) // Forbidden
                    .message(createErrorResponse("Non autorisé", "Vous n'êtes pas autorisé à annuler cette demande.", 403))
                    .build();
        }
        if (!"EN_ATTENTE".equals(demande.getStatut())) {
            LOG.warn("Tentative d'annulation d'une demande avec un statut incorrect (ID : {}, Statut : {}).",
                    demandeId, demande.getStatut());
            return DemandeCongeResponse.builder()
                    .response(null)
                    .status(409)
                    .message(createErrorResponse("Statut incorrect", "Seules les demandes en attente peuvent être annulées.", 409))
                    .build();
        }

        demande.setStatut("ANNULEE");
        LOG.info("Demande ID {} annulée par l'utilisateur {}.", demandeId, userId);
        return DemandeCongeResponse.builder()
                .response(demande)
                .status(200)
                .message("Demande de congé annulée avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse getDemandesUtilisateur(String userId) {
        if (userId == null || userId.isEmpty()) {
            return DemandeCongeResponse.builder()
                    .response(new ArrayList<>())
                    .status(400)
                    .message(createErrorResponse("Données invalides", "L'ID utilisateur est manquant.", 400))
                    .build();
        }
        List<DemandeConge> userDemandes = demandes.values().stream()
                .filter(d -> d.getUserId().equals(userId))
                .collect(Collectors.toList());
        LOG.info("Récupération de {} demandes pour l'utilisateur {}.", userDemandes.size(), userId);
        return DemandeCongeResponse.builder()
                .response(userDemandes)
                .status(200)
                .message("Demandes de l'utilisateur récupérées avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse getDemandeById(int demandeId) {
        DemandeConge demande = demandes.get(demandeId);
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
    }

    @Override
    public DemandeCongeResponse getDemandesEnAttente(String approverId) {
        // Simuler la vérification de l'autorisation d'approbateur
        if (!isAdmin(approverId)) {
            LOG.warn("Tentative d'accéder aux demandes en attente par un utilisateur non autorisé : {}.", approverId);
            return DemandeCongeResponse.builder()
                    .response(new ArrayList<>())
                    .status(403)
                    .message(createErrorResponse("Non autorisé", "Seuls les administrateurs peuvent consulter les demandes en attente.", 403))
                    .build();
        }
        List<DemandeConge> pendingDemandes = demandes.values().stream()
                .filter(d -> "EN_ATTENTE".equals(d.getStatut()))
                .collect(Collectors.toList());
        LOG.info("Récupération de {} demandes en attente par l'approbateur {}.", pendingDemandes.size(), approverId);
        return DemandeCongeResponse.builder()
                .response(pendingDemandes)
                .status(200)
                .message("Demandes en attente récupérées avec succès.")
                .build();
    }

    @Override
    public DemandeCongeResponse getAllDemandes(String adminId) {
        if (!isAdmin(adminId)) {
            LOG.warn("Tentative d'accéder à toutes les demandes par un utilisateur non autorisé : {}.", adminId);
            return DemandeCongeResponse.builder()
                    .response(new ArrayList<>())
                    .status(403)
                    .message(createErrorResponse("Non autorisé", "Seuls les administrateurs peuvent consulter toutes les demandes.", 403))
                    .build();
        }
        List<DemandeConge> allDemandes = new ArrayList<>(demandes.values());
        LOG.info("Récupération de toutes les {} demandes par l'administrateur {}.", allDemandes.size(), adminId);
        return DemandeCongeResponse.builder()
                .response(allDemandes)
                .status(200)
                .message("Toutes les demandes récupérées avec succès.")
                .build();
    }

    private String createErrorResponse(String title, String message, int statusCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FAILED_TITLE, title);
        jsonObject.put(FAILED_RESPONSE_MESSAGE, message);
        jsonObject.put(FAILED_RESPONSE_CODE, statusCode);
        return jsonObject.toString();
    }

    // Méthode de simulation pour vérifier si un utilisateur est un administrateur
    private boolean isAdmin(String userId) {
        // En production, cette méthode interagirait avec un service d'authentification et d'autorisation
        return "admin".equals(userId);
    }
}