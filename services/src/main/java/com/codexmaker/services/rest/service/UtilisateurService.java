package com.codexmaker.services.rest.service;

import com.codexmaker.services.rest.model.entity.Utilisateur;
import java.util.List;

/**
 * Interface définissant la couche de service métier pour la gestion des
 * utilisateurs.
 */
public interface UtilisateurService {
    Utilisateur synchroniserUtilisateur(Utilisateur utilisateur);

    int consulterSolde(String userId);

    List<Utilisateur> getTousLesResponsables();

    Utilisateur getUtilisateur(String id);
}
