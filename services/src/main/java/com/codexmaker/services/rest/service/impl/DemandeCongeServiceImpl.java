package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.DemandeConge;
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
        return null;
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
    }

    @Override
    public void refuserDemande(String demandeId, String commentaire) {
    }

    @Override
    public void annulerDemande(String demandeId) {
    }
}
