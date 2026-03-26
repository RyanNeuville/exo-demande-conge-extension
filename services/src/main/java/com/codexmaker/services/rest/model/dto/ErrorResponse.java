package com.codexmaker.services.rest.model.dto;

import java.time.LocalDateTime;

/**
 * Objet de transfert de données standardisant les réponses d'erreur de l'API.
 * Utilisé par le GlobalExceptionMapper pour fournir un feedback cohérent au
 * frontend.
 */
public class ErrorResponse {
    private int status;
    private String message;
    private String errorCode;
    private LocalDateTime timestamp;

    public ErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    /**
     * @param status    Code HTTP (ex: 400, 403, 500).
     * @param message   Description humaine de l'erreur.
     * @param errorCode Code technique pour le traitement programmatique (ex:
     *                  INSUFFICIENT_BALANCE).
     */
    public ErrorResponse(int status, String message, String errorCode) {
        this();
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
    }

    /** Getters and Setters */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
