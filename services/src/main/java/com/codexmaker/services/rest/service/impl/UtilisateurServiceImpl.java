package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import com.codexmaker.services.rest.repository.impl.UtilisateurRepositoryImpl;
import com.codexmaker.services.rest.service.UtilisateurService;

import java.util.List;

/**
 * Implémentation du service métier pour les utilisateurs.
 * Gère la synchronisation des profils et la consultation des soldes de congés.
 */
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;

    /**
     * Constructeur par défaut initialisant le dépôt par défaut.
     */
    public UtilisateurServiceImpl() {
        this(new UtilisateurRepositoryImpl());
    }

    /**
     * Constructeur permettant l'injection de dépendances pour les tests.
     *
     * @param utilisateurRepository le dépôt des utilisateurs
     */
    public UtilisateurServiceImpl(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    /**
     * Synchronise les informations d'un utilisateur avec la base de données locale.
     * Si l'utilisateur existe déjà, ses informations actuelles sont retournées.
     * Sinon, un nouvel utilisateur est créé.
     *
     * @param utilisateur Les informations de l'utilisateur à synchroniser.
     * @return L'utilisateur synchronisé.
     */
    @Override
    public Utilisateur synchroniserUtilisateur(Utilisateur utilisateur) {
        if (utilisateurRepository.existsById(utilisateur.getId())) {
            /**
             * Note : Dans une évolution future, on pourra mettre à jour les champs
             * changeants comme le nom ou l'email ici.
             */
            return utilisateurRepository.findById(utilisateur.getId());
        }
        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Récupère le solde de congés actuel d'un utilisateur.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Le solde de congés restant (en jours).
     */
    @Override
    public double consulterSolde(String userId) {
        return utilisateurRepository.getSoldeById(userId);
    }

    /**
     * Récupère la liste de tous les utilisateurs ayant le rôle de responsable.
     *
     * @return Liste d'utilisateurs responsables.
     */
    @Override
    public List<Utilisateur> getTousLesResponsables() {
        return utilisateurRepository.findAllResponsables();
    }

    /**
     * Récupère les détails d'un utilisateur par son identifiant.
     *
     * @param id L'identifiant UUID de l'utilisateur.
     * @return L'entité utilisateur ou null si non trouvée.
     */
    @Override
    public Utilisateur getUtilisateur(String id) {
        return utilisateurRepository.findById(id);
    }
}
