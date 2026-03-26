package com.codexmaker.services.rest.model.entity;

import com.codexmaker.services.rest.model.enums.Role;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe de base abstraite représentant un utilisateur du système.
 * Les utilisateurs sont spécialisés par rôle (Employé, Responsable,
 * Administrateur).
 */
public abstract class Utilisateur {
    private String id;
    private String username;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private double soldeConges;

    @com.fasterxml.jackson.annotation.JsonIgnore
    private List<DemandeConge> demandes = new ArrayList<>();

    public Utilisateur() {
    }

    /** Getters & Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public double getSoldeConges() {
        return soldeConges;
    }

    public void setSoldeConges(double soldeConges) {
        this.soldeConges = soldeConges;
    }

    public List<DemandeConge> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeConge> demandes) {
        this.demandes = demandes;
    }
}
