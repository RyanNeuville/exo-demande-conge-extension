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

    @JsonProperty("quota")
    private int quota;

    @JsonProperty("usedDays")
    private int usedDays;

    public UserDemandes() {
    }

    public UserDemandes(String userName, String fullName, List<DemandeConge> demandes) {
        this.userName = userName;
        this.fullName = fullName;
        this.demandes = demandes;
    }

    public UserDemandes(String userName, String fullName, List<DemandeConge> demandes, int quota, int usedDays){
        this.userName = userName;
        this.fullName = fullName;
        this.demandes = demandes;
        this.quota = quota;
        this.usedDays = usedDays;
    }

    /** Getters et setters */
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

    public int getQuota() {
        return quota;
    }

    public void setQuota(int quota) {
        this.quota = quota;
    }

    public int getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(int usedDays) {
        this.usedDays = usedDays;
    }
}