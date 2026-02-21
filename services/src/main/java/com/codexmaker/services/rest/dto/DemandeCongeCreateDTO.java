package com.codexmaker.services.rest.dto;

import java.time.LocalDate;

import com.codexmaker.services.rest.model.entity.TypeConge;

public class DemandeCongeCreateDTO {
    private String userId;
    private LocalDate dateDebut;
    private boolean demiJourneeDebut;
    private LocalDate dateFin;
    private boolean demiJourneeFin;
    private TypeConge typeConge;
    private String motif;
    private String commentaireEmploye;
    private int dureeJoursOuvres;

    public DemandeCongeCreateDTO() {
    }

    public DemandeCongeCreateDTO(String userId, LocalDate dateDebut, boolean demiJourneeDebut, LocalDate dateFin, boolean demiJourneeFin, TypeConge typeConge, String motif, String commentaireEmploye, int dureeJoursOuvres) {
        this.userId = userId;
        this.dateDebut = dateDebut;
        this.demiJourneeDebut = demiJourneeDebut;
        this.dateFin = dateFin;
        this.demiJourneeFin = demiJourneeFin;
        this.typeConge = typeConge;
        this.motif = motif;
        this.commentaireEmploye = commentaireEmploye;
        this.dureeJoursOuvres = dureeJoursOuvres;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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
