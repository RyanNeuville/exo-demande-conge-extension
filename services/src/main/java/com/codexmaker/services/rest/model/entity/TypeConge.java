package com.codexmaker.services.rest.model.entity;

/**
 * Représente une catégorie de congé (ex: Congé Payé, RTT, Maladie).
 * Définit les règles de calcul (plafond annuel, déduction du solde).
 */
public class TypeConge {
    private String id;
    private String code;
    private String libelle;
    private String description;
    private int joursMaxParAn;
    private boolean deductionSolde;

    public TypeConge() {
    }

    /** Getters & Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJoursMaxParAn() {
        return joursMaxParAn;
    }

    public void setJoursMaxParAn(int joursMaxParAn) {
        this.joursMaxParAn = joursMaxParAn;
    }

    public boolean isDeductionSolde() {
        return deductionSolde;
    }

    public void setDeductionSolde(boolean deductionSolde) {
        this.deductionSolde = deductionSolde;
    }
}
