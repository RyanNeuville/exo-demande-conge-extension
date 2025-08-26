package com.codexmaker.services.service.dao;

import com.codexmaker.services.model.entity.Utilisateur;
import com.codexmaker.services.model.enums.Role;
import com.codexmaker.services.utils.Constants;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UtilisateurDAO {

    private static final Logger LOGGER = Logger.getLogger(UtilisateurDAO.class.getName());
    private final String TABLE_NAME = Constants.TABLE_UTILISATEUR;

    public UtilisateurDAO() throws SQLException {
    }

    /**
     * Map un ResultSet sur une entité Utilisateur.
     */
    Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(rs.getString("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setRole(Role.valueOf(rs.getString("role")));
        utilisateur.setSoldeDemande(rs.getInt("solde_demande"));
        return utilisateur;
    }

    /**
     * Enregistre un nouvel utilisateur dans la base de données.
     */
    public void save(Utilisateur utilisateur) {
        try {
            Connection connection = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_CREATE_USER, TABLE_NAME);
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, utilisateur.getId());
            pstmt.setString(2, utilisateur.getNom());
            pstmt.setString(3, utilisateur.getPrenom());
            pstmt.setString(4, utilisateur.getEmail());
            pstmt.setString(5, utilisateur.getRole().toString());
            pstmt.setInt(6, utilisateur.getSoldeDemande());
            pstmt.executeUpdate();
            LOGGER.info("Utilisateur enregistrer avec sussces ");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_USER_SAVE, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
    }

    /**
     * Recherche un utilisateur par son identifiant.
     */
    public Utilisateur findById(String id) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_GET_USER_BY_ID, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUtilisateur(rs);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_USER_FIND_BY_ID, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
        return null;
    }

    /**
     * Met à jour le solde de congés d'un utilisateur.
     */
    public void updateSolde(String userId, int newSolde) {
        try {
            Connection conn = DataBaseConnection.getConnection();
            String sql = String.format(Constants.SQL_UPDATE_USER_SOLDE, TABLE_NAME);
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, newSolde);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, Constants.ERROR_USER_LEAVE_REQUEST_BALANCE_UPDATE, e);
        } finally {
            DataBaseConnection.closeConnection();
        }
    }
}