package com.codexmaker.services.rest.service;

import com.codexmaker.services.rest.model.entity.TypeConge;
import java.util.List;

/**
 * Interface définissant la couche de service métier pour la gestion des types
 * de congés.
 */
public interface TypeCongeService {
    TypeConge creerTypeConge(TypeConge typeConge);

    void modifierTypeConge(TypeConge typeConge);

    TypeConge getTypeConge(String id);

    List<TypeConge> getTousLesTypesConges();

    void supprimerTypeConge(String id);
}
