package com.codexmaker.services.api;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.enums.Statut;
import org.exoplatform.commons.persistence.impl.GenericDAOJPAImpl;

import jakarta.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

public class DemandeCongeDAO extends GenericDAOJPAImpl<DemandeConge, Integer> {

    public List<DemandeConge> findByUserId(String userId) {
        TypedQuery<DemandeConge> query = getEntityManager().createQuery(
                "SELECT d FROM DemandeConge d WHERE d.userId = :userId ORDER BY d.dateSoumission DESC",
                DemandeConge.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }

    public DemandeConge findByDemandeId(String demandeId) {
        TypedQuery<DemandeConge> query = getEntityManager().createQuery(
                "SELECT d FROM DemandeConge d WHERE d.demandeId = :demandeId",
                DemandeConge.class);
        query.setParameter("demandeId", demandeId);
        List<DemandeConge> results = query.getResultList();
        return results.isEmpty() ? null : results.getFirst();
    }

    public List<DemandeConge> findAllWithFilters(Statut statut, LocalDate dateDebut, LocalDate dateFin) {
        StringBuilder jpql = new StringBuilder("SELECT d FROM DemandeConge d WHERE 1=1");
        if (statut != null) {
            jpql.append(" AND d.statut = :statut");
        }
        if (dateDebut != null) {
            jpql.append(" AND d.dateDebut >= :dateDebut");
        }
        if (dateFin != null) {
            jpql.append(" AND d.dateFin <= :dateFin");
        }
        jpql.append(" ORDER BY d.dateSoumission DESC");

        TypedQuery<DemandeConge> query = getEntityManager().createQuery(jpql.toString(), DemandeConge.class);
        if (statut != null) query.setParameter("statut", statut);
        if (dateDebut != null) query.setParameter("dateDebut", dateDebut);
        if (dateFin != null) query.setParameter("dateFin", dateFin);

        return query.getResultList();
    }
}