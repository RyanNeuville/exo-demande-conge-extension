package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.dto.DemandeCongeDTO;
import com.codexmaker.services.rest.dto.DemandeCongeResponseDTO;
import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.utils.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire responsable du mapping entre les différents formats de
 * données.
 * Gère la conversion entre ResultSet JDBC, entités du domaine et DTOs de
 * réponse.
 */
public final class DemandeCongeMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private DemandeCongeMapper() {
        /**
         * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
         */
    }

    /**
     * Transforme une ligne de résultat SQL en une instance de l'entité
     * DemandeConge.
     *
     * @param rs Le ResultSet positionné sur la ligne cible.
     * @return L'entité DemandeConge populée.
     * @throws SQLException En cas d'erreur de lecture des colonnes.
     */
    public static DemandeConge fromResultSet(ResultSet rs) throws SQLException {
        DemandeConge demande = new DemandeConge();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE_SQLITE);

        demande.setId(rs.getString("id"));
        demande.setNumero(rs.getString("numero"));
        demande.setUserId(rs.getString("user_id"));

        String dateSoumissionStr = rs.getString("date_soumission");
        if (dateSoumissionStr != null) {
            demande.setDateSoumission(LocalDate.parse(dateSoumissionStr, dateFormatter));
        }

        String dateDebutStr = rs.getString("date_debut");
        if (dateDebutStr != null) {
            demande.setDateDebut(LocalDate.parse(dateDebutStr, dateFormatter));
        }

        String dateFinStr = rs.getString("date_fin");
        if (dateFinStr != null) {
            demande.setDateFin(LocalDate.parse(dateFinStr, dateFormatter));
        }

        demande.setDemiJourneeDebut(rs.getBoolean("demi_journee_debut"));
        demande.setDemiJourneeFin(rs.getBoolean("demi_journee_fin"));

        /**
         * Chargement partiel du type de congé (ID uniquement).
         */
        TypeConge typeConge = new TypeConge();
        typeConge.setId(rs.getString("type_conge_id"));
        demande.setTypeConge(typeConge);

        String statutStr = rs.getString("statut");
        if (statutStr != null) {
            demande.setStatut(StatutDemande.valueOf(statutStr));
        }

        demande.setMotif(rs.getString("motif"));
        demande.setCommentaireEmploye(rs.getString("commentaire_employe"));
        demande.setCommentaireValideur(rs.getString("commentaire_valideur"));
        demande.setValideurId(rs.getString("valideur_id"));

        String dateValidationStr = rs.getString("date_validation");
        if (dateValidationStr != null) {
            demande.setDateValidation(LocalDate.parse(dateValidationStr, dateFormatter));
        }

        String dateModificationStr = rs.getString("date_modification");
        if (dateModificationStr != null) {
            demande.setDateModification(LocalDate.parse(dateModificationStr, dateFormatter));
        }

        demande.setSoldeCongesAvant(rs.getDouble("solde_conges_avant"));
        demande.setDureeJoursOuvres(rs.getDouble("duree_jours_ouvres"));

        return demande;
    }

    /**
     * Convertit un DTO de soumission en entité persistable.
     *
     * @param dto Le DTO reçu via l'API REST.
     * @return L'entité DemandeConge correspondante.
     */
    public static DemandeConge toEntity(DemandeCongeDTO dto) {
        if (dto == null)
            return null;

        DemandeConge entity = new DemandeConge();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setDemiJourneeDebut(dto.isDemiJourneeDebut());
        entity.setDemiJourneeFin(dto.isDemiJourneeFin());
        entity.setMotif(dto.getMotif());
        entity.setCommentaireEmploye(dto.getCommentaireEmploye());
        entity.setDureeJoursOuvres(dto.getDureeJoursOuvres());
        entity.setTypeConge(dto.getTypeConge());

        if (dto.getDateDebut() != null) {
            entity.setDateDebut(LocalDate.parse(dto.getDateDebut(), FORMATTER));
        }
        if (dto.getDateFin() != null) {
            entity.setDateFin(LocalDate.parse(dto.getDateFin(), FORMATTER));
        }

        return entity;
    }

    /**
     * Transforme une entité en un DTO de réponse optimisé pour le JSON.
     *
     * @param entity L'entité chargée depuis le repository.
     * @return Le DTO de réponse contenant les données formatées.
     */
    public static DemandeCongeResponseDTO toResponseDTO(DemandeConge entity) {
        if (entity == null)
            return null;

        DemandeCongeResponseDTO dto = new DemandeCongeResponseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setUserId(entity.getUserId());

        if (entity.getDateDebut() != null)
            dto.setDateDebut(entity.getDateDebut().toString());
        if (entity.getDateFin() != null)
            dto.setDateFin(entity.getDateFin().toString());

        dto.setDemiJourneeDebut(entity.isDemiJourneeDebut());
        dto.setDemiJourneeFin(entity.isDemiJourneeFin());
        dto.setTypeConge(entity.getTypeConge());
        dto.setStatut(entity.getStatut());
        dto.setMotif(entity.getMotif());
        dto.setCommentaireEmploye(entity.getCommentaireEmploye());
        dto.setCommentaireValideur(entity.getCommentaireValideur());
        dto.setValideurId(entity.getValideurId());

        if (entity.getDateSoumission() != null)
            dto.setDateSoumission(entity.getDateSoumission().toString());
        if (entity.getDateValidation() != null)
            dto.setDateValidation(entity.getDateValidation().toString());
        if (entity.getDateModification() != null)
            dto.setDateModification(entity.getDateModification().toString());

        dto.setSoldeCongesAvant(entity.getSoldeCongesAvant());
        dto.setDureeJoursOuvres(entity.getDureeJoursOuvres());

        return dto;
    }
}
