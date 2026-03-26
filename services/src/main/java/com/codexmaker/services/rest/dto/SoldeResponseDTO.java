package com.codexmaker.services.rest.dto;

public class SoldeResponseDTO {
    private double solde;

    public SoldeResponseDTO() {}

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
