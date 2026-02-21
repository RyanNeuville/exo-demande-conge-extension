package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.TypeConge;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper pour transformer les données de la base de données (ResultSet)
 * en objets de type TypeConge.
 */
public class TypeCongeMapper {

    private TypeCongeMapper() {
        // Constructeur privé pour empêcher l'instanciation
    }

    /**
     * Convertit une ligne d'un ResultSet en une instance de TypeConge.
     * 
     * @param rs le ResultSet positionné sur la ligne à mapper
     * @return une instance de TypeConge
     * @throws SQLException en cas d'erreur de lecture du ResultSet
     */
    public static TypeConge fromResultSet(ResultSet rs) throws SQLException {
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
