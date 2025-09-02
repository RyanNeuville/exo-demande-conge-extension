package com.codexmaker.services.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Statut {
    @JsonProperty("EN_ATTENTE")
    EN_ATTENTE,
    @JsonProperty("APPROUVEE")
    APPROUVEE,
    @JsonProperty("REFUSEE")
    REFUSEE,
    @JsonProperty("ANNULEE")
    ANNULEE
}