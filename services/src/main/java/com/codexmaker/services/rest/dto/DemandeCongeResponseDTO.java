package com.codexmaker.services.rest.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;

public class DemandeCongeResponseDTO {
    private String id;
    private String numero;
    private String userId;
    private String nomEmploye;
    private String prenomEmploye;
    private LocalDate dateDebut;
    private boolean demiJourneeDebut;
    private LocalDate dateFin;
    private boolean demiJourneeFin;
    private TypeConge typeConge;
    private StatutDemande statut;
    private String motif;
    private String commentaireEmploye;
    private String commentaireValideur;
    private String valideurId;
    private String nomValideur;
    private LocalDateTime dateSoumission;
    private LocalDateTime dateValidation;
    private LocalDateTime dateModification;
    private int soldeCongesAvant;
    private int dureeJoursOuvres;

    public DemandeCongeResponseDTO() {
    }

    public DemandeCongeResponseDTO(String id, String numero, String userId, String nomEmploye, String prenomEmploye,
            LocalDate dateDebut, boolean demiJourneeDebut, LocalDate dateFin, boolean demiJourneeFin,
            TypeConge typeConge, StatutDemande statut, String motif, String commentaireEmploye,
            String commentaireValideur, String valideurId, String nomValideur, LocalDateTime dateSoumission,
            LocalDateTime dateValidation, LocalDateTime dateModification, int soldeCongesAvant, int dureeJoursOuvres) {
        this.id = id;
        this.numero = numero;
        this.userId = userId;
        this.nomEmploye = nomEmploye;
        this.prenomEmploye = prenomEmploye;
        this.dateDebut = dateDebut;
        this.demiJourneeDebut = demiJourneeDebut;
        this.dateFin = dateFin;
        this.demiJourneeFin = demiJourneeFin;
        this.typeConge = typeConge;
        this.statut = statut;
        this.motif = motif;
        this.commentaireEmploye = commentaireEmploye;
        this.commentaireValideur = commentaireValideur;
        this.valideurId = valideurId;
        this.nomValideur = nomValideur;
        this.dateSoumission = dateSoumission;
        this.dateValidation = dateValidation;
        this.dateModification = dateModification;
        this.soldeCongesAvant = soldeCongesAvant;
        this.dureeJoursOuvres = dureeJoursOuvres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public void setNomEmploye(String nomEmploye) {
        this.nomEmploye = nomEmploye;
    }

    public String getPrenomEmploye() {
        return prenomEmploye;
    }

    public void setPrenomEmploye(String prenomEmploye) {
        this.prenomEmploye = prenomEmploye;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public boolean isDemiJourneeDebut() {
        return demiJourneeDebut;
    }

    public void setDemiJourneeDebut(boolean demiJourneeDebut) {
        this.demiJourneeDebut = demiJourneeDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public boolean isDemiJourneeFin() {
        return demiJourneeFin;
    }

    public void setDemiJourneeFin(boolean demiJourneeFin) {
        this.demiJourneeFin = demiJourneeFin;
    }

    public TypeConge getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(TypeConge typeConge) {
        this.typeConge = typeConge;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getCommentaireEmploye() {
        return commentaireEmploye;
    }

    public void setCommentaireEmploye(String commentaireEmploye) {
        this.commentaireEmploye = commentaireEmploye;
    }

    public String getCommentaireValideur() {
        return commentaireValideur;
    }

    public void setCommentaireValideur(String commentaireValideur) {
        this.commentaireValideur = commentaireValideur;
    }

    public String getValideurId() {
        return valideurId;
    }

    public void setValideurId(String valideurId) {
        this.valideurId = valideurId;
    }

    public String getNomValideur() {
        return nomValideur;
    }

    public void setNomValideur(String nomValideur) {
        this.nomValideur = nomValideur;
    }

    public LocalDateTime getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDateTime dateSoumission) {
        this.dateSoumission = dateSoumission;
    }

    public LocalDateTime getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDateTime dateValidation) {
        this.dateValidation = dateValidation;
    }

    public LocalDateTime getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDateTime dateModification) {
        this.dateModification = dateModification;
    }

    public int getSoldeCongesAvant() {
        return soldeCongesAvant;
    }

    public void setSoldeCongesAvant(int soldeCongesAvant) {
        this.soldeCongesAvant = soldeCongesAvant;
    }

    public int getDureeJoursOuvres() {
        return dureeJoursOuvres;
    }

    public void setDureeJoursOuvres(int dureeJoursOuvres) {
        this.dureeJoursOuvres = dureeJoursOuvres;
    }

}
