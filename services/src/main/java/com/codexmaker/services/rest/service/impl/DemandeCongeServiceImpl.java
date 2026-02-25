package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.repository.DemandeCongeRepository;
import com.codexmaker.services.rest.repository.impl.DemandeCongeRepositoryImpl;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import com.codexmaker.services.rest.repository.impl.UtilisateurRepositoryImpl;
import com.codexmaker.services.rest.service.DemandeCongeService;

import java.util.List;

/**
 * Implémentation du service métier pour les demandes de congés.
 */
public class DemandeCongeServiceImpl implements DemandeCongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UtilisateurRepository utilisateurRepository;

    public DemandeCongeServiceImpl() {
        this.demandeCongeRepository = new DemandeCongeRepositoryImpl();
        this.utilisateurRepository = new UtilisateurRepositoryImpl();
    }

    @Override
    public DemandeConge soumettreDemande(DemandeConge demande, String userId) {
        // 1. Vérification de chevauchement
        if (demandeCongeRepository.hasChevauchement(userId, null, demande.getDateDebut(), demande.getDateFin())) {
            throw new RuntimeException("Une demande existe déjà pour cette période.");
        }

        // 2. Initialisation des champs
        demande.setUserId(userId);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateSoumission(java.time.LocalDate.now());

        // 3. Sauvegarde
        return demandeCongeRepository.save(demande);
    }

    @Override
    public List<DemandeConge> getDemandesParUtilisateur(String userId) {
        return demandeCongeRepository.findByUserId(userId);
    }

    @Override
    public List<DemandeConge> getToutesLesDemandes() {
        return demandeCongeRepository.findAll();
    }

    @Override
    public List<DemandeConge> getDemandesATraiter(String valideurId) {
        return demandeCongeRepository.findPendingForValidator(valideurId);
    }

    @Override
    public void validerDemande(String demandeId, String commentaire) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null && demande.getStatut() == StatutDemande.EN_ATTENTE) {
            // Mettre à jour le solde de l'utilisateur
            int actuel = utilisateurRepository.getSoldeById(demande.getUserId());
            utilisateurRepository.updateSolde(demande.getUserId(), actuel - demande.getDureeJoursOuvres());

            // Mettre à jour la demande
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.VALIDEE, commentaire,
                    java.time.LocalDate.now(), java.time.LocalDate.now());
        }
    }

    @Override
    public void refuserDemande(String demandeId, String commentaire) {
        demandeCongeRepository.updateStatus(demandeId, StatutDemande.REFUSEE, commentaire,
                java.time.LocalDate.now(), java.time.LocalDate.now());
    }

    @Override
    public void modifierDemandeEnAttente(DemandeConge demande, String userId) {
        DemandeConge existante = demandeCongeRepository.findById(demande.getId());
        if (existante == null || existante.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new RuntimeException("Seulement les demandes en attente peuvent être modifiées.");
        }

        if (demandeCongeRepository.hasChevauchement(userId, demande.getId(), demande.getDateDebut(),
                demande.getDateFin())) {
            throw new RuntimeException("Modification impossible : chevauchement de dates.");
        }

        demande.setDateModification(java.time.LocalDate.now());
        demandeCongeRepository.update(demande);
    }

    @Override
    public void annulerDemande(String demandeId) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null
                && (demande.getStatut() == StatutDemande.EN_ATTENTE || demande.getStatut() == StatutDemande.VALIDEE)) {
            // Si elle était déjà validée, on recrédite le solde
            if (demande.getStatut() == StatutDemande.VALIDEE) {
                int actuel = utilisateurRepository.getSoldeById(demande.getUserId());
                utilisateurRepository.updateSolde(demande.getUserId(), actuel + demande.getDureeJoursOuvres());
            }
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.ANNULEE, "Annulée par l'utilisateur",
                    java.time.LocalDate.now(), null);
        }
    }

    @Override
    public void supprimerDemande(String demandeId) {
        demandeCongeRepository.deleteById(demandeId);
    }
}
