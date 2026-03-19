package com.codexmaker.services.rest.service;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import java.time.LocalDate;
import java.util.List;

/**
 * Interface définissant la couche de service métier pour les demandes de
 * congés.
 * Orchestre les règles métier et les appels aux repositories.
 */
public interface DemandeCongeService {

    DemandeConge soumettreDemande(DemandeConge demande, String userId);

    DemandeConge getDemande(String demandeId);

    List<DemandeConge> getDemandesParUtilisateur(String userId);

    List<DemandeConge> getToutesLesDemandes();

    List<DemandeConge> getDemandesATraiter(String valideurId);

    void validerDemande(String demandeId, String commentaire);

    void refuserDemande(String demandeId, String commentaire);

    void modifierDemandeEnAttente(DemandeConge demande, String userId);

    void annulerDemande(String demandeId);

    void supprimerDemande(String demandeId);

    String exporterRapports(String format, LocalDate debut, LocalDate fin);
}
