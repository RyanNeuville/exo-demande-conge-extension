package com.codexmaker.services.rest.exception;

/**
 * Exception de base pour toutes les erreurs liées aux règles métier
 * de la gestion des congés.
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
