package com.codexmaker.services.utils;

import com.codexmaker.services.model.dto.DemandeCongeDTO;
import com.codexmaker.services.model.entity.DemandeConge;

public class Util {
    /**
     * Méthode utilitaire pour mapper une entité DemandeConge vers un DTO DemandeCongeDTO.
     *
     * @param demande l'entité DemandeConge à mapper
     * @return le DTO DemandeCongeDTO correspondant
     */
    public static DemandeCongeDTO mapToDto(DemandeConge demande) {
        DemandeCongeDTO dto = new DemandeCongeDTO();
        dto.setId(demande.getId());
        dto.setUserId(String.valueOf(demande.getUtilisateur().getId()));
        dto.setNom(demande.getUtilisateur().getNom() + " " + demande.getUtilisateur().getPrenom());
        dto.setDateDebut(demande.getDateDebut());
        dto.setDateFin(demande.getDateFin());
        dto.setTypeConge(demande.getTypeConge());
        dto.setStatut(demande.getStatut());
        dto.setMotif(demande.getMotif());
        dto.setDateSoumission(demande.getDateSoumission());
        dto.setDateModification(demande.getDateModification());
        dto.setDureeJoursOuvres(demande.getDureeJoursOuvres());
        return dto;
    }

    /**
     * Méthode utilitaire pour mapper un DTO DemandeCongeDTO vers une entité DemandeConge.
     *
     * @param dto le DTO DemandeCongeDTO à mapper
     * @return l'entité DemandeConge correspondante
     */
    private DemandeConge convertToEntity(DemandeCongeDTO dto) {
        DemandeConge entity = new DemandeConge();
        entity.setId(dto.getId());
        entity.setDateDebut(dto.getDateDebut());
        entity.setDateFin(dto.getDateFin());
        entity.setTypeConge(dto.getTypeConge());
        entity.setMotif(dto.getMotif());
        return entity;
    }
}
