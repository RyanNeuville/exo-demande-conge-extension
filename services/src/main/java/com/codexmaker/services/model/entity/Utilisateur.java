package com.codexmaker.services.model.entity;

/**
 * Classe abstraite représentant un utilisateur du système.
 * Elle est étendue par les classes Admin et Employee.
 */

import com.codexmaker.services.model.enums.Role;

public class Utilisateur {

    private int id;

    private String userId;

    private String nom;

    private String prenom;

    private String email;

    private Role role;

    private int soldeDemande;

    public Utilisateur() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getSoldeDemande() {
        return soldeDemande;
    }

    public void setSoldeDemande(int soldeDemande) {
        this.soldeDemande = soldeDemande;
    }
}