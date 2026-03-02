package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
import com.codexmaker.services.rest.mapper.HistoriqueEtatMapper;
import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import com.codexmaker.services.rest.repository.HistoriqueEtatRepository;
import com.codexmaker.services.rest.utils.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Implémentation JDBC pour l'historique des états.
 */
public class HistoriqueEtatRepositoryImpl implements HistoriqueEtatRepository {

    private static final Log LOG = ExoLogger.getLogger(HistoriqueEtatRepositoryImpl.class);

    @Override
    public void save(HistoriqueEtat historique) {
        if (historique.getId() == null) {
            historique.setId(UUID.randomUUID().toString());
        }

        String sql = SqlQueries.INSERT_HISTORIQUE_ETAT;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, historique.getId());
            pstmt.setString(2, historique.getDemande().getId());
            pstmt.setString(3, historique.getStatutAvant() != null ? historique.getStatutAvant().name() : null);
            pstmt.setString(4, historique.getStatutAPres().name());
            pstmt.setString(5, java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setString(6, historique.getUtilisateurChange());
            pstmt.setString(7, historique.getCommentaire());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            LOG.error("Erreur lors de l'enregistrement de l'historique : " + e.getMessage(), e);
        }
    }

    @Override
    public List<HistoriqueEtat> findByDemandeId(String demandeId) {
        String sql = "SELECT id, demande_id, statut_avant, statut_apres, date_changement, utilisateur_change, commentaire FROM historique_etat WHERE demande_id = ? ORDER BY date_changement ASC";
        List<HistoriqueEtat> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, demandeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(HistoriqueEtatMapper.fromResultSet(rs));
                }
            }
            return list;

        } catch (SQLException e) {
            LOG.error("Erreur lors de la récupération de l'historique : " + e.getMessage(), e);
            return list;
        }
    }
}
