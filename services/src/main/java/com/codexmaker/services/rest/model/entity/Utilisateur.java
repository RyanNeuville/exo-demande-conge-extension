package com.codexmaker.services.rest.model.entity;

import com.codexmaker.services.rest.model.enums.Role;

import java.util.ArrayList;
import java.util.List;

public abstract class Utilisateur {
    /** attribut commun a tous les utilisateurs */
    private String id;
    private String username;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private int soldeConges;

    /** Liste des demandes soumises par cet utilisateur */
    private List<DemandeConge> demandes = new ArrayList<>();

    /** Constructeur */
    public Utilisateur() {
    }
    public Utilisateur(String id, String username, String nom, String prenom, String email, Role role, int soldeConges, List<DemandeConge> demandes) {
        this.id = id;
        this.username = username;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.soldeConges = soldeConges;
        this.demandes = demandes;
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

    public List<DemandeConge> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeConge> demandes) {
        this.demandes = demandes;
    }

    public int getSoldeConges() {
        return soldeConges;
    }

    public void setSoldeConges(int soldeConges) {
        this.soldeConges = soldeConges;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
