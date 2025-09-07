package com.codexmaker.services.model.entity;

import com.codexmaker.services.model.enums.Statut;
import com.codexmaker.services.model.enums.TypeConge;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.exoplatform.commons.api.persistence.ExoEntity;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Entité représentant une demande de congé.
 * Cette classe est mappée à la table "demande_conge" dans la base de données.
 * Elle contient des informations sur la demande de congé, y compris l'utilisateur,
 * les dates, le type de congé, le statut, et d'autres détails pertinents.
 */

@Entity(name = "DemandeConge")
@Table(name = "demande_conge")
@ExoEntity
public class DemandeConge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "demande_id", unique = true)
    private String demandeId; // Génére un UUID ou similaire lors de la création

    @Column(name = "user_id")
    private String userId;

    @Column(name = "nom")
    private String nom;

    @Column(name = "prenom")
    private String prenom;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_debut")
    private LocalDate dateDebut;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_fin")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "type_conge")
    private TypeConge typeConge;

    @Enumerated(EnumType.STRING)
    @JsonSerialize(using = ToStringSerializer.class)
    @Column(name = "statut")
    private Statut statut;

    @Column(name = "motif")
    private String motif;

    @Column(name = "commentaires_manager")
    private String commentairesManager;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_soumission")
    private LocalDate dateSoumission;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "date_modification")
    private LocalDate dateModification;

    @Column(name = "solde_demande")
    private int soldeDemande;

    @Column(name = "duree_jours_ouvres")
    private int dureeJoursOuvres;

    public DemandeConge() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getDemandeId() { return demandeId; }
    public void setDemandeId(String demandeId) { this.demandeId = demandeId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public TypeConge getTypeConge() { return typeConge; }
    public void setTypeConge(TypeConge typeConge) { this.typeConge = typeConge; }
    public Statut getStatut() { return statut; }
    public void setStatut(Statut statut) { this.statut = statut; }
    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }
    public String getCommentairesManager() { return commentairesManager; }
    public void setCommentairesManager(String commentairesManager) { this.commentairesManager = commentairesManager; }
    public LocalDate getDateSoumission() { return dateSoumission; }
    public void setDateSoumission(LocalDate dateSoumission) { this.dateSoumission = dateSoumission; }
    public LocalDate getDateModification() { return dateModification; }
    public void setDateModification(LocalDate dateModification) { this.dateModification = dateModification; }
    public int getSoldeDemande() { return soldeDemande; }
    public void setSoldeDemande(int soldeDemande) { this.soldeDemande = soldeDemande; }
    public int getDureeJoursOuvres() { return dureeJoursOuvres; }
    public void setDureeJoursOuvres(int dureeJoursOuvres) { this.dureeJoursOuvres = dureeJoursOuvres; }

    public void calculerDuree() {
        if (dateDebut != null && dateFin != null) {
            this.dureeJoursOuvres = (int) ChronoUnit.DAYS.between(dateDebut, dateFin) + 1; // Adapte pour jours ouvrés réels
        }
    }

    @Override
    public String toString() {
        return "DemandeConge{" +
                "id=" + id +
                ", demandeId='" + demandeId + '\'' +
                ", userId='" + userId + '\'' +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", typeConge=" + typeConge +
                ", statut=" + statut +
                ", motif='" + motif + '\'' +
                ", commentairesManager='" + commentairesManager + '\'' +
                ", dateSoumission=" + dateSoumission +
                ", dateModification=" + dateModification +
                ", soldeDemande=" + soldeDemande +
                ", dureeJoursOuvres=" + dureeJoursOuvres +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DemandeConge that = (DemandeConge) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void calculerSolde() {
        int soldeInitial = 30; // Fictif, remplace par une requête à un service RH
        this.soldeDemande = soldeInitial - this.dureeJoursOuvres;
    }
}