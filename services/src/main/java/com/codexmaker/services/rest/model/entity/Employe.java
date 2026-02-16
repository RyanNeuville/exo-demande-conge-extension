package com.codexmaker.services.rest.model.entity;

public class Employe extends Utilisateur{

    /** Methodes metier */
    public DemandeConge creerBrouillonDemande() {
        return null;
    }

    public void soumettreDemande(DemandeConge demande){
    }

    public void modifierDemandeEnAttente(DemandeConge demande){
    }

    public void annulerDemandeEnAttente(String demandeId){
    }
}
