package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import com.codexmaker.services.rest.repository.impl.UtilisateurRepositoryImpl;
import com.codexmaker.services.rest.service.UtilisateurService;

import java.util.List;

/**
 * Implémentation du service métier pour les utilisateurs.
 */
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurServiceImpl() {
        this.utilisateurRepository = new UtilisateurRepositoryImpl();
    }

    @Override
    public Utilisateur synchroniserUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsById(utilisateur.getId())) {
            // Dans un cas réel, on pourrait plutôt mettre à jour certains champs (nom,
            // email)
            return utilisateurRepository.findById(utilisateur.getId());
        }
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public int consulterSolde(String userId) {
        return utilisateurRepository.getSoldeById(userId);
    }

    @Override
    public List<Utilisateur> getTousLesResponsables() {
        return utilisateurRepository.findAllResponsables();
    }

    @Override
    public Utilisateur getUtilisateur(String id) {
        return utilisateurRepository.findById(id);
    }
}
