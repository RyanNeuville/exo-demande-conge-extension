package com.codexmaker.services.model.entity;

/**
 * Classe représentant une demande de congé.
 * Elle contient les informations relatives à la demande de congé d'un utilisateur.
 * Cette classe inclut les dates de début et de fin du congé, le statut de la demande,
 * le motif de la demande, ainsi que les relations avec l'utilisateur et le type de congé associés.
 * La classe permet également de calculer la durée du congé et de modifier le statut de la demande.
 */

import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.TypeConge;

import java.time.LocalDate;

public class DemandeConge {

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

    private Utilisateur utilisateur;

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Statut getStatut() {
        return statut;
    }

    public void setStatut(Statut statut) {
        this.statut = statut;
    }

    public TypeConge getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
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