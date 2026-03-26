package com.codexmaker.services.rest.model.entity;

import com.codexmaker.services.rest.model.enums.StatutDemande;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité principale représentant une demande de congé.
 * Contient toutes les informations relatives à la période, au type, au statut
 * et à la traçabilité de la demande.
 */
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
    private double soldeCongesAvant;
    private double dureeJoursOuvres;

    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<HistoriqueEtat> historique = new ArrayList<>();

    public DemandeConge() {
    }

    /**
     * Calcule la durée effective de la demande en jours ouvrés.
     * Exclut les samedis et dimanches et déduit les demi-journées si cochées.
     * Met à jour l'attribut interne 'dureeJoursOuvres'.
     * 
     * @return La durée calculée (ex: 2.5 pour 3 jours ouvrés moins une matinée).
     */
    public double calculerDureeJoursOuvres() {
        if (dateDebut == null || dateFin == null)
            return 0.0;
        if (dateDebut.isAfter(dateFin))
            return 0.0;

        double count = 0.0;
        LocalDate current = dateDebut;
        while (!current.isAfter(dateFin)) {
            java.time.DayOfWeek day = current.getDayOfWeek();
            if (day != java.time.DayOfWeek.SATURDAY && day != java.time.DayOfWeek.SUNDAY) {
                count += 1.0;
            }
            current = current.plusDays(1);
        }

        if (count > 0) {
            if (demiJourneeDebut)
                count -= 0.5;
            if (demiJourneeFin)
                count -= 0.5;
        }

        this.dureeJoursOuvres = Math.max(0, count);
        return this.dureeJoursOuvres;
    }

    /**
     * Vérifie si la demande est encore éditable.
     * Une demande n'est modifiable que si elle est en attente de validation.
     * 
     * @return true si modifiable.
     */
    public boolean estModifiable() {
        return statut == StatutDemande.EN_ATTENTE;
    }

    /**
     * Vérifie si l'utilisateur peut annuler sa demande.
     * 
     * @return true si annulable.
     */
    public boolean estAnnulable() {
        return statut == StatutDemande.EN_ATTENTE;
    }

    /** Getters and Setters */
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

    public double getSoldeCongesAvant() {
        return soldeCongesAvant;
    }

    public void setSoldeCongesAvant(double soldeCongesAvant) {
        this.soldeCongesAvant = soldeCongesAvant;
    }

    public double getDureeJoursOuvres() {
        return dureeJoursOuvres;
    }

    public void setDureeJoursOuvres(double dureeJoursOuvres) {
        this.dureeJoursOuvres = dureeJoursOuvres;
    }

    public List<HistoriqueEtat> getHistorique() {
        return historique;
    }

    public void setHistorique(List<HistoriqueEtat> historique) {
        this.historique = historique;
    }
}
