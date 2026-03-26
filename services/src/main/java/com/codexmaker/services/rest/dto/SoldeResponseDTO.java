package com.codexmaker.services.rest.dto;

/**
 * DTO simple pour retourner uniquement la valeur du solde de congés.
 */
public class SoldeResponseDTO {
    private double solde;

    public SoldeResponseDTO() {
    }

    public SoldeResponseDTO(double solde) {
        this.solde = solde;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
}
