package com.codexmaker.services.service;

import com.codexmaker.services.model.entity.DemandeConge;

import java.util.List;

/**
 * Interface de service pour la gestion des demandes de congé.
 * Définit le contrat des opérations métier disponibles.
 */
public interface DemandeCongeService {

    /**
     * Soumet une nouvelle demande de congé.
     * @param demande La demande à soumettre.
     * @param currentUserId L'identifiant de l'utilisateur eXo qui soumet la demande.
     * @return La demande soumise avec son ID généré.
     * @throws IllegalArgumentException si les données sont invalides ou le solde insuffisant.
     */
    DemandeConge soumettreDemande(DemandeConge demande, String currentUserId);

    /**
     * Approuve une demande de congé.
     * @param demandeId L'identifiant de la demande à approuver.
     * @param commentaires Les commentaires du manager.
     * @throws IllegalArgumentException si la demande est introuvable ou le statut est incorrect.
     * @throws SecurityException si le manager n'est pas autorisé.
     */
    void approuverDemande(int demandeId,  String commentaires);

    /**
     * Refuse une demande de congé.
     * @param demandeId L'identifiant de la demande à refuser.
     * @param commentaires Les commentaires du manager.
     * @throws IllegalArgumentException si la demande est introuvable ou le statut est incorrect.
     * @throws SecurityException si le manager n'est pas autorisé.
     */
    void refuserDemande(int demandeId, String commentaires);

    /**
     * Annule une demande de congé.
     * @param demandeId L'identifiant de la demande à annuler.
     * @param userId L'identifiant de l'utilisateur qui annule sa demande.
     * @throws IllegalArgumentException si la demande est introuvable ou le statut est incorrect.
     * @throws SecurityException si l'utilisateur n'est pas autorisé.
     */
    void annulerDemande(int demandeId, String userId);

    /**
     * Récupère toutes les demandes de congé pour un utilisateur donné.
     * @param userId L'identifiant de l'utilisateur.
     * @return Une liste de demandes de congé.
     */
    List<DemandeConge> getDemandesUtilisateur(String userId);

    /**
     * Récupère une demande de congé par son ID.
     * @param demandeId L'ID de la demande.
     * @return La demande de congé, ou null si introuvable.
     */
    DemandeConge getDemandeById(int demandeId);

    /**
     * Récupère toutes les demandes en attente d'approbation (pour les ADMIN).
     * @param approverId L'ID de l'approbateur pour vérification des droits.
     * @return Une liste de demandes en attente.
     * @throws SecurityException si l'approbateur n'est pas autorisé.
     */
    List<DemandeConge> getDemandesEnAttente(String approverId);

    /**
     * Récupère toutes les demandes de congé, quel que soit l'utilisateur ou le statut (pour les Admin).
     * @param adminId L'ID de l'admin pour vérification des droits.
     * @return Une liste de toutes les demandes de congé.
     * @throws SecurityException si l'utilisateur n'est pas autorisé.
     */
    List<DemandeConge> getAllDemandes(String adminId);
}