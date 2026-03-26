package com.codexmaker.services.rest.service;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface définissant les règles métier du système de gestion des congés.
 */
public interface DemandeCongeService {

    /**
     * Soumet une nouvelle demande de congé après validation du solde et des dates.
     * 
     * @param demande Les données de la demande.
     * @param userId  L'utilisateur propriétaire.
     * @return La demande enregistrée.
     */
    DemandeConge soumettreDemande(DemandeConge demande, String userId);

    /**
     * Récupère le détail d'une demande par son identifiant.
     * 
     * @param demandeId ID technique.
     * @return L'entité.
     */
    DemandeConge getDemande(String demandeId);

    /**
     * Récupère l'historique personnel d'un employé.
     * 
     * @param userId ID utilisateur.
     * @return Liste de demandes.
     */
    List<DemandeConge> getDemandesParUtilisateur(String userId);

    /**
     * Permet à l'administrateur de voir toutes les demandes.
     * 
     * @return Liste globale.
     */
    List<DemandeConge> getToutesLesDemandes();

    /**
     * Liste les demandes soumises à validation pour un manager.
     * 
     * @param valideurId ID du responsable.
     * @return Liste de demandes en attente.
     */
    List<DemandeConge> getDemandesATraiter(String valideurId);

    /**
     * Approuve une demande et déduit définitivement le solde.
     * 
     * @param demandeId   ID demande.
     * @param commentaire Commentaire du manager.
     */
    void validerDemande(String demandeId, String commentaire);

    /**
     * Rejette une demande et recrédite le solde si nécessaire.
     * 
     * @param demandeId   ID demande.
     * @param commentaire Motif du refus.
     */
    void refuserDemande(String demandeId, String commentaire);

    /**
     * Modifie une demande existante avant traitement.
     * 
     * @param demande Nouvelles données.
     * @param userId  ID utilisateur.
     */
    void modifierDemandeEnAttente(DemandeConge demande, String userId);

    /**
     * Annule une demande (action employé).
     * 
     * @param demandeId ID demande.
     */
    void annulerDemande(String demandeId);

    /**
     * Supprime une demande (action admin).
     * 
     * @param demandeId ID demande.
     */
    void supprimerDemande(String demandeId);

    /**
     * Génère un rapport d'export des congés. (Placeholder pour le moment)
     */
    String exporterRapports(String format, LocalDate debut, LocalDate fin);
}
