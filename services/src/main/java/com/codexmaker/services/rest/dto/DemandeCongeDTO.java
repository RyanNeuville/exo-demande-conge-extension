package com.codexmaker.services.rest.dto;

public class DemandeCongeDTO {
    private String id;
    private String userId;
    private String typeConge;
    private String dateDebut;
    private String dateFin;
    private boolean demiJourneeDebut;
    private boolean demiJourneeFin;
    private String motif;
    private String commentaireEmploye;
    private int dureeJoursOuvres;

    public DemandeCongeDTO() {
    }

    /** Getters et Setters */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
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

    public int getDureeJoursOuvres() {
        return dureeJoursOuvres;
    }

    public void setDureeJoursOuvres(int dureeJoursOuvres) {
        this.dureeJoursOuvres = dureeJoursOuvres;
    }
}
