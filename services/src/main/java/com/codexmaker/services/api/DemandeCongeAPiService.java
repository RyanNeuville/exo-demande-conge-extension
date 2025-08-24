package com.codexmaker.services.api;

import org.exoplatform.services.organization.User;

/** Classe pour interagir avec l'API Exo Platform */

public interface DemandeCongeAPiService {
    User findExoUserById(String userId);

    String getExoUserFullName(String userId);

    boolean hasExoRole(String userId, String role);
}
