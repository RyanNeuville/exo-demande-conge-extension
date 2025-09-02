package com.codexmaker.services.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * TypeConge représente un type de congé dans le système.
 * Il contient un identifiant unique, un libellé et une description.
 */
public enum TypeConge {
    @JsonProperty("CONGE_ANNUEL")
    CONGE_ANNUEL,
    @JsonProperty("CONGE_MALADIE")
    CONGE_MALADIE,
    @JsonProperty("CONGE_MATERNITE")
    CONGE_MATERNITE,
    @JsonProperty("CONGE_PATERNITE")
    CONGE_PATERNITE,
    @JsonProperty("CONGE_REPOS")
    CONGE_REPOS,
    @JsonProperty("CONGE_VACANCES")
    CONGE_VACANCES,
    @JsonProperty("CONGE_FORMATION")
    CONGE_FORMATION,
    @JsonProperty("AUTRE_CONGE")
    AUTRE_CONGE;
}