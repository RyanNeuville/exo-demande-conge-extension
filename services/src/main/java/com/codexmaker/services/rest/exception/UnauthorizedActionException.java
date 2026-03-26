package com.codexmaker.services.rest.exception;

/**
 * Exception levée lorsqu'un utilisateur tente d'effectuer une action
 * non autorisée par son rôle ou ses droits (ex: valider sa propre demande).
 */
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
