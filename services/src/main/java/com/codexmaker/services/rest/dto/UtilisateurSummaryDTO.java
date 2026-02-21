package com.codexmaker.services.rest.dto;

import com.codexmaker.services.rest.model.enums.Role;

public class UtilisateurSummaryDTO {
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private int soldeCongesRestants;

    public UtilisateurSummaryDTO() {
    }

    public UtilisateurSummaryDTO(String id, String nom, String prenom, String email, Role role, int soldeCongesRestants) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.soldeCongesRestants = soldeCongesRestants;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getSoldeCongesRestants() {
        return soldeCongesRestants;
    }

    public void setSoldeCongesRestants(int soldeCongesRestants) {
        this.soldeCongesRestants = soldeCongesRestants;
    }

}
