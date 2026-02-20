package com.codexmaker.services.rest.repository;

import com.codexmaker.services.rest.model.entity.TypeConge;
import java.util.List;

/**
 * Interface du repository pour l'entité TypeConge.
 * Opérations CRUD complètes pour la gestion administrative.
 */
public interface TypeCongeRepository {
    /**
     * Crée un nouveau type de congé.
     * Retourne l'entité avec l'ID généré.
     */
    TypeConge save(TypeConge typeConge);

    /**
     * Met à jour un type de congé existant.
     */
    void update(TypeConge typeConge);

    /**
     * Récupère un type de congé par son ID.
     */
    TypeConge findById(String id);

    /**
     * Récupère tous les types de congé (pour listes déroulantes et admin).
     */
    List<TypeConge> findAll();

    /**
     * Supprime un type de congé (seulement si non utilisé dans des demandes).
     */
    void deleteById(String id);

    /**
     * Vérifie si un type de congé est utilisé dans au moins une demande.
     * Utile avant suppression.
     */
    boolean isTypeUsed(String typeCongeId);
}
