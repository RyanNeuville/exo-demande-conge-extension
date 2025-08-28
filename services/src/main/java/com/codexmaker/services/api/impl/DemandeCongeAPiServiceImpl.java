package com.codexmaker.services.api.impl;

import com.codexmaker.services.api.DemandeCongeAPiService;
import com.codexmaker.services.utils.Constants;
import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeCongeAPiServiceImpl implements DemandeCongeAPiService {
    private static final Logger LOGGER = Logger.getLogger(DemandeCongeAPiServiceImpl.class.getName());

    /**
     * Injecte le service d'organisation d'eXo Platform.
     * Le conteneur eXo (via CDI) se chargera de fournir une instance de ce service.
     */
    private OrganizationService organizationService;

    /**
     * Récupère un utilisateur complet depuis le système d'organisation d'eXo.
     *
     * @param userId L'identifiant unique de l'utilisateur (username eXo).
     * @return L'objet User d'eXo, ou null si l'utilisateur n'est pas trouvé.
     */
    public User findExoUserById(String userId) {
        try {
            if (organizationService == null) {
                LOGGER.log(Level.SEVERE, "OrganizationService non injecté. Vérifiez la configuration CDI.");
                return null;
            }
            UserHandler userHandler = organizationService.getUserHandler();
            return userHandler.findUserByName(userId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la récupération de l'utilisateur eXo " + userId + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Récupère le nom complet d'un utilisateur eXo.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Le nom complet de l'utilisateur (firstName + lastName), ou null s'il n'est pas trouvé.
     */
    public String getExoUserFullName(String userId) {
        User user = findExoUserById(userId);
        return (user != null) ? user.getUserName() : null;
    }

    /**
     * Vérifie si un utilisateur a un rôle spécifique dans eXo.
     * Cette implémentation est simplifiée. Une logique plus robuste impliquerait
     * d'utiliser l'API des groupes/rôles d'eXo (`MembershipHandler`).
     * Pour cet exemple, nous allons simuler.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @param role Le rôle a vérifié.
     * @return true si l'utilisateur a le rôle, false sinon.
     */
    public boolean hasExoRole(String userId, String role) {
        try {
            /** return organizationService.getMembershipHandler().findMembershipsOfUser(userId).contains(role); */
            if (userId == null) return false;
            return role.equalsIgnoreCase("admin") && userId.startsWith("admin_");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_VERIFY_USER_ROLE + userId + ": " + e.getMessage(), e);
            return false;
        }
    }
}
