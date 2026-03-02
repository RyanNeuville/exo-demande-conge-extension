package com.codexmaker.services.rest.repository;

import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import java.util.List;

/**
 * Interface pour la gestion de l'historique des changements de statut.
 */
public interface HistoriqueEtatRepository {

    /**
     * Enregistre une nouvelle entrée d'historique.
     */
    void save(HistoriqueEtat historique);

    /**
     * Récupère l'historique complet pour une demande de congé.
     */
    List<HistoriqueEtat> findByDemandeId(String demandeId);
}
