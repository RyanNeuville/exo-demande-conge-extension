package com.codexmaker.services.rest.repository.impl;

import com.codexmaker.services.rest.config.DatabaseConnection;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.repository.TypeCongeRepository;
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
 * Implémentation JDBC du repository pour l'entité TypeConge.
 * Gère toutes les opérations CRUD sur les types de congés (côté admin).
 */
public class TypeCongeRepositoryImpl implements TypeCongeRepository {
    private static final Log LOG = ExoLogger.getLogger(TypeCongeRepositoryImpl.class);

    /**
     * Crée un nouveau type de congé.
     *
     * @param typeConge l'entité à persister (doit avoir un ID UUID généré)
     * @return l'entité persistée avec ID confirmé
     * @throws RuntimeException en cas d'erreur SQL
     */
    @Override
    public TypeConge save(TypeConge typeConge) {
        String sql = SqlQueries.INSERT_TYPE_CONGE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeConge.getId());
            pstmt.setString(2, typeConge.getCode());
            pstmt.setString(3, typeConge.getLibelle());
            pstmt.setString(3, typeConge.getDescription());
            pstmt.setInt(5, typeConge.getJoursMaxParAn());
            pstmt.setBoolean(6, typeConge.isDeductionSolde());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Échec insertion type congé");
            }
            LOG.info("Type de congé créé : ID={}, Libellé={}", typeConge.getId(), typeConge.getLibelle());
            return typeConge;

        } catch (SQLException e) {
            LOG.error("Erreur lors de la création du type de congé : {}", e.getMessage(), e);
            throw new RuntimeException("Échec création type congé", e);
        }
    }

    /**
     * Met à jour un type de congé existant.
     *
     * @param typeConge l'entité mise à jour
     * @throws RuntimeException si la mise à jour échoue ou si non trouvé
     */
    @Override
    public void update(TypeConge typeConge) {
        String sql = SqlQueries.UPDATE_TYPE_CONGE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeConge.getCode());
            pstmt.setString(2, typeConge.getLibelle());
            pstmt.setString(3, typeConge.getDescription());
            pstmt.setInt(4, typeConge.getJoursMaxParAn());
            pstmt.setBoolean(5, typeConge.isDeductionSolde());
            pstmt.setString(6, typeConge.getId());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Type de congé non trouvé ou mise à jour impossible");
            }
            LOG.info("Type de congé mis à jour : ID={}, Libellé={}", typeConge.getId(), typeConge.getLibelle());

        } catch (SQLException e) {
            LOG.error("Erreur mise à jour type congé ID={} : {}", typeConge.getId(), e.getMessage(), e);
            throw new RuntimeException("Échec mise à jour type congé", e);
        }
    }

    /**
     * Récupère un type de congé par son ID.
     *
     * @param id l'UUID du type de congé
     * @return l'entité ou null si non trouvé
     */
    @Override
    public TypeConge findById(String id) {
        String sql = SqlQueries.SELECT_TYPE_CONGE_BY_ID;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    TypeConge typeConge = new TypeConge();
                    typeConge.setId(rs.getString("id"));
                    typeConge.setCode(rs.getString("code"));
                    typeConge.setLibelle(rs.getString("libelle"));
                    typeConge.setDescription(rs.getString("description"));
                    typeConge.setJoursMaxParAn(rs.getInt("jours_max_par_an"));
                    typeConge.setDeductionSolde(rs.getBoolean("deduction_solde"));
                    return typeConge;
                }
            }
            return null;
        } catch (SQLException e) {
            LOG.error("Erreur récupération type congé ID={} : {}", id, e.getMessage(), e);
            throw new RuntimeException("Échec récupération type congé", e);
        }
    }

    /**
     * Récupère la liste complète des types de congé (triés par libellé).
     *
     * @return liste non nulle (vide si aucun type)
     */
    @Override
    public List<TypeConge> findAll() {
        String sql = SqlQueries.SELECT_ALL_TYPES_CONGE;
        List<TypeConge> typeConges = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TypeConge typeConge = new TypeConge();
                typeConge.setId(rs.getString("id"));
                typeConge.setCode(rs.getString("code"));
                typeConge.setLibelle(rs.getString("libelle"));
                typeConge.setDescription(rs.getString("description"));
                typeConge.setJoursMaxParAn(rs.getInt("jours_max_par_an"));
                typeConge.setDeductionSolde(rs.getBoolean("deduction_solde"));
                typeConges.add(typeConge);
            }

            LOG.debug("Récupérés {} types de congé", typeConges.size());
            return typeConges;
        } catch (Exception e) {
            LOG.error("Erreur récupération tous types congé : {}", e.getMessage(), e);
            throw new RuntimeException("Échec récupération types congé", e);
        }
    }

    /**
     * Supprime un type de congé (uniquement si non utilisé).
     *
     * @param id l'UUID du type à supprimer
     * @throws RuntimeException si suppression impossible (utilisé ou non trouvé)
     */
    @Override
    public void deleteById(String id) {
        if (isTypeUsed(id)) {
            throw new IllegalStateException(
                    "Impossible de supprimer : type de congé utilisé dans au moins une demande");
        }

        String sql = SqlQueries.DELETE_TYPE_CONGE;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            int rows = pstmt.executeUpdate();
            if (rows == 0) {
                throw new IllegalArgumentException("Type de congé non trouvé : " + id);
            }
            LOG.info("Type de congé supprimé : ID={}", id);
        } catch (SQLException e) {
            LOG.error("Erreur suppression type congé ID={} : {}", id, e.getMessage(), e);
            throw new RuntimeException("Échec suppression type congé", e);
        }
    }

    /**
     * Vérifie si un type de congé est référencé dans au moins une demande.
     *
     * @param typeCongeId UUID du type
     * @return true si utilisé, false sinon
     */
    @Override
    public boolean isTypeUsed(String typeCongeId) {
        String sql = SqlQueries.SELECT_TYPE_CONGE_USED;
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, typeCongeId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
            return false;
        } catch (SQLException e) {
            LOG.error("Erreur vérification usage type congé ID={} : {}", typeCongeId, e.getMessage(), e);
            throw new RuntimeException("Échec vérification usage type congé", e);
        }
    }
}
