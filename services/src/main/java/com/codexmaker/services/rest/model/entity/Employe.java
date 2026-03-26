package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;
import com.codexmaker.services.rest.model.enums.StatutDemande;

/**
 * Représente un collaborateur de l'entreprise pouvant soumettre des demandes de
 * congés.
 */
public class Employe extends Utilisateur {

    /**
     * Initialise une nouvelle demande à l'état BROUILLON pour cet employé.
     * 
     * @return Une instance de DemandeConge pré-remplie.
     */
    public DemandeConge creerBrouillonDemande() {
        DemandeConge demande = new DemandeConge();
        demande.setUserId(this.getId());
        demande.setStatut(StatutDemande.BROUILLON);
        return demande;
    }

    /**
     * Délègue la soumission de la demande au service métier.
     * 
     * @param demande La demande à soumettre.
     */
    public void soumettreDemande(DemandeConge demande) {
        ExoContainerContext.getService(DemandeCongeService.class).soumettreDemande(demande, this.getId());
    }

    /**
     * Permet la modification d'une demande tant qu'elle n'est pas traitée.
     * 
     * @param demande L'entité avec les nouvelles valeurs.
     */
    public void modifierDemandeEnAttente(DemandeConge demande) {
        ExoContainerContext.getService(DemandeCongeService.class).modifierDemandeEnAttente(demande, this.getId());
    }

    /**
     * Annule une demande de congé existante.
     * 
     * @param demandeId ID de la demande.
     */
    public void annulerDemandeEnAttente(String demandeId) {
        ExoContainerContext.getService(DemandeCongeService.class).annulerDemande(demandeId);
    }
}
