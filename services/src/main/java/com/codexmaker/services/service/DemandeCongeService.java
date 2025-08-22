package com.codexmaker.services.service;

import com.codexmaker.services.model.dto.DemandeCongeDTO;
import com.codexmaker.services.model.entity.DemandeConge;

import java.time.LocalDate;
import java.util.List;

/**
* interface pour le service de gestion des demandes de congé.
* Fournit des méthodes pour créer, modifier, supprimer et consulter les demandes de congé.
* */

public interface DemandeCongeService {

    /**
     * Cette methode Soumet une nouvelle demande de congé pour un utilisateur donné.
     * @return Le DTO de la demande soumise avec son identifiant et son statut.
     */
    DemandeCongeDTO soumettreDemande(DemandeCongeDTO demandeDto, String userId);

    /**
     * Cette methode Récupère toutes les demandes de congé d'un utilisateur spécifique.
     * @return Une liste de DTO de demandes de congé pour l'utilisateur spécifié.
     */
    List<DemandeCongeDTO> getDemandesUtilisateur(String userId);

    /**
     * Cette methode Récupère toutes les demandes de congé en attente d'approbation pour un manager ou admin spécifique.
     * @return Une liste de DTO de demandes de congé en attente pour le manager spécifié.
     */
    List<DemandeCongeDTO> getDemandesEnAttenteManager(String managerId);

    /**
     * Cette methode Modifie une demande de congé existante.
     * @return Le DTO de la demande mise à jour.
     */
    DemandeCongeDTO annulerDemande(Long demandeId, String userId);


    /**
     * Cette methode Récupère toutes les demandes de congé.
     * @return Une liste de DTO de toutes les demandes de congé.
     */
    List<DemandeCongeDTO> getAllDemandes();

    /**
     * Cette methode Approuve une demande de congé.
     *
     * @return Le DTO de la demande mise à jour.
     */
    DemandeConge approuverDemande(Long demandeId, String managerId);

    /**
     * Cette methode Refuse une demande de congé.
     * @return Le DTO de la demande mise à jour.
     */
    DemandeConge refuserDemande(Long demandeId, String managerId);

    /**
     * Cette methode Calcule la durée en jours ouvrés entre deux dates.
     * @param dateDebut La date de début du congé.
     * @param dateFin La date de fin du congé.
     * @return Le nombre de jours ouvrés entre les deux dates.
     */
    int calculerDureeJoursOuvres(LocalDate dateDebut, LocalDate dateFin);
}
