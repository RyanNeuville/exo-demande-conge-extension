package com.codexmaker.services.rest.repository;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface du repository pour l'entité DemandeConge.
 * Toutes les opérations CRUD + spécifiques métier.
 */
public interface DemandeCongeRepository {

    /**
     * Crée ou persiste une nouvelle demande.
     * Retourne l'entité avec l'ID généré.
     */
    DemandeConge save(DemandeConge demande);

    /**
     * Met à jour une demande existante.
     */
    void update(DemandeConge demande);

    /**
     * Récupère une demande par son ID.
     */
    DemandeConge findById(String id);

    /**
     * Récupère toutes les demandes d'un utilisateur donné.
     */
    List<DemandeConge> findByUserId(String userId);

    /**
     * Récupère toutes les demandes du système.
     */
    List<DemandeConge> findAll();

    /**
     * Récupère les demandes en attente pour un valideur donné.
     */
    List<DemandeConge> findPendingForValidator(String validatorId);

    /**
     * Met à jour uniquement le statut + commentaire + dates de
     * validation/modification.
     */
    void updateStatus(String id, StatutDemande statut, String commentaire, LocalDate dateModif, LocalDate dateValid);

    /**
     * Vérifie s'il y a chevauchement de dates pour un utilisateur (exclut la
     * demande actuelle si modification).
     * Retourne true si au moins une demande chevauche.
     */
    boolean hasChevauchement(String userId, String excludeDemandeId, LocalDate debut, LocalDate fin);

    /**
     * Supprime une demande (réservé admin).
     */
    void deleteById(String id);
}