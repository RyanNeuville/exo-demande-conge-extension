package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;
import com.codexmaker.services.rest.service.TypeCongeService;

import java.util.List;

public class Administrateur extends Utilisateur {

    /** Methodes metier */
    public List<DemandeConge> consulterToutesLesDemandes() {
        return ExoContainerContext.getService(DemandeCongeService.class).getToutesLesDemandes();
    }

    public void supprimerDemande(String demandeId) {
        ExoContainerContext.getService(DemandeCongeService.class).supprimerDemande(demandeId);
    }

    public void gererTypesConges() {
        /**
         * Cette méthode peut être un point d'entrée pour la logique de gestion des
         * types
         * Pour l'instant on expose le service via le contexte si besoin ailleurs
         */
        ExoContainerContext.getService(TypeCongeService.class).getTousLesTypesConges();
    }
}
