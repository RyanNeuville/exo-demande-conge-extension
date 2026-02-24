package com.codexmaker.services.rest.model.entity;

import java.util.List;

public class Responsable extends Utilisateur {

    /** Methodes metier */
    public List<DemandeConge> consulterDemandesATraiter(){
        return null;
    }

    public void validerDemande(String demandeId, String commentaire){
    }

    public void refuserDemande(String demandeId, String commentaire){
    }

    public void ajouterCommentaireValidation(String demandeId, String commentaire){
    }

}
