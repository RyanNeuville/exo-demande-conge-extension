package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.Administrateur;
import com.codexmaker.services.rest.model.entity.Employe;
import com.codexmaker.services.rest.model.entity.Responsable;
import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.model.enums.Role;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour transformer les données de la base de données (ResultSet)
 * en objets de type Utilisateur (Employe, Responsable, Administrateur).
 */
public class UtilisateurMapper {

    private UtilisateurMapper() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Convertit une ligne d'un ResultSet en une instance concrète de Utilisateur.
     * Le type d'objet retourné (Employe, Responsable, Administrateur) dépend du
     * rôle.
     * 
     * @param rs le ResultSet positionné sur la ligne à mapper
     * @return une instance de Utilisateur (ou ses sous-types)
     * @throws SQLException en cas d'erreur de lecture du ResultSet
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
        utilisateur.setSoldeConges(rs.getInt("solde_conges"));

        return utilisateur;
    }
}
