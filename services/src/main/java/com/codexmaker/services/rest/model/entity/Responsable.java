package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;
import java.util.List;

/**
 * Représente un manager ou valideur ayant autorité sur les demandes de son
 * équipe.
 */
public class Responsable extends Utilisateur {

    /**
     * Liste les demandes en attente dont ce responsable est affecté comme valideur.
     * 
     * @return Liste de demandes à traiter.
     */
    public List<DemandeConge> consulterDemandesATraiter() {
        return ExoContainerContext.getService(DemandeCongeService.class).getDemandesATraiter(this.getId());
    }

    /**
     * Valide positivement une demande de congé.
     * 
     * @param demandeId   ID de la demande.
     * @param commentaire Note additionnelle.
     */
    public void validerDemande(String demandeId, String commentaire) {
        ExoContainerContext.getService(DemandeCongeService.class).validerDemande(demandeId, commentaire);
    }

    /**
     * Refuse une demande de congé (le solde sera recrédité).
     * 
     * @param demandeId   ID de la demande.
     * @param commentaire Motif du refus.
     */
    public void refuserDemande(String demandeId, String commentaire) {
        ExoContainerContext.getService(DemandeCongeService.class).refuserDemande(demandeId, commentaire);
    }
}
