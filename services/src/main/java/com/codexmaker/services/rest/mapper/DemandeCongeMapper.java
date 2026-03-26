package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.dto.DemandeCongeDTO;
import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.utils.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.codexmaker.services.rest.dto.DemandeCongeResponseDTO;

/**
 * Classe utilitaire responsable du mapping entre un ResultSet JDBC
 * et un objet du domaine DemandeConge.
 */
public final class DemandeCongeMapper {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private DemandeCongeMapper() {
        /** Constructeur privé pour empêcher l'instanciation */
    }

    /**
     * Map une ligne courante de ResultSet en un objet DemandeConge.
     * Cette méthode ne ferme pas le ResultSet et n'appelle pas rs.next().
     *
     * @param rs le ResultSet positionné sur la ligne à mapper
     * @return un objet DemandeConge instancié et populé
     * @throws SQLException s'il y a une erreur d'accès aux colonnes du ResultSet
     */
    public static DemandeConge fromResultSet(ResultSet rs) throws SQLException {
        DemandeConge demande = new DemandeConge();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.FORMAT_DATE_SQLITE);

        demande.setId(rs.getString("id"));
        demande.setNumero(rs.getString("numero"));
        demande.setUserId(rs.getString("user_id"));

        String dateCreationStr = rs.getString("date_creation");
        if (dateCreationStr != null) {
            demande.setDateCreation(LocalDate.parse(dateCreationStr));
        }

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
         * Instanciation du sous-objet TypeConge avec son ID uniquement.
         * La charge complète de l'objet TypeConge peut être gérée par le composant
         * appelant si nécessaire.
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

        demande.setSoldeCongesAvant(rs.getInt("solde_conges_avant"));
        demande.setDureeJoursOuvres(rs.getInt("duree_jours_ouvres"));

        return demande;
    }

    public static DemandeConge toEntity(DemandeCongeDTO dto) {
        if (dto == null) return null;

        DemandeConge entity = new DemandeConge();
        entity.setId(dto.getId());
        entity.setUserId(dto.getUserId());
        entity.setDemiJourneeDebut(dto.isDemiJourneeDebut());
        entity.setDemiJourneeFin(dto.isDemiJourneeFin());
        entity.setMotif(dto.getMotif());
        entity.setCommentaireEmploye(dto.getCommentaireEmploye());
        entity.setDureeJoursOuvres(dto.getDureeJoursOuvres());
        entity.setTypeConge(dto.getTypeConge());

        /** Cas specifiques pour les convertions des dates, statut en String */
        if (dto.getDateDebut() != null) {
            entity.setDateDebut(LocalDate.parse(dto.getDateDebut(), FORMATTER));
        }
        if (dto.getDateFin() != null) {
            entity.setDateFin(LocalDate.parse(dto.getDateFin(), FORMATTER));
        }
        
        return entity;
    }

    public static DemandeCongeResponseDTO toResponseDTO(DemandeConge entity) {
        if (entity == null) return null;

        DemandeCongeResponseDTO dto = new DemandeCongeResponseDTO();
        dto.setId(entity.getId());
        dto.setNumero(entity.getNumero());
        dto.setUserId(entity.getUserId());
        
        // Dates (Conversion LocalDate -> String)
        if (entity.getDateDebut() != null) dto.setDateDebut(entity.getDateDebut().toString());
        if (entity.getDateFin() != null) dto.setDateFin(entity.getDateFin().toString());
        
        dto.setDemiJourneeDebut(entity.isDemiJourneeDebut());
        dto.setDemiJourneeFin(entity.isDemiJourneeFin());
        dto.setTypeConge(entity.getTypeConge());
        dto.setStatut(entity.getStatut());
        dto.setMotif(entity.getMotif());
        dto.setCommentaireEmploye(entity.getCommentaireEmploye());
        dto.setCommentaireValideur(entity.getCommentaireValideur());
        dto.setValideurId(entity.getValideurId());

        // Date/Time (Conversion LocalDate -> String, simplification pour eXo)
        if (entity.getDateSoumission() != null) dto.setDateSoumission(entity.getDateSoumission().toString());
        if (entity.getDateValidation() != null) dto.setDateValidation(entity.getDateValidation().toString());
        if (entity.getDateModification() != null) dto.setDateModification(entity.getDateModification().toString());

        dto.setSoldeCongesAvant(entity.getSoldeCongesAvant());
        dto.setDureeJoursOuvres(entity.getDureeJoursOuvres());

        return dto;
    }
}
