package com.codexmaker.services.rest.repository;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import java.time.LocalDate;
import java.util.List;

/**
 * Contrat pour la gestion de la persistance des demandes de congés.
 */
public interface DemandeCongeRepository {

    /**
     * Sauvegarde une demande en base.
     * 
     * @param demande L'entité à créer.
     * @return L'entité sauvegardée.
     */
    DemandeConge save(DemandeConge demande);

    /**
     * Met à jour les infos d'une demande.
     * 
     * @param demande L'entité modifiée.
     */
    void update(DemandeConge demande);

    /**
     * Recherche par ID.
     * 
     * @param id UUID.
     * @return La demande.
     */
    DemandeConge findById(String id);

    /**
     * Liste les demandes d'un employé.
     * 
     * @param userId ID utilisateur.
     * @return Liste de demandes.
     */
    List<DemandeConge> findByUserId(String userId);

    /**
     * Retourne toutes les demandes.
     * 
     * @return Liste complète.
     */
    List<DemandeConge> findAll();

    /**
     * Liste les demandes en attente pour un responsable donné.
     * 
     * @param validatorId ID du valideur.
     * @return Liste filtrée.
     */
    List<DemandeConge> findPendingForValidator(String validatorId);

    /**
     * Met à jour le statut et les métadonnées de décision.
     * 
     * @param id          ID demande.
     * @param statut      Nouveau statut.
     * @param commentaire Motif.
     * @param dateModif   Date modification.
     * @param dateValid   Date validation.
     */
    void updateStatus(String id, StatutDemande statut, String commentaire, LocalDate dateModif, LocalDate dateValid);

    /**
     * Vérifie la validité temporelle d'une demande.
     * 
     * @param userId           ID utilisateur.
     * @param excludeDemandeId ID à ignorer (pour update).
     * @param debut            Date début.
     * @param fin              Date fin.
     * @return true si conflit.
     */
    boolean hasChevauchement(String userId, String excludeDemandeId, LocalDate debut, LocalDate fin);

    /**
     * Suppression définitive d'une demande.
     * 
     * @param id ID demande.
     */
    void deleteById(String id);
}