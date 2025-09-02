package com.codexmaker.services.service.dao;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.TypeConge;
import com.codexmaker.services.utils.Constants;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DemandeCongeDAO {

    private static final Logger LOGGER = Logger.getLogger(DemandeCongeDAO.class.getName());
    private final String TABLE_NAME = Constants.TABLE_DEMANDE_CONGE;

    /**
     * Map un ResultSet sur une entité DemandeConge.
     * Prend en charge les champs supplémentaires de l'utilisateur.
     */
    DemandeConge mapResultSetToDemandeConge(ResultSet rs) throws SQLException {
        DemandeConge demande = new DemandeConge();
        demande.setId(rs.getInt("id"));
        demande.setUserId(rs.getString("user_id"));
        demande.setNom(rs.getString("nom"));
        demande.setPrenom(rs.getString("prenom"));

        /** Les noms d'utilisateur peuvent venir directement de la vue jointe ou être null si non présents.
         * 'user_nom', 'user_prenom' de la vue pour éviter les conflits en basede donnees.
         * */
        try {
            demande.setNom(rs.getString("user_nom"));
            demande.setPrenom(rs.getString("user_prenom"));
        } catch (SQLException e) {
            LOGGER.log(Level.FINE, Constants.ERROR_NOT_COLLUM_IN_RESULTSET + e.getMessage());
        }

        demande.setDateDebut(rs.getDate("date_debut").toLocalDate());
        demande.setDateFin(rs.getDate("date_fin").toLocalDate());
        demande.setTypeConge(TypeConge.valueOf(rs.getString("type_conge")));
        demande.setStatut(Statut.valueOf(rs.getString("statut")));
        demande.setMotif(rs.getString("motif"));
        demande.setCommentairesManager(rs.getString("commentaires_manager"));
        demande.setDateSoumission(rs.getDate("date_soumission").toLocalDate());
        if (rs.getDate("date_modification") != null) {
            demande.setDateModification(rs.getDate("date_modification").toLocalDate());
        }
        demande.setSoldeDemande(rs.getInt("solde_demande"));
        demande.setDureeJoursOuvres(rs.getInt("duree_jours_ouvres"));
        return demande;
    }

    /**
     * Enregistre une nouvelle demande de congé dans la base de données.
     */
    public DemandeConge save(DemandeConge demande) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_CREATE_LEAVE_REQUEST, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, demande.getUserId());
            pstmt.setString(2, demande.getNom());
            pstmt.setString(3, demande.getPrenom());
            pstmt.setDate(4, java.sql.Date.valueOf(demande.getDateDebut()));
            pstmt.setDate(5, java.sql.Date.valueOf(demande.getDateFin()));
            pstmt.setString(6, demande.getTypeConge().toString());
            pstmt.setString(7, demande.getStatut().toString());
            pstmt.setString(8, demande.getMotif());
            pstmt.setString(9, demande.getCommentairesManager());
            pstmt.setDate(10, java.sql.Date.valueOf(demande.getDateSoumission()));
            pstmt.setDate(11, null);
            pstmt.setInt(12, demande.getSoldeDemande());
            pstmt.setInt(13, demande.getDureeJoursOuvres());
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                demande.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_LEAVE_REQUEST_SAVE, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return demande;
    }

    /**
     * Met à jour une demande de congé existante.
     */
    public void update(DemandeConge demande) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_UPDATE_LEAVE_REQUEST, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, demande.getUserId());
            pstmt.setString(2, demande.getNom());
            pstmt.setString(3, demande.getPrenom());
            pstmt.setDate(4, Date.valueOf(demande.getDateDebut()));
            pstmt.setDate(5, Date.valueOf(demande.getDateFin()));
            pstmt.setString(6, demande.getTypeConge().toString());
            pstmt.setString(7, demande.getStatut().toString());
            pstmt.setString(8, demande.getMotif());
            pstmt.setString(9, demande.getCommentairesManager());
            pstmt.setDate(10, Date.valueOf(demande.getDateModification()));
            pstmt.setInt(11, demande.getSoldeDemande());
            pstmt.setInt(12, demande.getDureeJoursOuvres());
            pstmt.setLong(13, demande.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_LEAVE_REQUEST_UPDATE, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
    }

    /**
     * Recherche une demande par son identifiant.
     */
    public DemandeConge findById(int id) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_GET_LEAVE_REQUEST_BY_ID, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setLong(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDemandeConge(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_LEAVE_REQUEST_FIND_BY_ID, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return null;
    }

    public List<DemandeConge> findByStatus(Statut statut){
        List<DemandeConge> demandesEnAttente = new ArrayList<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql =  String.format(Constants.SQL_GET_LEAVE_REQUESTS_WHERE_STATUS_EN_ATTENTE, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                demandesEnAttente.add(mapResultSetToDemandeConge(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_GET_ALL_LEAVE_REQUEST, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return demandesEnAttente;
    }

    /**
     * Récupère toutes les demandes de congé d'un utilisateur, y compris les noms/prénoms via la vue.
     */
    public List<DemandeConge> findByUserId(String userId) {
        List<DemandeConge> demandes = new ArrayList<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = Constants.SQL_GET_LEAVE_REQUESTS_FROM_VIEW_BY_USER_ID;
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                demandes.add(mapResultSetToDemandeConge(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_GET_LEAVE_REQUEST_BY_ID_WITH_VIEW, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return demandes;
    }

    /**
     * Récupère toutes les demandes de congé.
     */
    public List<DemandeConge> findAll() {
        List<DemandeConge> demandes = new ArrayList<>();
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_GET_ALL_LEAVE_REQUESTS, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                demandes.add(mapResultSetToDemandeConge(rs));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_GET_ALL_LEAVE_REQUEST, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return demandes;
    }

    /**
     * Met à jour le statut d'une demande de congé.
     */
    public void updateStatut(Long id, Statut newStatut, String commentaires, LocalDate dateModification) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql;
            if (newStatut == Statut.APPROUVEE) {
                sql = String.format(Constants.SQL_APPROVE_LEAVE_REQUEST, TABLE_NAME);
            } else if (newStatut == Statut.REFUSEE) {
                sql = String.format(Constants.SQL_REJECT_LEAVE_REQUEST, TABLE_NAME);
            } else if (newStatut == Statut.ANNULEE) {
                sql = String.format(Constants.SQL_ANNULER_LEAVE_REQUEST, TABLE_NAME);
            } else {
                throw new IllegalArgumentException(Constants.ERROR_NOT_FOUND_STATUS + newStatut);
            }
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDate(1, Date.valueOf(dateModification));
            pstmt.setString(2, commentaires);
            pstmt.setLong(3, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_STATUS_LEAVE_REQUEST_UPDATE, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
    }
}