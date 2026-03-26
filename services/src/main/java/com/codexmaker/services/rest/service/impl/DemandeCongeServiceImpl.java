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
 * Gère la logique de soumission, validation, refus et annulation des demandes,
 * ainsi que le calcul automatique de la durée et la mise à jour des soldes.
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

    /**
     * Soumet une nouvelle demande de congé pour un utilisateur.
     * Réalise plusieurs vérifications : cohérence des dates, chevauchement, et
     * solde suffisant.
     * Réalise également la création automatique de l'utilisateur (JIT) si
     * nécessaire.
     *
     * @param demande L'objet demande contenant les dates et le motif.
     * @param userId  L'identifiant de l'utilisateur qui soumet la demande.
     * @return La demande sauvegardée avec son état initial "EN_ATTENTE".
     * @throws BusinessException                 Si les dates sont incohérentes ou
     *                                           s'il y a un chevauchement.
     * @throws InsufficientLeaveBalanceException Si le solde de l'utilisateur est
     *                                           insuffisant.
     */
    @Override
    public DemandeConge soumettreDemande(DemandeConge demande, String userId) {
        /**
         * Vérification de la cohérence des dates.
         */
        if (demande.getDateDebut() == null || demande.getDateFin() == null) {
            throw new BusinessException("Les dates de début et de fin sont obligatoires.");
        }
        if (demande.getDateDebut().isAfter(demande.getDateFin())) {
            throw new BusinessException("La date de début ne peut pas être après la date de fin.");
        }

        /**
         * Vérification de chevauchement avec une demande existante.
         */
        if (demandeCongeRepository.hasChevauchement(userId, null, demande.getDateDebut(), demande.getDateFin())) {
            throw new BusinessException("Une demande existe déjà pour cette période.");
        }

        /**
         * Vérification / Création automatique de l'utilisateur (Just-In-Time creation).
         */
        if (!utilisateurRepository.existsById(userId)) {
            com.codexmaker.services.rest.model.entity.Employe nouvelUtilisateur = new com.codexmaker.services.rest.model.entity.Employe();
            nouvelUtilisateur.setId(userId);
            nouvelUtilisateur.setUsername(userId);
            nouvelUtilisateur.setNom("Employé");
            nouvelUtilisateur.setPrenom(userId);
            nouvelUtilisateur.setEmail(userId + "@kozao.ko");
            nouvelUtilisateur.setRole(com.codexmaker.services.rest.model.enums.Role.EMPLOYE);
            nouvelUtilisateur.setSoldeConges(25.0);
            utilisateurRepository.save(nouvelUtilisateur);
        }

        /**
         * Calcul de la durée et vérification du solde.
         */
        demande.calculerDureeJoursOuvres();
        double soldeActuel = utilisateurRepository.getSoldeById(userId);
        if (soldeActuel < demande.getDureeJoursOuvres()) {
            throw new InsufficientLeaveBalanceException("Solde insuffisant pour cette demande.");
        }

        /**
         * Initialisation des champs techniques de la demande.
         */
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

        /**
         * Déduction immédiate du solde (réservation).
         */
        utilisateurRepository.updateSolde(userId, soldeActuel - demande.getDureeJoursOuvres());

        /**
         * Sauvegarde effective dans la base de données.
         */
        DemandeConge saved = demandeCongeRepository.save(demande);

        /**
         * Enregistrement de l'action dans l'historique.
         */
        logHistorique(saved, null, StatutDemande.EN_ATTENTE, userId, "Soumission initiale");

        return saved;
    }

    /**
     * Récupère une demande par son identifiant unique.
     *
     * @param demandeId L'identifiant UUID de la demande.
     * @return L'entité demande ou null si non trouvée.
     */
    @Override
    public DemandeConge getDemande(String demandeId) {
        return demandeCongeRepository.findById(demandeId);
    }

    /**
     * Récupère toutes les demandes associées à un utilisateur.
     * L'historique des changements d'état est également chargé pour chaque demande.
     *
     * @param userId L'identifiant de l'utilisateur.
     * @return Une liste de demandes de congés.
     */
    @Override
    public List<DemandeConge> getDemandesParUtilisateur(String userId) {
        List<DemandeConge> demandes = demandeCongeRepository.findByUserId(userId);
        for (DemandeConge d : demandes) {
            d.setHistorique(historiqueEtatRepository.findByDemandeId(d.getId()));
        }
        return demandes;
    }

    /**
     * Liste l'intégralité des demandes présentes dans le système.
     * 
     * @return Liste de toutes les demandes.
     */
    @Override
    public List<DemandeConge> getToutesLesDemandes() {
        return demandeCongeRepository.findAll();
    }

    /**
     * Récupère les demandes en attente d'un valideur spécifique.
     *
     * @param valideurId L'identifiant du manager/valideur.
     * @return Liste des demandes à traiter.
     */
    @Override
    public List<DemandeConge> getDemandesATraiter(String valideurId) {
        return demandeCongeRepository.findPendingForValidator(valideurId);
    }

    /**
     * Valide une demande de congé.
     * Puisque le solde est déjà déduit à la soumission, cette action ne fait que
     * confirmer le statut.
     *
     * @param demandeId   Identifiant de la demande.
     * @param commentaire Note de validation par le manager.
     */
    @Override
    public void validerDemande(String demandeId, String commentaire) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null && demande.getStatut() == StatutDemande.EN_ATTENTE) {
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.VALIDEE, commentaire,
                    LocalDate.now(), LocalDate.now());

            logHistorique(demande, StatutDemande.EN_ATTENTE, StatutDemande.VALIDEE, "SYSTEM", commentaire);
        }
    }

    /**
     * Refuse une demande de congé et recrédite le solde de l'utilisateur.
     *
     * @param demandeId   Identifiant de la demande.
     * @param commentaire Motif du refus.
     */
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

    /**
     * Modifie une demande encore en attente de validation.
     * Gère dynamiquement l'ajustement du solde si la durée est modifiée.
     *
     * @param demande Les nouvelles données de la demande.
     * @param userId  L'identifiant de l'utilisateur effectuant la modification.
     * @throws UnauthorizedActionException       Si la demande n'est pas modifiable.
     * @throws InsufficientLeaveBalanceException Si le nouveau solde requis est
     *                                           indisponible.
     */
    @Override
    public void modifierDemandeEnAttente(DemandeConge demande, String userId) {
        DemandeConge existante = demandeCongeRepository.findById(demande.getId());
        if (existante == null || existante.getStatut() != StatutDemande.EN_ATTENTE) {
            throw new UnauthorizedActionException("Seulement les demandes en attente peuvent être modifiées.");
        }

        /** Ajustement du solde réservé si la durée change */
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

    /**
     * Annule une demande de congé et recrédite systématiquement le solde.
     * L'annulation est possible pour les demandes "EN_ATTENTE" ou "VALIDEE".
     *
     * @param demandeId Identifiant de la demande à annuler.
     */
    @Override
    public void annulerDemande(String demandeId) {
        DemandeConge demande = demandeCongeRepository.findById(demandeId);
        if (demande != null
                && (demande.getStatut() == StatutDemande.EN_ATTENTE || demande.getStatut() == StatutDemande.VALIDEE)) {
            double soldeActuel = utilisateurRepository.getSoldeById(demande.getUserId());
            utilisateurRepository.updateSolde(demande.getUserId(), soldeActuel + demande.getDureeJoursOuvres());

            StatutDemande ancienStatut = demande.getStatut();
            demandeCongeRepository.updateStatus(demandeId, StatutDemande.ANNULEE, "Annulée par l'utilisateur",
                    LocalDate.now(), null);

            logHistorique(demande, ancienStatut, StatutDemande.ANNULEE, demande.getUserId(), "Annulation");
        }
    }

    /**
     * Supprime définitivement une demande de la base de données.
     * 
     * @param demandeId Identifiant de la demande.
     */
    @Override
    public void supprimerDemande(String demandeId) {
        demandeCongeRepository.deleteById(demandeId);
    }

    /**
     * Exporte les rapports de congés dans une chaîne CSV structurée.
     *
     * @param format Le format (actuellement seul CSV est supporté).
     * @param debut  Date de début de filtre.
     * @param fin    Date de fin de filtre.
     * @return Chaîne de caractères au format CSV.
     */
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
