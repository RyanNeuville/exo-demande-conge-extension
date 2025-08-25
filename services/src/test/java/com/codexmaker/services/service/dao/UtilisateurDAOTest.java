package com.codexmaker.services.service.dao;

import com.codexmaker.services.model.entity.Utilisateur;
import com.codexmaker.services.model.enums.Role;
import com.codexmaker.services.utils.Constants;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurDAOTest {
    UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public UtilisateurDAOTest() throws SQLException {
    }


    @Test
    @DisplayName("Test findById - Success: Find user")
    void findById() throws Exception{
        Utilisateur user = utilisateurDAO.findById("12");
        try {
            assertNotNull(user);
            assertEquals("12", user.getId());
            assertEquals("Ryan", user.getNom());
            assertEquals("Feussi", user.getPrenom());
            assertEquals("ryan@gmail.com", user.getEmail());
            assertEquals(Role.ADMIN, user.getRole());
            assertEquals(10, user.getSoldeDemande());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test updateSolde - Success: Updates solde")
    void updateSolde() throws Exception{
        try {
            utilisateurDAO.updateSolde("12", 10);
            Utilisateur user = utilisateurDAO.findById("12");

            assertEquals(10, user.getSoldeDemande());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test save - Success: Inserts user into DB")
    void save() throws Exception{
        Utilisateur userSave = new Utilisateur("2", "Neuville", "Feukouo", "neuville@gmail.com", Role.EMPLOYE, 20);
        utilisateurDAO.save(userSave);

        try {
            Utilisateur userFind = utilisateurDAO.findById("2");

            assertEquals("2", userFind.getId());
            assertEquals("Neuville", userFind.getNom());
            assertEquals("Feukouo", userFind.getPrenom());
            assertEquals("neuville@gmail.com", userFind.getEmail());
            assertEquals(Role.EMPLOYE, userFind.getRole());
            assertEquals(20, userFind.getSoldeDemande());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("Test mapResultSetToUtilisateur - Success: Maps RS to Utilisateur correctly")
    void mapResultSetToUtilisateur_Success() throws SQLException {
        try (
                Connection connection = DataBaseConnection.getConnection();
                PreparedStatement stmt = connection.prepareStatement(String.format(Constants.SQL_GET_LEAVE_REQUEST_BY_ID, Constants.TABLE_UTILISATEUR));

        ) {
            stmt.setString(1, "1");
            ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            Utilisateur result = utilisateurDAO.mapResultSetToUtilisateur(rs);
            assertNotNull(result);
            assertEquals("1", result.getId());
            assertEquals("Doe", result.getNom());
        }
    }
}
