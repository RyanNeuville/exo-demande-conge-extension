package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.repository.TypeCongeRepository;
import com.codexmaker.services.rest.repository.impl.TypeCongeRepositoryImpl;
import com.codexmaker.services.rest.service.TypeCongeService;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service métier pour les types de congés.
 */
public class TypeCongeServiceImpl implements TypeCongeService {

    private static final Log LOG = ExoLogger.getLogger(TypeCongeServiceImpl.class);
    private final TypeCongeRepository typeCongeRepository;

    public TypeCongeServiceImpl() {
        this.typeCongeRepository = new TypeCongeRepositoryImpl();
    }

    @Override
    public TypeConge creerTypeConge(TypeConge typeConge) {
        if (typeConge.getId() == null || typeConge.getId().isEmpty()) {
            typeConge.setId(UUID.randomUUID().toString());
        }
        return typeCongeRepository.save(typeConge);
    }

    @Override
    public void modifierTypeConge(TypeConge typeConge) {
        if (typeConge == null || typeConge.getId() == null) {
            throw new IllegalArgumentException("Le type de congé à modifier doit avoir un ID");
        }
        typeCongeRepository.update(typeConge);
    }

    @Override
    public TypeConge getTypeConge(String id) {
        return typeCongeRepository.findById(id);
    }

    @Override
    public List<TypeConge> getTousLesTypesConges() {
        return typeCongeRepository.findAll();
    }

    @Override
    public void supprimerTypeConge(String id) {
        if (typeCongeRepository.isTypeUsed(id)) {
            LOG.warn("Impossible de supprimer le type de congé ID={} car il est lié à des demandes", id);
            throw new RuntimeException(
                    "Ce type de congé ne peut pas être supprimé car il est utilisé dans des demandes existantes.");
        }
        typeCongeRepository.deleteById(id);
    }
}
