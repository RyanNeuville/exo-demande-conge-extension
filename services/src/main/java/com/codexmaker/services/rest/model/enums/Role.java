package com.codexmaker.services.rest.model.enums;

/**
 * Définit les droits et les accès des différents profils utilisateurs.
 */
public enum Role {
    /** Utilisateur standard pouvant poser des congés. */
    EMPLOYE,
    /** Manager pouvant valider les demandes de ses subordonnés. */
    RESPONSABLE,
    /** Gestionnaire système avec accès complet. */
    ADMINISTRATEUR
}