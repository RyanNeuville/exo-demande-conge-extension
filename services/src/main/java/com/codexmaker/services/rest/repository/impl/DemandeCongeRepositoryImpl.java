package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
import com.codexmaker.services.rest.mapper.DemandeCongeMapper;
import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.repository.DemandeCongeRepository;
import com.codexmaker.services.rest.utils.Constants;
import com.codexmaker.services.rest.utils.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Implémentation JDBC du repository pour l'entité DemandeConge.
 * Gère le cycle de vie des demandes en base de données.
 */
public class DemandeCongeRepositoryImpl implements DemandeCongeRepository {

    private static final Log LOG = ExoLogger.getLogger(DemandeCongeRepositoryImpl.class);

    @Override
    public DemandeConge save(DemandeConge demande) {
        String sql = SqlQueries.INSERT_DEMANDE_CONGE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, demande.getId());
            pstmt.setString(2, demande.getNumero());
            pstmt.setString(3, demande.getUserId());
            pstmt.setString(4, demande.getDateDebut() != null ? demande.getDateDebut().toString() : null);
            pstmt.setBoolean(5, demande.isDemiJourneeDebut());
            pstmt.setString(6, demande.getDateFin() != null ? demande.getDateFin().toString() : null);
            pstmt.setBoolean(7, demande.isDemiJourneeFin());
            pstmt.setString(8, demande.getTypeConge() != null ? demande.getTypeConge().getId() : null);
            pstmt.setString(9, demande.getStatut() != null ? demande.getStatut().name() : null);
            pstmt.setString(10, demande.getMotif());
            pstmt.setString(11, demande.getCommentaireEmploye());
            pstmt.setString(12, demande.getCommentaireValideur());
            pstmt.setString(13, demande.getValideurId());
            pstmt.setString(14, demande.getDateSoumission() != null ? demande.getDateSoumission().toString() : null);
            pstmt.setString(15,
                    demande.getDateModification() != null ? demande.getDateModification().toString() : null);
            pstmt.setString(16, demande.getDateValidation() != null ? demande.getDateValidation().toString() : null);
            pstmt.setInt(17, demande.getSoldeCongesAvant());
            pstmt.setInt(18, demande.getDureeJoursOuvres());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException(Constants.EXCEPTION_DEMANDE_INSERTION_ECHOUEE);
            }
            LOG.info(Constants.LOG_DEMANDE_SOUMISE, demande.getId(), demande.getUserId());
            return demande;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_CREATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_INSERTION_ECHOUEE, e);
        }
    }

    @Override
    public void update(DemandeConge demande) {
        String sql = SqlQueries.UPDATE_DEMANDE_CONGE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, demande.getUserId());
            pstmt.setString(2, demande.getDateDebut() != null ? demande.getDateDebut().toString() : null);
            pstmt.setBoolean(3, demande.isDemiJourneeDebut());
            pstmt.setString(4, demande.getDateFin() != null ? demande.getDateFin().toString() : null);
            pstmt.setBoolean(5, demande.isDemiJourneeFin());
            pstmt.setString(6, demande.getTypeConge() != null ? demande.getTypeConge().getId() : null);
            pstmt.setString(7, demande.getStatut() != null ? demande.getStatut().name() : null);
            pstmt.setString(8, demande.getMotif());
            pstmt.setString(9, demande.getCommentaireEmploye());
            pstmt.setString(10, demande.getCommentaireValideur());
            pstmt.setString(11, demande.getValideurId());
            pstmt.setString(12,
                    demande.getDateModification() != null ? demande.getDateModification().toString() : null);
            pstmt.setInt(13, demande.getSoldeCongesAvant());
            pstmt.setInt(14, demande.getDureeJoursOuvres());
            pstmt.setString(15, demande.getId());

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException(Constants.ERREUR_DEMANDE_NON_TROUVEE);
            }
            LOG.info(Constants.LOG_DEMANDE_MODIFIEE, demande.getId());

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_MODIFICATION_BD, demande.getId(), e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_MODIFICATION_ECHOUEE, e);
        }
    }

    @Override
    public DemandeConge findById(String id) {
        String sql = SqlQueries.SELECT_DEMANDE_BY_ID;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return DemandeCongeMapper.fromResultSet(rs);
                }
            }
            return null;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_RECUPERATION, id, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_RECUPERATION_ECHOUEE, e);
        }
    }

    @Override
    public List<DemandeConge> findByUserId(String userId) {
        String sql = SqlQueries.SELECT_DEMANDES_BY_USER_ID;
        List<DemandeConge> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(DemandeCongeMapper.fromResultSet(rs));
                }
            }
            LOG.debug(Constants.LOG_DEMANDES_RECUPEREES, list.size());
            return list;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDES_RECUPERATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_RECUPERATION_ECHOUEE, e);
        }
    }

    @Override
    public List<DemandeConge> findAll() {
        String sql = SqlQueries.SELECT_ALL_DEMANDES;
        List<DemandeConge> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                list.add(DemandeCongeMapper.fromResultSet(rs));
            }
            LOG.debug(Constants.LOG_DEMANDES_RECUPEREES, list.size());
            return list;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDES_RECUPERATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_RECUPERATION_ECHOUEE, e);
        }
    }

    @Override
    public List<DemandeConge> findPendingForValidator(String validatorId) {
        String sql = SqlQueries.CONSULTER_DEMANDE_A_TRAITER;
        List<DemandeConge> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, validatorId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(DemandeCongeMapper.fromResultSet(rs));
                }
            }
            LOG.debug(Constants.LOG_DEMANDES_RECUPEREES, list.size());
            return list;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDES_RECUPERATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_RECUPERATION_ECHOUEE, e);
        }
    }

    @Override
    public void updateStatus(String id, StatutDemande statut, String commentaire, LocalDate dateModif,
            LocalDate dateValid) {
        String sql = SqlQueries.UPDATE_STATUT_DEMANDE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, statut.name());
            pstmt.setString(2, commentaire);
            pstmt.setString(3, dateModif != null ? dateModif.toString() : null);
            pstmt.setString(4, dateValid != null ? dateValid.toString() : null);
            pstmt.setString(5, id);

            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException(Constants.ERREUR_DEMANDE_NON_TROUVEE);
            }
            LOG.info(Constants.LOG_STATUT_DEMANDE_MIS_A_JOUR, id, statut.name());

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_STATUT, id, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_STATUT_ECHOUEE, e);
        }
    }

    @Override
    public boolean hasChevauchement(String userId, String excludeDemandeId, LocalDate debut, LocalDate fin) {
        String sql = SqlQueries.CHECK_CHEVAUCHEMENT;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setString(2, excludeDemandeId != null ? excludeDemandeId : "");
            pstmt.setString(3, fin.toString());
            pstmt.setString(4, debut.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_CHEVAUCHEMENT, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_RECUPERATION_ECHOUEE, e);
        }
    }

    @Override
    public void deleteById(String id) {
        String sql = SqlQueries.SUPPRIMER_DEMANDE_BY_ID;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new SQLException(Constants.ERREUR_DEMANDE_NON_TROUVEE);
            }
            LOG.info(Constants.LOG_DEMANDE_SUPPRIMEE, id);

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_DEMANDE_SUPPRESSION, id, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_DEMANDE_SUPPRESSION_ECHOUEE, e);
        }
    }
}
