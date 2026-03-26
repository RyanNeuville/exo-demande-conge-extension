package com.codexmaker.services.rest.model.entity;

import org.exoplatform.container.ExoContainerContext;
import com.codexmaker.services.rest.service.DemandeCongeService;
import com.codexmaker.services.rest.service.TypeCongeService;
import java.util.List;

/**
 * Rôle privilégié permettant la supervision globale et le paramétrage des types
 * de congés.
 */
public class Administrateur extends Utilisateur {

    /**
     * Accède à l'historique complet de toutes les demandes de l'entreprise.
     * 
     * @return Liste exhaustive des demandes.
     */
    public List<DemandeConge> consulterToutesLesDemandes() {
        return ExoContainerContext.getService(DemandeCongeService.class).getToutesLesDemandes();
    }

    /**
     * Supprime physiquement une demande (opération administrative de nettoyage).
     * 
     * @param demandeId ID de la demande.
     */
    public void supprimerDemande(String demandeId) {
        ExoContainerContext.getService(DemandeCongeService.class).supprimerDemande(demandeId);
    }

    /**
     * Point d'accès pour lister les types de congés paramétrés.
     */
    public void gererTypesConges() {
        ExoContainerContext.getService(TypeCongeService.class).getTousLesTypesConges();
    }
}
