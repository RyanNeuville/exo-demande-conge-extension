package com.codexmaker.services.rest.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import java.time.LocalDateTime;

/**
 * Enregistre un changement d'état sur une demande pour assurer la traçabilité.
 * Permet de savoir qui a fait quoi et quand sur une demande précise.
 */
public class HistoriqueEtat {
    private String id;

    @JsonBackReference
    private DemandeConge demande;

    private StatutDemande statutAvant;
    private StatutDemande statutAPres;
    private LocalDateTime dateChangement;
    private String utilisateurChange;
    private String commentaire;

    public HistoriqueEtat() {
    }

    /** Getters & Setters */
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

    public LocalDateTime getDateChangement() {
        return dateChangement;
    }

    public void setDateChangement(LocalDateTime dateChangement) {
        this.dateChangement = dateChangement;
    }

    public String getUtilisateurChange() {
        return utilisateurChange;
    }

    public void setUtilisateurChange(String utilisateurChange) {
        this.utilisateurChange = utilisateurChange;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }
}
