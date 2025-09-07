package com.codexmaker.services.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.List;

public class UserDemandes implements Serializable {
    @JsonProperty("userName")
    private String userName;

    @JsonProperty("fullName")
    private String fullName;

    @JsonProperty("demandes")
    private List<DemandeConge> demandes;

    public UserDemandes() {
    }

    public UserDemandes(String userName, String fullName, List<DemandeConge> demandes) {
        this.userName = userName;
        this.fullName = fullName;
        this.demandes = demandes;
    }

    // Getters and setters
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<DemandeConge> getDemandes() {
        return demandes;
    }

    public void setDemandes(List<DemandeConge> demandes) {
        this.demandes = demandes;
    }
}