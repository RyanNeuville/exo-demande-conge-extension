package com.codexmaker.services.model.dto;

import com.codexmaker.services.model.enums.TypeConge;
import com.codexmaker.services.model.enums.Statut;

import java.time.LocalDate;

/**
 * DemandeCongeDTO est un Data Transfer Object (DTO) utilisé pour transférer
 * les informations d'une demande de congé entre les couches de l'application
 * (ex: entre le frontend et le contrôleur, ou le contrôleur et le service).
 **/

public class DemandeCongeDTO {

    public DemandeCongeDTO() {
    }

    public DemandeCongeDTO(int id, String userId, String nom, String prenom, LocalDate dateDebut, LocalDate dateFin, TypeConge typeConge, String motif, Statut statut, String commentaire, LocalDate dateSoumission, LocalDate dateModification, int soldeDemande, int dureeJoursOuvres) {
        this.id = id;
        this.userId = userId;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeConge = typeConge;
        this.motif = motif;
        this.statut = statut;
        this.commentaire = commentaire;
        this.dateSoumission = dateSoumission;
        this.dateModification = dateModification;
        this.soldeDemande = soldeDemande;
        this.dureeJoursOuvres = dureeJoursOuvres;
    }

    private int id;

    private String userId;

    private String nom;

    private String prenom;

    private LocalDate dateDebut;

    private  LocalDate dateFin;

    private TypeConge typeConge;

    private Statut statut;

    private String motif;

    private String commentaire;

    private LocalDate dateSoumission;

    private LocalDate dateModification;

    private int soldeDemande;

    private int dureeJoursOuvres;

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

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public TypeConge getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public LocalDate getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
        this.dateModification = dateModification;
    }

    public int getSoldeDemande() {
        return soldeDemande;
    }

    public void setSoldeDemande(int soldeDemande) {
        this.soldeDemande = soldeDemande;
    }

    public int getDureeJoursOuvres() {
        return dureeJoursOuvres;
    }

    public void setDureeJoursOuvres(int dureeJoursOuvres) {
        this.dureeJoursOuvres = dureeJoursOuvres;
    }
}
