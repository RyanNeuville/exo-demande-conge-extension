package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
import com.codexmaker.services.rest.mapper.UtilisateurMapper;
import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import com.codexmaker.services.rest.utils.Constants;
import com.codexmaker.services.rest.utils.SqlQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

/**
 * Implémentation JDBC du repository pour l'entité Utilisateur.
 * Gère toutes les opérations CRUD sur les utilisateurs.
 * Les utilisateurs sont souvent synchronisés depuis eXo Platform.
 */
public class UtilisateurRepositoryImpl implements UtilisateurRepository {

    private static final Log LOG = ExoLogger.getLogger(UtilisateurRepositoryImpl.class);

    /**
     * Crée ou persiste un nouvel utilisateur (souvent synchronisé depuis eXo).
     *
     * @param utilisateur l'entité à persister (doit avoir un ID généré au
     *                    préalable)
     * @return l'entité persistée avec ID confirmé
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        String sql = SqlQueries.INSERT_UTILISATEUR;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, utilisateur.getId());
            pstmt.setString(2, utilisateur.getNom());
            pstmt.setString(3, utilisateur.getPrenom());
            pstmt.setString(4, utilisateur.getUsername());
            pstmt.setString(5, utilisateur.getEmail());
            pstmt.setString(6, utilisateur.getRole().name());
            pstmt.setInt(7, utilisateur.getSoldeConges());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException(Constants.EXCEPTION_UTILISATEUR_INSERTION_ECHOUEE);
            }
            LOG.info(Constants.LOG_UTILISATEUR_CREE, utilisateur.getId(), utilisateur.getEmail());
            return utilisateur;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_CREATION, utilisateur.getId(), e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_INSERTION_ECHOUEE, e);
        }
    }

    /**
     * Récupère un utilisateur par son ID (username eXo ou UUID).
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'entité Utilisateur trouvée, ou null si inexistante
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public Utilisateur findById(String id) {
        String sql = SqlQueries.SELECT_UTILISATEUR_BY_ID;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Utilisateur utilisateur = UtilisateurMapper.fromResultSet(rs);
                    LOG.debug(Constants.LOG_UTILISATEUR_RECUPERE, id);
                    return utilisateur;
                }
            }
            LOG.debug(Constants.LOG_UTILISATEUR_NON_TROUVE, id);
            return null;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_RECUPERATION, id, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_RECUPERATION_ECHOUEE, e);
        }
    }

    /**
     * Met à jour le solde de congés d'un utilisateur.
     *
     * @param userId   l'identifiant de l'utilisateur
     * @param newSolde le nouveau solde à enregistrer
     * @throws RuntimeException en cas d'erreur SQL ou si l'utilisateur est
     *                          introuvable
     */
    @Override
    public void updateSolde(String userId, int newSolde) {
        String sql = SqlQueries.UPDATE_SOLDE_UTILISATEUR;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newSolde);
            pstmt.setString(2, userId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException(Constants.ERREUR_UTILISATEUR_NON_TROUVE);
            }
            LOG.info(Constants.LOG_SOLDE_MIS_A_JOUR, userId, newSolde);

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_SOLDE, userId, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_SOLDE_ECHOUE, e);
        }
    }

    /**
     * Récupère le solde de congés actuel d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return le solde de congés (en jours)
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public int getSoldeById(String userId) {
        String sql = SqlQueries.SELECT_SOLDE_UTILISATEUR;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int solde = rs.getInt("solde_conges");
                    LOG.debug(Constants.LOG_SOLDE_RECUPERE, userId, solde);
                    return solde;
                }
            }
            throw new SQLException(Constants.ERREUR_UTILISATEUR_NON_TROUVE);

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_SOLDE_LECTURE, userId, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_SOLDE_LECTURE_ECHOUEE, e);
        }
    }

    /**
     * Récupère la liste complète de tous les utilisateurs (triés par nom).
     *
     * @return liste non nulle (vide si aucun utilisateur)
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public List<Utilisateur> findAll() {
        String sql = SqlQueries.SELECT_ALL_UTILISATEURS;
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                utilisateurs.add(UtilisateurMapper.fromResultSet(rs));
            }
            LOG.debug(Constants.LOG_UTILISATEURS_RECUPERES, utilisateurs.size());
            return utilisateurs;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEURS_RECUPERATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEURS_RECUPERATION_ECHOUEE, e);
        }
    }

    /**
     * Récupère la liste de tous les utilisateurs ayant le rôle RESPONSABLE.
     * Utile pour alimenter la liste de sélection du valideur lors d'une demande.
     *
     * @return liste non nulle (vide si aucun responsable)
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public List<Utilisateur> findAllResponsables() {
        String sql = SqlQueries.SELECT_ALL_RESPONSABLES;
        List<Utilisateur> responsables = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                responsables.add(UtilisateurMapper.fromResultSet(rs));
            }
            LOG.debug(Constants.LOG_RESPONSABLES_RECUPERES, responsables.size());
            return responsables;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_RESPONSABLES_RECUPERATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_RESPONSABLES_RECUPERATION_ECHOUEE, e);
        }
    }

    /**
     * Vérifie si un utilisateur existe en base par son ID.
     *
     * @param id l'identifiant de l'utilisateur
     * @return true si l'utilisateur existe, false sinon
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public boolean existsById(String id) {
        String sql = SqlQueries.EXISTS_UTILISATEUR_BY_ID;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    LOG.debug(Constants.LOG_UTILISATEUR_EXISTE, id, exists);
                    return exists;
                }
            }
            return false;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_EXISTENCE, id, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_EXISTENCE_ECHOUEE, e);
        }
    }

    /**
     * Vérifie si un email est déjà utilisé par un autre utilisateur.
     * Utile lors de la création d'un compte pour éviter les doublons.
     *
     * @param email l'adresse email à vérifier
     * @return true si l'email est déjà enregistré, false sinon
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public boolean existsByEmail(String email) {
        String sql = SqlQueries.EXISTS_UTILISATEUR_BY_EMAIL;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    boolean exists = rs.getInt(1) > 0;
                    LOG.debug(Constants.LOG_EMAIL_EXISTE, email, exists);
                    return exists;
                }
            }
            return false;

        } catch (SQLException e) {
            LOG.error(Constants.LOG_ERREUR_EMAIL_EXISTENCE, email, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_EMAIL_EXISTENCE_ECHOUEE, e);
        }
    }
}
