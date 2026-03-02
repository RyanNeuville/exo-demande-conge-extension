package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;

import java.util.List;

public class Responsable extends Utilisateur {

    /** Methodes metier */
    public List<DemandeConge> consulterDemandesATraiter() {
        return ExoContainerContext.getService(DemandeCongeService.class).getDemandesATraiter(this.getId());
    }

    public void validerDemande(String demandeId, String commentaire) {
        ExoContainerContext.getService(DemandeCongeService.class).validerDemande(demandeId, commentaire);
    }

    public void refuserDemande(String demandeId, String commentaire) {
        ExoContainerContext.getService(DemandeCongeService.class).refuserDemande(demandeId, commentaire);
    }

    public void ajouterCommentaireValidation(String demandeId, String commentaire) {
        // Dans notre service actuel, valider/refuser prend le commentaire.
        // Si on veut juste ajouter un commentaire sans changer le statut :
        // On pourrait ajouter une méthode au service si besoin.
    }

}
