package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
import com.codexmaker.services.rest.mapper.HistoriqueEtatMapper;
import com.codexmaker.services.rest.model.entity.HistoriqueEtat;
import com.codexmaker.services.rest.repository.HistoriqueEtatRepository;
import com.codexmaker.services.rest.utils.Constants;
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
 * Implémentation JDBC pour la gestion de l'historique des états.
 * Permet de conserver une trace de toutes les actions et changements de statut
 * sur les demandes de congés.
 */
public class HistoriqueEtatRepositoryImpl implements HistoriqueEtatRepository {

    private static final Log LOG = ExoLogger.getLogger(HistoriqueEtatRepositoryImpl.class);

    /**
     * Enregistre un nouvel événement dans l'historique d'une demande.
     *
     * @param historique L'entrée d'historique à sauvegarder.
     */
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
            LOG.error(Constants.ERROR_SAVE_HISTORIQUE_ETAT + e.getMessage(), e);
        }
    }

    /**
     * Récupère l'historique chronologique pour une demande de congé donnée.
     *
     * @param demandeId L'identifiant de la demande concernée.
     * @return Liste chronologique des changements d'état.
     */
    @Override
    public List<HistoriqueEtat> findByDemandeId(String demandeId) {
        String sql = SqlQueries.GET_HISTORIQUE_ETAT_BY_DEMANDE_ID;
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
            LOG.error(Constants.ERROR_GET_HISTORIQUE_ETAT + e.getMessage(), e);
            return list;
        }
    }
}
