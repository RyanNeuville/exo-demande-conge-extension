package com.codexmaker.services.model.entity;

import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.TypeConge;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entité représentant une demande de congé.
 * Cette classe est mappée à la table "demande_conge" dans la base de données.
 * Elle contient des informations sur la demande de congé, y compris l'utilisateur,
 * les dates, le type de congé, le statut, et d'autres détails pertinents.
 */

public class DemandeConge {

    private int id;
    private String demandeId;
    private String userId;
    private String nom;
    private String prenom;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private TypeConge typeConge;
    private Statut statut;
    private String motif;
    private String commentairesManager;
    private LocalDate dateSoumission;
    private LocalDate dateModification;
    private int soldeDemande;
    private int dureeJoursOuvres;

    public DemandeConge() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDemandeId() { return demandeId; }
    public void setDemandeId(String demandeId) { this.demandeId = demandeId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public TypeConge getTypeConge() { return typeConge; }
    public void setTypeConge(TypeConge typeConge) { this.typeConge = typeConge; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getCommentairesManager() { return commentairesManager; }
    public void setCommentairesManager(String commentairesManager) { this.commentairesManager = commentairesManager; }
    public LocalDate getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDate dateSoumission) { this.dateSoumission = dateSoumission; }
    public LocalDate getDateModification() { return dateModification; }
    public void setDateModification(LocalDate dateModification) { this.dateModification = dateModification; }
    public int getSoldeDemande() { return soldeDemande; }
    public void setSoldeDemande(int soldeDemande) { this.soldeDemande = soldeDemande; }
    public int getDureeJoursOuvres() { return dureeJoursOuvres; }
    public void setDureeJoursOuvres(int dureeJoursOuvres) { this.dureeJoursOuvres = dureeJoursOuvres; }

    @Override
    public String toString() {
        return "DemandeConge{" +
                "id=" + id +
                ", demandeId='" + demandeId + '\'' +
                ", userId='" + userId + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", typeConge=" + typeConge +
                ", statut=" + statut +
                ", motif='" + motif + '\'' +
                ", commentairesManager='" + commentairesManager + '\'' +
                ", dateSoumission=" + dateSoumission +
                ", dateModification=" + dateModification +
                ", soldeDemande=" + soldeDemande +
                ", dureeJoursOuvres=" + dureeJoursOuvres +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandeConge that = (DemandeConge) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}