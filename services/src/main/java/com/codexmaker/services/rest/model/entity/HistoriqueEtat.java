package com.codexmaker.services.rest.model.entity;

import com.codexmaker.services.rest.model.enums.StatutDemande;

import java.time.LocalDate;

public class HistoriqueEtat {
    private String id;
    private DemandeConge demande;
    private StatutDemande statutAvant;
    private StatutDemande statutAPres;
    private LocalDate dateChangement;
    private String utilisateursCange;
    private String commentaire;

    public HistoriqueEtat(){
    }

    public HistoriqueEtat(String id, DemandeConge demande, StatutDemande statutAvant, StatutDemande statutAPres, LocalDate dateChangement, String utilisateursCange, String commentaire) {
        this.id = id;
        this.demande = demande;
        this.statutAvant = statutAvant;
        this.statutAPres = statutAPres;
        this.dateChangement = dateChangement;
        this.utilisateursCange = utilisateursCange;
        this.commentaire = commentaire;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DemandeConge getDemande() {
        return demande;
    }

    public void setDemande(DemandeConge demande) {
        this.demande = demande;
    }

    public StatutDemande getStatutAvant() {
        return statutAvant;
    }

    public void setStatutAvant(StatutDemande statutAvant) {
        this.statutAvant = statutAvant;
    }

    public StatutDemande getStatutAPres() {
        return statutAPres;
    }

    public void setStatutAPres(StatutDemande statutAPres) {
        this.statutAPres = statutAPres;
    }

    public LocalDate getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDate dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getUtilisateursCange() {
        return utilisateursCange;
    }

    public void setUtilisateursCange(String utilisateursCange) {
        this.utilisateursCange = utilisateursCange;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
