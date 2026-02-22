package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.utils.Constants;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitaire responsable du mapping entre un ResultSet JDBC
 * et un objet du domaine DemandeConge.
 */
public final class DemandeCongeMapper {

    private DemandeCongeMapper() {
        // Constructeur privé pour empêcher l'instanciation
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

        // Instanciation du sous-objet TypeConge avec son ID uniquement.
        // La charge complète de l'objet TypeConge peut être gérée par le composant
        // appelant si nécessaire.
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
}
