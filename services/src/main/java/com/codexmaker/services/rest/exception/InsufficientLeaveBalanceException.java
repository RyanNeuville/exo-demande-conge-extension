package com.codexmaker.services.rest.exception;

/**
 * Exception levée lorsqu'un employé tente de poser plus de jours
 * que son solde actuel ne le permet.
 */
public class InsufficientLeaveBalanceException extends RuntimeException {
    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }
}
