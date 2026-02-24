package com.codexmaker.services.rest.service;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import java.util.List;

/**
 * Interface définissant la couche de service métier pour les demandes de
 * congés.
 * Orchestre les règles métier et les appels aux repositories.
 */
public interface DemandeCongeService {

    DemandeConge soumettreDemande(DemandeConge demande, String userId);

    List<DemandeConge> getDemandesParUtilisateur(String userId);

    List<DemandeConge> getToutesLesDemandes();

    List<DemandeConge> getDemandesATraiter(String valideurId);

    void validerDemande(String demandeId, String commentaire);

    void refuserDemande(String demandeId, String commentaire);

    void annulerDemande(String demandeId);
}
