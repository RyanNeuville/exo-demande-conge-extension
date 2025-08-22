package com.codexmaker.services.model.entity;

/**
 * Classe représentant un utilisateur dans le système de gestion des congés.
 * Contient les informations de base d'un utilisateur, telles que son ID, nom, prénom, email,
 * rôle et solde de congés restants.
 */

import com.codexmaker.services.model.enums.Role;
import java.util.Objects;

public class Utilisateur {
    /** Identifiant unique de l'utilisateur eXo */
    private String id;
    private String nom;
    private String prenom;
    private String email;
    private Role role;
    private int soldeDemande;

    public Utilisateur() {}

    public Utilisateur(String id, String nom, String prenom, String email, Role role, int soldeDemande) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.role = role;
        this.soldeDemande = soldeDemande;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
    public int getSoldeDemande() { return soldeDemande; }
    public void setSoldeDemande(int soldeDemande) { this.soldeDemande = soldeDemande; }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id='" + id + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", soldeDemande=" + soldeDemande +
                '}';
    }

    /** La méthode equals est basée uniquement sur l'ID pour garantir la cohérence avec hashCode.*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        return Objects.equals(id, that.id);
    }

    /** Le hashCode est basé uniquement sur l'ID pour garantir la cohérence avec equals.*/
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}