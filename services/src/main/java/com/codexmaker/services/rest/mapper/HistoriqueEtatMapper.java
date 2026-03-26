package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import com.codexmaker.services.rest.model.enums.StatutDemande;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Mapper spécialisé dans la transformation des événements d'historique
 * depuis la base de données vers le modèle objet.
 */
public final class HistoriqueEtatMapper {

    private HistoriqueEtatMapper() {
        /** Constructeur privé. */
    }

    /**
     * Reconstruit un objet HistoriqueEtat à partir d'un ResultSet.
     * 
     * @param rs Le ResultSet JDBC.
     * @return L'instance HistoriqueEtat.
     * @throws SQLException En cas d'erreur SQL.
     */
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
            /**
             * SQLite stocke les DATETIME sous forme de chaîne au format standard ISO.
             */
            historique.setDateChangement(
                    LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }

        historique.setUtilisateurChange(rs.getString("utilisateur_change"));
        historique.setCommentaire(rs.getString("commentaire"));

        return historique;
    }
}
