package com.codexmaker.services.service.dao;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.TypeConge;
import com.codexmaker.services.utils.Constants;
import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
public class DemandeCongeDAOTest {
    DemandeCongeDAO demandeCongeDAO = new DemandeCongeDAO();

    public DemandeCongeDAOTest() throws SQLException {
    }

    @Test
    @Step("Mapping du resultset en objet DemandeConge - succes")
    @DisplayName("Test mapResultSetToDemandeConge - Success: Maps RS to DemandeConge correctly")
    void mapResultSetToDemandeConge_Success() throws Exception {
        try (
                java.sql.Connection connection = DataBaseConnection.getConnection();
                java.sql.PreparedStatement stmt = connection.prepareStatement(String.format(Constants.SQL_GET_LEAVE_REQUEST_BY_ID, Constants.TABLE_DEMANDE_CONGE));
        ) {
            stmt.setInt(1, 1);
            java.sql.ResultSet rs = stmt.executeQuery();
            assertTrue(rs.next());
            DemandeConge result = demandeCongeDAO.mapResultSetToDemandeConge(rs);
            assertNotNull(result);
            assertEquals(1, result.getId());
        }
    }

    @Test
    @Step("Enregistrement d'une demande de conge - succes")
    @DisplayName("Test save - Success: Inserts demande into DB")
    void save_Success() throws Exception {
        DemandeConge demandeSave = new DemandeConge();
        demandeSave.setUserId("3");
        demandeSave.setNom("Doe237");
        demandeSave.setPrenom("John237");
        demandeSave.setDateDebut(LocalDate.now());
        demandeSave.setDateFin(LocalDate.now().plusDays(5));
        demandeSave.setTypeConge(TypeConge.CONGE_ANNUEL);
        demandeSave.setStatut(Statut.EN_ATTENTE);
        demandeSave.setMotif("Vacances");
        demandeSave.setCommentairesManager(null);
        demandeSave.setDateSoumission(LocalDate.now());
        demandeSave.setDateModification(null);
        demandeSave.setSoldeDemande(20);
        demandeSave.setDureeJoursOuvres(5);

        DemandeConge saved = demandeCongeDAO.save(demandeSave);

        try {
            DemandeConge demandeFind = demandeCongeDAO.findById(saved.getId());

            assertNotNull(demandeFind);
            assertEquals("3", demandeFind.getUserId());
            assertEquals("Doe237", demandeFind.getNom());
            assertEquals("John237", demandeFind.getPrenom());
            assertEquals(demandeSave.getDateDebut(), demandeFind.getDateDebut());
            assertEquals(demandeSave.getDateFin(), demandeFind.getDateFin());
            assertEquals(TypeConge.CONGE_ANNUEL, demandeFind.getTypeConge());
            assertEquals(Statut.EN_ATTENTE, demandeFind.getStatut());
            assertEquals("Vacances", demandeFind.getMotif());
            assertEquals(20, demandeFind.getSoldeDemande());
            assertEquals(5, demandeFind.getDureeJoursOuvres());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Step("Modification d'une demande de conge - succes")
    @DisplayName("Test update - Success: Updates demande in DB")
    void update_Success() throws Exception {
        DemandeConge demandeUpdate = new DemandeConge();
        demandeUpdate.setId(1);
        demandeUpdate.setUserId("1");
        demandeUpdate.setNom("Doe Updated");
        demandeUpdate.setPrenom("John Updated");
        demandeUpdate.setDateDebut(LocalDate.now());
        demandeUpdate.setDateFin(LocalDate.now().plusDays(10));
        demandeUpdate.setTypeConge(TypeConge.CONGE_MALADIE);
        demandeUpdate.setStatut(Statut.APPROUVEE);
        demandeUpdate.setMotif("Maladie");
        demandeUpdate.setCommentairesManager("Approuvé");
        demandeUpdate.setDateModification(LocalDate.now());
        demandeUpdate.setSoldeDemande(15);
        demandeUpdate.setDureeJoursOuvres(10);

        demandeCongeDAO.update(demandeUpdate);

        try {
            DemandeConge demandeFind = demandeCongeDAO.findById(1);

            assertEquals("Doe Updated", demandeFind.getNom());
            assertEquals("John Updated", demandeFind.getPrenom());
            assertEquals(TypeConge.CONGE_MALADIE, demandeFind.getTypeConge());
            assertEquals(Statut.APPROUVEE, demandeFind.getStatut());
            assertEquals("Maladie", demandeFind.getMotif());
            assertEquals("Approuvé", demandeFind.getCommentairesManager());
            assertEquals(15, demandeFind.getSoldeDemande());
            assertEquals(10, demandeFind.getDureeJoursOuvres());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Step("Recherche d'une demande de conge - succes")
    @DisplayName("Test findById - Success: Find demande by ID")
    void findById_Success() throws Exception {
        DemandeConge demande = demandeCongeDAO.findById(1);
        assertNotNull(demande);
        assertEquals(1, demande.getId());
    }

    @Test
    @Step("Recherche d'une demande de conge propre a un utilisateur - succes")
    @DisplayName("Test findByUserId - Success: Find demandes by user ID")
    void findByUserId_Success() throws Exception {
        List<DemandeConge> demandes = demandeCongeDAO.findByUserId("1");
        System.out.println(demandes);
        assertNotNull(demandes);
        assertFalse(demandes.isEmpty());

        DemandeConge first = demandes.getFirst();
        assertEquals("1", first.getUserId());
    }

    @Test
    @Step("Recuperer tous les demande de congee (admin) - succes")
    @DisplayName("Test findAll - Success: Find all demandes")
    void findAll_Success() throws Exception {
        List<DemandeConge> demandes = demandeCongeDAO.findAll();
        assertNotNull(demandes);
        assertFalse(demandes.isEmpty());
    }

    @Test
    @Step("Modifier le statut d''une demande de conge (aprouve, refuse) - succes")
    @DisplayName("Test updateStatut - Success: Updates statut")
    void updateStatut_Success() throws Exception {
        demandeCongeDAO.updateStatut(1L, Statut.APPROUVEE, "Approuvé", LocalDate.now());

        DemandeConge demande = demandeCongeDAO.findById(1);
        assertEquals(Statut.APPROUVEE, demande.getStatut());
        assertEquals("Approuvé", demande.getCommentairesManager());
    }
}