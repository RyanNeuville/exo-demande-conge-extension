package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import com.codexmaker.services.rest.model.enums.StatutDemande;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper pour l'entité HistoriqueEtat.
 */
public final class HistoriqueEtatMapper {

    private HistoriqueEtatMapper() {
    }

    public static HistoriqueEtat fromResultSet(ResultSet rs) throws SQLException {
        HistoriqueEtat historique = new HistoriqueEtat();
        historique.setId(rs.getString("id"));

        String statutAvantStr = rs.getString("statut_avant");
        if (statutAvantStr != null) {
            historique.setStatutAvant(StatutDemande.valueOf(statutAvantStr));
        }

        String statutApresStr = rs.getString("statut_apres");
        if (statutApresStr != null) {
            historique.setStatutAPres(StatutDemande.valueOf(statutApresStr));
        }

        String dateStr = rs.getString("date_changement");
        if (dateStr != null) {
            /** SQLite stored as String, usually ISO format for DATETIME
             * Note: SQLite CURRENT_TIMESTAMP is YYYY-MM-DD HH:MM:SS
             */
            historique.setDateChangement(
                    LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        historique.setUtilisateurChange(rs.getString("utilisateur_change"));
        historique.setCommentaire(rs.getString("commentaire"));

        return historique;
    }
}
