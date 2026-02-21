package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
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
 */
public class UtilisateurRepositoryImpl implements UtilisateurRepository {
    private static final Log LOG = ExoLogger.getLogger(TypeCongeRepositoryImpl.class);

    /**
     * Crée un nouveau type de congé.
     *
     * @param utilisateur l'entité à persister (doit avoir un ID UUID généré)
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
            LOG.error(Constants.LOG_ERREUR_UTILISATEUR_CREATION, e.getMessage(), e);
            throw new RuntimeException(Constants.EXCEPTION_UTILISATEUR_INSERTION_ECHOUEE, e);
        }
    }

    /**
     * Récupère un utilisateur par son ID.
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'entité Utilisateur trouvée, ou null si inexistante
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public Utilisateur findById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'findById'");
    }

    /**
     * Met à jour le solde de congés d'un utilisateur.
     *
     * @param userId   l'identifiant de l'utilisateur
     * @param newSolde le nouveau solde à enregistrer
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public void updateSolde(String userId, int newSolde) {
        throw new UnsupportedOperationException("Unimplemented method 'updateSolde'");
    }

    /**
     * Récupère le solde de congés d'un utilisateur.
     *
     * @param userId l'identifiant de l'utilisateur
     * @return le solde de congés
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public int getSoldeById(String userId) {
        throw new UnsupportedOperationException("Unimplemented method 'getSoldeById'");
    }

    /**
     * Récupère la liste complète des utilisateurs.
     *
     * @return liste non nulle (vide si aucun utilisateur)
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public List<Utilisateur> findAll() {
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }

    /**
     * Récupère la liste des responsables.
     *
     * @return liste non nulle (vide si aucun responsable)
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public List<Utilisateur> findAllResponsables() {
        throw new UnsupportedOperationException("Unimplemented method 'findAllResponsables'");
    }

    /**
     * Vérifie si un utilisateur existe.
     *
     * @param id l'identifiant de l'utilisateur
     * @return true si l'utilisateur existe, false sinon
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public boolean existsById(String id) {
        throw new UnsupportedOperationException("Unimplemented method 'existsById'");
    }

    /**
     * Vérifie si un email est déjà utilisé.
     *
     * @param email l'email à vérifier
     * @return true si l'email existe, false sinon
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public boolean existsByEmail(String email) {
        throw new UnsupportedOperationException("Unimplemented method 'existsByEmail'");
    }

}
