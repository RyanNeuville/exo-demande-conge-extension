package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;

public class Employe extends Utilisateur {

    /** Methodes metier */
    public DemandeConge creerBrouillonDemande() {
        DemandeConge demande = new DemandeConge();
        demande.setUserId(this.getId());
        demande.setStatut(com.codexmaker.services.rest.model.enums.StatutDemande.BROUILLON);
        return demande;
    }

    public void soumettreDemande(DemandeConge demande) {
        ExoContainerContext.getService(DemandeCongeService.class).soumettreDemande(demande, this.getId());
    }

    public void modifierDemandeEnAttente(DemandeConge demande) {
        ExoContainerContext.getService(DemandeCongeService.class).modifierDemandeEnAttente(demande, this.getId());
    }

    public void annulerDemandeEnAttente(String demandeId) {
        ExoContainerContext.getService(DemandeCongeService.class).annulerDemande(demandeId);
    }
}
