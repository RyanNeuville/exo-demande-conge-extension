package com.codexmaker.services.rest.repository;

import java.util.List;

import com.codexmaker.services.rest.model.entity.Utilisateur;

public interface UtilisateurRepository {
    /**
     * Crée ou persiste un utilisateur (souvent synchronisé depuis eXo).
     */
    Utilisateur save(Utilisateur utilisateur);

    /**
     * Récupère un utilisateur par son ID (username eXo).
     */
    Utilisateur findById(String id);

    /**
     * Met à jour le solde de congés d'un utilisateur.
     */
    void updateSolde(String userId, int newSolde);

    /**
     * Récupère le solde actuel d'un utilisateur.
     */
    int getSoldeById(String userId);

    /**
     * Récupère tous les utilisateurs (pour admin).
     */
    List<Utilisateur> findAll();

    /**
     * Récupère tous les responsables (pour assignation valideur).
     */
    List<Utilisateur> findAllResponsables();

    /**
     * Vérifie si un utilisateur existe.
     */
    boolean existsById(String id);

    /**
     * Vérifie si un email est déjà utilisé.
     */
    boolean existsByEmail(String email);
}
