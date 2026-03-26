package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.exception.BusinessException;
import com.codexmaker.services.rest.exception.InsufficientLeaveBalanceException;
import com.codexmaker.services.rest.exception.UnauthorizedActionException;
import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.repository.DemandeCongeRepository;
import com.codexmaker.services.rest.repository.HistoriqueEtatRepository;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import com.codexmaker.services.rest.repository.impl.DemandeCongeRepositoryImpl;
import com.codexmaker.services.rest.repository.impl.HistoriqueEtatRepositoryImpl;
import com.codexmaker.services.rest.repository.impl.UtilisateurRepositoryImpl;
import com.codexmaker.services.rest.service.DemandeCongeService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service métier pour les demandes de congés.
 */
public class DemandeCongeServiceImpl implements DemandeCongeService {

    private final DemandeCongeRepository demandeCongeRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final HistoriqueEtatRepository historiqueEtatRepository;

    /**
     * Constructeur par défaut initialisant les dépôts par défaut.
     */
    public DemandeCongeServiceImpl() {
        this(new DemandeCongeRepositoryImpl(), new UtilisateurRepositoryImpl(), new HistoriqueEtatRepositoryImpl());
    }

    /**
     * Constructeur permettant l'injection de dépendances pour les tests.
     *
     * @param demandeCongeRepository   le dépôt des demandes
     * @param utilisateurRepository    le dépôt des utilisateurs
     * @param historiqueEtatRepository le dépôt des historiques
     */
    public DemandeCongeServiceImpl(DemandeCongeRepository demandeCongeRepository,
                                   UtilisateurRepository utilisateurRepository,
                                   HistoriqueEtatRepository historiqueEtatRepository) {
        this.demandeCongeRepository = demandeCongeRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.historiqueEtatRepository = historiqueEtatRepository;
    }

    @Override
    public DemandeConge soumettreDemande(DemandeConge demande, String userId) {
        /** 1. Vérification de la cohérence des dates */
        if (demande.getDateDebut() == null || demande.getDateFin() == null) {
            throw new BusinessException("Les dates de début et de fin sont obligatoires.");
        }
        if (demande.getDateDebut().isAfter(demande.getDateFin())) {
            throw new BusinessException("La date de début ne peut pas être après la date de fin.");
        }

        /** 2. Vérification de chevauchement */
        if (demandeCongeRepository.hasChevauchement(userId, null, demande.getDateDebut(), demande.getDateFin())) {
            throw new BusinessException("Une demande existe déjà pour cette période.");
        }

        /** 2. Vérification / Création automatique de l'utilisateur (JIT) */
        if (!utilisateurRepository.existsById(userId)) {
            com.codexmaker.services.rest.model.entity.Employe nouvelUtilisateur = new com.codexmaker.services.rest.model.entity.Employe();
            nouvelUtilisateur.setId(userId);
            nouvelUtilisateur.setUsername(userId);
            nouvelUtilisateur.setNom("Employé");
            nouvelUtilisateur.setPrenom(userId);
            nouvelUtilisateur.setEmail(userId + "@kozao.ko");
            nouvelUtilisateur.setRole(com.codexmaker.services.rest.model.enums.Role.EMPLOYE);
            /** Solde par défaut d'une demande chaque année */
            nouvelUtilisateur.setSoldeConges(25.0);
            utilisateurRepository.save(nouvelUtilisateur);
        }

        /** 3. Calculer la durée et vérifier le solde */
        demande.calculerDureeJoursOuvres();
        double soldeActuel = utilisateurRepository.getSoldeById(userId);
        if (soldeActuel < demande.getDureeJoursOuvres()) {
            throw new InsufficientLeaveBalanceException("Solde insuffisant pour cette demande.");
        }

        /** 3. Initialisation des champs */
        if (demande.getId() == null)
            demande.setId(UUID.randomUUID().toString());
        if (demande.getNumero() == null)
            demande.setNumero("REQ-" + System.currentTimeMillis());
        if (demande.getValideurId() == null)
            demande.setValideurId("root");
        demande.setUserId(userId);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDateCreation(LocalDate.now());
        demande.setDateSoumission(LocalDate.now());
        demande.setSoldeCongesAvant(soldeActuel);

        /** 4. Réserver le solde immédiatement (Logique de soumission) */
        utilisateurRepository.updateSolde(userId, soldeActuel - demande.getDureeJoursOuvres());

        /** 5. Sauvegarde de la demande */
        DemandeConge saved = demandeCongeRepository.save(demande);

        /** 6. Historisation */
        logHistorique(saved, null, StatutDemande.EN_ATTENTE, userId, "Soumission initiale");

        return saved;
    }

    @Override
    public DemandeConge getDemande(String demandeId) {
        return demandeCongeRepository.findById(demandeId);
    }

    @Override
    public List<DemandeConge> getDemandesParUtilisateur(String userId) {
        List<DemandeConge> demandes = demandeCongeRepository.findByUserId(userId);
        for (DemandeConge d : demandes) {
            d.setHistorique(historiqueEtatRepository.findByDemandeId(d.getId()));
        }
        return demandes;
    }

    @Override
    public List<DemandeConge> getToutesLesDemandes() {
        return demandeCongeRepository.findAll();
    }

    @Override
    public List<DemandeConge> getDemandesATraiter(String valideurId) {
        return demandeCongeRepository.findPendingForValidator(valideurId);
    }

    @Override
    public void validerDemande(String demandeId, String commentaire) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null && demande.getStatut() == StatutDemande.EN_ATTENTE) {
            /** Le solde est déjà déduit à la soumission. */
            /** On confirme juste la validation. */
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.VALIDEE, commentaire,
                    LocalDate.now(), LocalDate.now());

            logHistorique(demande, StatutDemande.EN_ATTENTE, StatutDemande.VALIDEE, "SYSTEM", commentaire);
        }
    }

    @Override
    public void refuserDemande(String demandeId, String commentaire) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null && demande.getStatut() == StatutDemande.EN_ATTENTE) {
            /** Recréditer le solde car la demande est refusée */
            double soldeActuel = utilisateurRepository.getSoldeById(demande.getUserId());
            utilisateurRepository.updateSolde(demande.getUserId(), soldeActuel + demande.getDureeJoursOuvres());

            demandeCongeRepository.updateStatus(demandeId, StatutDemande.REFUSEE, commentaire,
                    LocalDate.now(), LocalDate.now());

            logHistorique(demande, StatutDemande.EN_ATTENTE, StatutDemande.REFUSEE, "SYSTEM", commentaire);
        }
    }

    @Override
    public void modifierDemandeEnAttente(DemandeConge demande, String userId) {
        DemandeConge existante = demandeCongeRepository.findById(demande.getId());
        if (existante == null || existante.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new UnauthorizedActionException("Seulement les demandes en attente peuvent être modifiées.");
        }

        /** Si la durée change, il faut ajuster le solde réservé */
        demande.calculerDureeJoursOuvres();
        double diff = demande.getDureeJoursOuvres() - existante.getDureeJoursOuvres();
        if (diff != 0) {
            double soldeActuel = utilisateurRepository.getSoldeById(userId);
            if (soldeActuel < diff)
                throw new InsufficientLeaveBalanceException("Solde insuffisant pour la modification.");
            utilisateurRepository.updateSolde(userId, soldeActuel - diff);
        }

        /** Mise à jour uniquement des champs autorisés */
        existante.setDateDebut(demande.getDateDebut());
        existante.setDemiJourneeDebut(demande.isDemiJourneeDebut());
        existante.setDateFin(demande.getDateFin());
        existante.setDemiJourneeFin(demande.isDemiJourneeFin());
        existante.setTypeConge(demande.getTypeConge());
        existante.setMotif(demande.getMotif());
        existante.setCommentaireEmploye(demande.getCommentaireEmploye());
        existante.setDureeJoursOuvres(demande.getDureeJoursOuvres());
        existante.setDateModification(LocalDate.now());

        demandeCongeRepository.update(existante);

        logHistorique(existante, StatutDemande.EN_ATTENTE, StatutDemande.EN_ATTENTE, userId,
                "Modification de la demande");
    }

    @Override
    public void annulerDemande(String demandeId) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null
                && (demande.getStatut() == StatutDemande.EN_ATTENTE || demande.getStatut() == StatutDemande.VALIDEE)) {
            /**
             * Recréditer le solde dans tous les cas d'annulation (En attente ou Validée)
             */
            double soldeActuel = utilisateurRepository.getSoldeById(demande.getUserId());
            utilisateurRepository.updateSolde(demande.getUserId(), soldeActuel + demande.getDureeJoursOuvres());

            StatutDemande ancienStatut = demande.getStatut();
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.ANNULEE, "Annulée par l'utilisateur",
                    LocalDate.now(), null);

            logHistorique(demande, ancienStatut, StatutDemande.ANNULEE, demande.getUserId(), "Annulation");
        }
    }

    @Override
    public void supprimerDemande(String demandeId) {
        demandeCongeRepository.deleteById(demandeId);
    }

    @Override
    public String exporterRapports(String format, LocalDate debut, LocalDate fin) {
        List<DemandeConge> demandes = demandeCongeRepository.findAll();
        StringBuilder sb = new StringBuilder("Numero,Utilisateur,Debut,Fin,Type,Statut,Duree\n");
        for (DemandeConge d : demandes) {
            sb.append(d.getNumero()).append(",")
                    .append(d.getUserId()).append(",")
                    .append(d.getDateDebut()).append(",")
                    .append(d.getDateFin()).append(",")
                    .append(d.getTypeConge().getLibelle()).append(",")
                    .append(d.getStatut()).append(",")
                    .append(d.getDureeJoursOuvres()).append("\n");
        }
        return sb.toString();
    }

    /**
     * Enregistre une entrée dans l'historique des changements d'état.
     *
     * @param demande la demande concernée
     * @param avant   l'état avant le changement
     * @param apres   l'état après le changement
     * @param user    l'utilisateur ayant effectué l'action
     * @param comment le commentaire associé
     */
    private void logHistorique(DemandeConge demande, StatutDemande avant, StatutDemande apres, String user,
            String comment) {
        HistoriqueEtat hist = new HistoriqueEtat();
        hist.setDemande(demande);
        hist.setStatutAvant(avant);
        hist.setStatutAPres(apres);
        hist.setDateChangement(LocalDateTime.now());
        hist.setUtilisateurChange(user);
        hist.setCommentaire(comment);
        historiqueEtatRepository.save(hist);
    }
}
