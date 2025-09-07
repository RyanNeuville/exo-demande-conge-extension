package com.codexmaker.services.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;

public class DemandeConge implements Serializable {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("dateDebut")
    private String dateDebut;

    @JsonProperty("dateFin")
    private String dateFin;

    @JsonProperty("typeConge")
    private String typeConge;

    @JsonProperty("motif")
    private String motif;

    @JsonProperty("status")
    private String status;

    @JsonProperty("soumisLe")
    private String soumisLe;

    public DemandeConge() {
    }

    public DemandeConge(Long id, String dateDebut, String dateFin, String typeConge, String motif, String status, String soumisLe) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.typeConge = typeConge;
        this.motif = motif;
        this.status = status;
        this.soumisLe = soumisLe;
    }

    /** Getters et setters */
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut;
    }

    public String getDateFin() {
        return dateFin;
    }

    public void setDateFin(String dateFin) {
        this.dateFin = dateFin;
    }

    public String getTypeConge() {
        return typeConge;
    }

    public void setTypeConge(String typeConge) {
        this.typeConge = typeConge;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSoumisLe() {
        return soumisLe;
    }

    public void setSoumisLe(String soumisLe) {
        this.soumisLe = soumisLe;
    }
}