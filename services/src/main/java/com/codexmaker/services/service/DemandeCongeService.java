package com.codexmaker.services.service;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.entity.DemandeCongeResponse;

public interface DemandeCongeService {

    DemandeCongeResponse soumettreDemande(DemandeConge demande, String currentUserId);

    DemandeCongeResponse approuverDemande(int demandeId, String commentaires);

    DemandeCongeResponse refuserDemande(int demandeId, String commentaires);

    DemandeCongeResponse annulerDemande(int demandeId, String userId);

    DemandeCongeResponse getDemandesUtilisateur(String userId);

    DemandeCongeResponse getDemandeById(int demandeId);

    DemandeCongeResponse getDemandesEnAttente(String approverId);

    DemandeCongeResponse getAllDemandes(String adminId);
}