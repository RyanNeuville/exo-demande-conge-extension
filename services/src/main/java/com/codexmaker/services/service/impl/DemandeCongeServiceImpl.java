//package com.codexmaker.services.service.impl;
//
///**
// * Cette classe implémente l'interface DemandeCongeService.
// * Elle est responsable de la gestion des opérations liées aux demandes de congés.
// **/
//
//import com.codexmaker.services.model.dto.DemandeCongeDTO;
//import com.codexmaker.services.model.entity.DemandeConge;
//import com.codexmaker.services.model.entity.Utilisateur;
//import com.codexmaker.services.model.enums.Statut;
//import com.codexmaker.services.service.DemandeCongeService;
//import com.codexmaker.services.utils.Util;
//import javax.ejb.Stateless;
//import com.codexmaker.services.utils.Constants;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.time.temporal.ChronoUnit;
//import java.util.List;
//import java.util.logging.Logger;
//import java.util.stream.Collectors;
//
///**
// * Implémentation du service de gestion des demandes de congé.
// * Contient la logique métier pour les opérations sur les demandes de congé.
// */
//@Stateless
//public class DemandeCongeServiceImpl implements DemandeCongeService {
//
//    private static final Logger log = Logger.getLogger(DemandeCongeServiceImpl.class.getName());
//
//    /**
//     * Cette methode Soumet une nouvelle demande de congé pour un utilisateur donné.
//     * @param demandeDto Le DTO de la demande de congé à soumettre.
//     * @param userId L'identifiant de l'utilisateur qui soumet la demande.
//     * @return Le DTO de la demande soumise avec son identifiant et son statut.
//     */
//    @Override
//    public DemandeCongeDTO soumettreDemande(DemandeCongeDTO demandeDto, String userId){
//        /**
//         * Vérification de l'existence de l'utilisateur.
//         * Si l'utilisateur n'existe pas, une exception est levée.
//         */
//        Utilisateur utilisateur = utilisateurRepository.findById(Long.parseLong(userId))
//                .orElseThrow(() -> new IllegalArgumentException(Constants.USER_ERROR_NOT_FOUND));
//        if (utilisateur == null){
//            throw new IllegalArgumentException(Constants.USER_ERROR_NOT_FOUND);
//        }
//
//        /**
//         * Validation des dates de la demande de congé.
//         * Vérifie que la date de début n'est pas après la date de fin,
//         * et que la durée en jours ouvrés est positive.
//         */
//        if (demandeDto.getDateDebut().isAfter(demandeDto.getDateFin())){
//            Logger.error(Constants.ERROR_MESSAGE_INVALID_DATE);
//            throw new IllegalArgumentException("Erreur: "+Constants.ERROR_MESSAGE_INVALID_DATE);
//        }
//        if (calculerDureeJoursOuvres(demandeDto.getDateDebut(), demandeDto.getDateFin()) <= 0){
//            log.error(Constants.ERROR_MESSAGE_INVALID_DURATION);
//            throw new IllegalArgumentException("Erreur: "+Constants.ERROR_MESSAGE_INVALID_DURATION);
//        }
//
//        DemandeConge demande = new DemandeConge();
//        demande.setDateDebut(demandeDto.getDateDebut());
//        demande.setDateFin(demandeDto.getDateFin());
//        demande.setTypeConge(demandeDto.getTypeConge());
//        demande.setStatut(Statut.EN_ATTENTE);
//        demande.setMotif(demandeDto.getMotif());
//        demande.setUtilisateur(utilisateur);
//        int dureeJoursOuvres = calculerDureeJoursOuvres(demandeDto.getDateDebut(), demandeDto.getDateFin());
//        demande.setDureeJoursOuvres(dureeJoursOuvres);
//        demande.setSoldeDemande(Constants.MAX_LEAVE_REQUEST_BALANCE - dureeJoursOuvres);
//        demande.setDateSoumission(LocalDate.now());
//
//        /** Association de la demande avec l'utilisateur (car il a sa liste d'utilisateur) */
//        utilisateur.faireDemande(demande);
//
//        /** Enregistrement de la demande de congé dans la base de données */
//        demandeCongeRepository.save(demande);
//
//        log.info("Demande de congé soumise avec succès pour l'utilisateur: {}", utilisateur.getId());
//
//        return Util.mapToDto(demande);
//    }
//
//    /**
//     * Cette methode Récupère toutes les demandes de congé d'un utilisateur spécifique.
//     * @param userId L'identifiant de l'utilisateur dont on veut récupérer les demandes.
//     * @return Une liste de DTO de demandes de congé pour l'utilisateur spécifié.
//     */
//    @Override
//    public List<DemandeCongeDTO> getDemandesUtilisateur(String userId) {
//        return List.of();
//    }
//
//    @Override
//    public List<DemandeCongeDTO> getDemandesEnAttenteManager(String managerId) {
//        return List.of();
//    }
//
//    @Override
//    public DemandeCongeDTO annulerDemande(Long demandeId, String userId) {
//        return null;
//    }
//
//
//    /**
//     * Cette methode Récupère toutes les demandes de congé.
//     * @return Une liste de DTO de toutes les demandes de congé.
//     */
//    @Override
//    public List<DemandeCongeDTO> getAllDemandes() {
//        log.info(Constants.GET_ALL_LEAVE_REQUESTS);
//
//        return demandeCongeRepository.findAll()
//                .stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }
//
//
//    /**
//     * Cette methode Approuve une demande de congé.
//     * @param demandeId L'identifiant de la demande de congé à approuver.
//     * @param managerId L'identifiant du manager (admin) qui approuve la demande.
//     * @return la demande mise à jour.
//     */
//    @Override
//    public DemandeConge approuverDemande(Long demandeId, String managerId) {
//        DemandeConge demande = demandeCongeRepository.findById(demandeId)
//                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));
//
//        if (demande.getStatut() != Statut.EN_ATTENTE) {
//            throw new IllegalArgumentException(Constants.ERROR_REQUEST_NOT_PENDING);
//        }
//
//        demande.setStatut(Statut.VALIDEE);
//        demande.setDateModification(LocalDate.now());
//
//        return demandeCongeRepository.save(demande);
//    }
//
//
//    /**
//     * Cette methode Refuse une demande de congé.
//     * @param demandeId L'identifiant de la demande de congé à refuser.
//     * @param managerId L'identifiant du manager (admin) qui refuse la demande.
//     * @return la demande mise à jour.
//     */
//    @Override
//    public DemandeConge refuserDemande(Long demandeId, String managerId) {
//        try {
//            DemandeConge demande = demandeCongeRepository.findById(demandeId)
//                    .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée avec l'ID: " + demandeId));
//
//            if (demande.getStatut() != Statut.EN_ATTENTE) {
//                throw new IllegalArgumentException(Constants.ERROR_REQUEST_NOT_APPROVED);
//            }
//
//            demande.setStatut(Statut.REFUSEE);
//            demande.setDateModification(LocalDate.now());
//
//            return demandeCongeRepository.save(demande);
//        }catch (Exception e){
//            log.error(Constants.ERROR_REQUEST_NOT_APPROVED);
//            throw new IllegalArgumentException(Constants.ERROR_REQUEST_NOT_APPROVED);
//        }
//    }
//
//
//    /**
//     * Cette methode Calcule la durée en jours ouvrés entre deux dates.
//     * @param dateDebut La date de début du congé.
//     * @param dateFin La date de fin du congé.
//     * @return Le nombre de jours entre les deux dates en expluants le samedi et le dimanche.
//     */
//    @Override
//    public int calculerDureeJoursOuvres(LocalDate dateDebut, LocalDate dateFin) {
//        /** Si les dates sont invalides, retourne 0 jours ouvrés */
//        if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)){
//            return 0;
//        }
//        long daysBetween = ChronoUnit.DAYS.between(dateDebut, dateFin.plusDays(1));
//
//        int workingDays = 0;
//        for (int i = 0; i < daysBetween; i++){
//            LocalDate currentDay = dateDebut.plusDays(i);
//            /** Vérifie si le jour est un jour entre (lundi à vendredi) */
//            if(currentDay.getDayOfWeek() != DayOfWeek.SATURDAY && currentDay.getDayOfWeek() != DayOfWeek.SUNDAY){
//                workingDays++;
//            }
//        }
//        return workingDays;
//    }
//
//    /**
//     * Méthode utilitaire pour convertir une entité DemandeConge en DTO DemandeCongeDTO.
//     * @param demande L'entité DemandeConge à convertir.
//     * @return Le DTO DemandeCongeDTO correspondant.
//     */
//    private DemandeCongeDTO convertToDTO(DemandeConge demande) {
//        return Util.mapToDto(demande);
//    }
//
//}
