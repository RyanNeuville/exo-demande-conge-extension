package com.codexmaker.services.rest.model.enums;

/**
 * États possibles d'une demande de congé dans son cycle de vie.
 */
public enum StatutDemande {
    /** La demande est enregistrée mais pas encore envoyée au manager. */
    BROUILLON,
    /** La demande est soumise et attend l'approbation du manager. */
    EN_ATTENTE,
    /** La demande a été acceptée. */
    VALIDEE,
    /** La demande a été rejetée par le manager. */
    REFUSEE,
    /** L'utilisateur a retiré sa demande. */
    ANNULEE
}
