package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.Administrateur;
import com.codexmaker.services.rest.model.entity.Employe;
import com.codexmaker.services.rest.model.entity.Responsable;
import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.model.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour transformer les données de la base de données vers le modèle
 * objet Utilisateur.
 * Gère la spécialisation des instances (Employe, Responsable, Administrateur)
 * en fonction du rôle.
 */
public class UtilisateurMapper {

    private UtilisateurMapper() {
        /** Constructeur privé pour empêcher l'instanciation. */
    }

    /**
     * Instancie et peuple un objet Utilisateur à partir d'un ResultSet.
     * Le type concret de l'instance retournée est déterminé par la colonne 'role'.
     * 
     * @param rs Le ResultSet positionné sur la ligne client.
     * @return Une instance concrète de Utilisateur.
     * @throws SQLException En cas de problème d'accès aux données SQL.
     */
    public static Utilisateur fromResultSet(ResultSet rs) throws SQLException {
        Role role = Role.valueOf(rs.getString("role"));
        Utilisateur utilisateur;

        switch (role) {
            case EMPLOYE:
                utilisateur = new Employe();
                break;
            case RESPONSABLE:
                utilisateur = new Responsable();
                break;
            case ADMINISTRATEUR:
                utilisateur = new Administrateur();
                break;
            default:
                throw new IllegalArgumentException("Rôle inconnu : " + role);
        }

        utilisateur.setId(rs.getString("id"));
        utilisateur.setNom(rs.getString("nom"));
        utilisateur.setPrenom(rs.getString("prenom"));
        utilisateur.setUsername(rs.getString("username"));
        utilisateur.setEmail(rs.getString("email"));
        utilisateur.setRole(role);
        utilisateur.setSoldeConges(rs.getDouble("solde_conges"));

        return utilisateur;
    }
}
