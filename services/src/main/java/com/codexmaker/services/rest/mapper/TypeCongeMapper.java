package com.codexmaker.services.rest.mapper;

import com.codexmaker.services.rest.model.entity.TypeConge;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Mapper dédié à la transformation des lignes SQL en entités TypeConge.
 */
public class TypeCongeMapper {

    private TypeCongeMapper() {
        /** Constructeur privé pour empêcher l'instanciation. */
    }

    /**
     * Map un ResultSet vers un objet TypeConge.
     * 
     * @param rs Le ResultSet SQL.
     * @return L'instance TypeConge correspondante.
     * @throws SQLException En cas d'erreur JDBC.
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
