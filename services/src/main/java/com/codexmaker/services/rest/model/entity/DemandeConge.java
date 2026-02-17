package com.codexmaker.services.rest.model.entity;

import com.codexmaker.services.rest.model.enums.StatutDemande;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DemandeConge {
    private String id;
    private String numero;
    private String userId;
    private LocalDate dateCreation;
    private LocalDate dateSoumission;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private boolean demiJourneeDebut;
    private boolean demiJourneeFin;
    private TypeConge typeConge;
    private StatutDemande statut;
    private String motif;
    private String commentaireEmploye;
    private String commentaireValideur;
    private String valideurId;
    private LocalDate dateValidation;
    private LocalDate dateModification;
    private int soldeCongesAvant;
    private int dureeJoursOuvres;
    private List<HistoriqueEtat> historique = new ArrayList<>();

    public DemandeConge() {
    }

    public DemandeConge(String id, String numero, String userId, LocalDate dateCreation, LocalDate dateSoumission, LocalDate dateDebut, LocalDate dateFin, boolean demiJourneeDebut, boolean demiJourneeFin, TypeConge typeConge, StatutDemande statut, String motif, String commentaireEmploye, String commentaireValideur, String valideurId, LocalDate dateValidation, LocalDate dateModification, int soldeCongesAvant, int dureeJoursOuvres, List<HistoriqueEtat> historique) {
        this.id = id;
        this.numero = numero;
        this.userId = userId;
        this.dateCreation = dateCreation;
        this.dateSoumission = dateSoumission;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.demiJourneeDebut = demiJourneeDebut;
        this.demiJourneeFin = demiJourneeFin;
        this.typeConge = typeConge;
        this.statut = statut;
        this.motif = motif;
        this.commentaireEmploye = commentaireEmploye;
        this.commentaireValideur = commentaireValideur;
        this.valideurId = valideurId;
        this.dateValidation = dateValidation;
        this.dateModification = dateModification;
        this.soldeCongesAvant = soldeCongesAvant;
        this.dureeJoursOuvres = dureeJoursOuvres;
        this.historique = historique;
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

    public LocalDate getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }

    public LocalDate getDateSoumission() {
        return dateSoumission;
    }

    public void setDateSoumission(LocalDate dateSoumission) {
        this.dateSoumission = dateSoumission;
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

    public boolean isDemiJourneeDebut() {
        return demiJourneeDebut;
    }

    public void setDemiJourneeDebut(boolean demiJourneeDebut) {
        this.demiJourneeDebut = demiJourneeDebut;
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

    public LocalDate getDateValidation() {
        return dateValidation;
    }

    public void setDateValidation(LocalDate dateValidation) {
        this.dateValidation = dateValidation;
    }

    public LocalDate getDateModification() {
        return dateModification;
    }

    public void setDateModification(LocalDate dateModification) {
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

    public List<HistoriqueEtat> getHistorique() {
        return historique;
    }

    public void setHistorique(List<HistoriqueEtat> historique) {
        this.historique = historique;
    }

    public int calculerDureeJoursOuvres(){
        return 0;
    }

    public boolean estModifiable(){
        return statut == StatutDemande.EN_ATTENTE;
    }

    public boolean estAnnulable(){
        return statut == StatutDemande.EN_ATTENTE;
    }
}
