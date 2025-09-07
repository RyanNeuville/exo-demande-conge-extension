package com.codexmaker.services.service.impl;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.service.dao.DemandeCongeDAO;

import java.util.List;

public class Main {
    private static final DemandeCongeDAO demandeCongeDAO = new DemandeCongeDAO();

    public static void main(String[] args) {
        System.out.println("Liste des demandes de conge :");
        List<DemandeConge>  demandeConges = demandeCongeDAO.findAll();
        for(DemandeConge demande : demandeConges){
            System.out.println("No: "+demande.toString());
        }


        System.out.println("Liste des demandes de conge par userId :");
        List<DemandeConge> demandeCongeUser = demandeCongeDAO.findByUserId("1");
        for (DemandeConge demande : demandeCongeUser){
            System.out.println("No: "+demande.toString());
        }


        System.out.println("Liste des demandes de conge par filtre :");
        List<DemandeConge> demandeCongeFiltre = demandeCongeDAO.findByStatus(Statut.EN_ATTENTE);
        for (DemandeConge demande : demandeCongeFiltre){
            System.out.println("No: "+demande.toString());
        }
    }
}
