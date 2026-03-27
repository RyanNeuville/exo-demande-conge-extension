package com.codexmaker.services.rest.api;

import com.codexmaker.services.rest.dto.DemandeCongeDTO;
import com.codexmaker.services.rest.dto.SoldeResponseDTO;
import com.codexmaker.services.rest.mapper.DemandeCongeMapper;
import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.service.DemandeCongeService;
import com.codexmaker.services.rest.service.TypeCongeService;
import com.codexmaker.services.rest.service.UtilisateurService;
import com.codexmaker.services.rest.service.impl.UtilisateurServiceImpl;
import com.codexmaker.services.rest.utils.Constants;
import org.exoplatform.container.ExoContainerContext;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.services.security.ConversationState;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.util.List;

/**
 * Service REST pour la gestion complète des demandes de congés, types et
 * utilisateurs.
 * Ce service expose les endpoints nécessaires pour les employés, les
 * responsables et les administrateurs.
 */
@Path(Constants.API_BASE)
@Produces(MediaType.APPLICATION_JSON)
public class DemandeCongeRestService implements ResourceContainer {

    private final DemandeCongeService demandeCongeService;
    private final TypeCongeService typeCongeService;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur par défaut initialisant les services métier via le conteneur
     * eXo.
     */
    public DemandeCongeRestService() {
        this.demandeCongeService = ExoContainerContext.getService(DemandeCongeService.class);
        this.typeCongeService = ExoContainerContext.getService(TypeCongeService.class);

        /**
         * Récupération du service utilisateur.
         * Si non enregistré dans le contexte eXo, une implémentation par défaut est
         * utilisée.
         */
        UtilisateurService us = ExoContainerContext.getService(UtilisateurService.class);
        this.utilisateurService = (us != null) ? us : new UtilisateurServiceImpl();
    }

    /**
     * Récupère l'identifiant de l'utilisateur actuellement authentifié.
     * 
     * @return l'identifiant de l'utilisateur ou "anonymous" si non authentifié.
     */
    private String getAuthenticatedUserId() {
        ConversationState state = ConversationState.getCurrent();
        return (state != null && state.getIdentity() != null) ? state.getIdentity().getUserId() : "anonymous";
    }

    /**
     * Synchronise l'utilisateur eXo avec la base de données locale.
     * Mappe les groupes eXo vers les rôles applicatifs.
     */
    private Utilisateur syncUserWithExo() {
        ConversationState state = ConversationState.getCurrent();
        if (state == null || state.getIdentity() == null) {
            System.out.println("[DemandeConge] No ConversationState found.");
            return null;
        }

        String userId = state.getIdentity().getUserId();
        java.util.Collection<String> roles = state.getIdentity().getRoles();
        
        System.out.println("[DemandeConge] Synchronizing user: " + userId + " with roles: " + roles);

        // Détermination du rôle
        com.codexmaker.services.rest.model.enums.Role appRole = com.codexmaker.services.rest.model.enums.Role.EMPLOYE;
        
        // Robust root check (case insensitive, or contains root)
        boolean isRoot = "root".equalsIgnoreCase(userId) || (userId != null && userId.toLowerCase().contains("root"));
        boolean isAdminGroup = roles != null && roles.stream().anyMatch(r -> r.toLowerCase().contains("administrators") || r.toLowerCase().contains("admin"));

        if (isRoot || isAdminGroup) {
            appRole = com.codexmaker.services.rest.model.enums.Role.ADMINISTRATEUR;
        } else if (roles != null && roles.stream().anyMatch(r -> r.toLowerCase().contains("manager") || r.toLowerCase().contains("responsable"))) {
            appRole = com.codexmaker.services.rest.model.enums.Role.RESPONSABLE;
        }

        System.out.println("[DemandeConge] Mapped role for " + userId + ": " + appRole);

        Utilisateur existing = utilisateurService.getUtilisateur(userId);
        if (existing == null) {
            System.out.println("[DemandeConge] User " + userId + " not in DB. Returning transient profile.");
            // Création automatique en mémoire seulement (sera persisté lors de la 1ère demande)
            Utilisateur newUser;
            if (appRole == com.codexmaker.services.rest.model.enums.Role.ADMINISTRATEUR) {
                newUser = new com.codexmaker.services.rest.model.entity.Administrateur();
            } else if (appRole == com.codexmaker.services.rest.model.enums.Role.RESPONSABLE) {
                newUser = new com.codexmaker.services.rest.model.entity.Responsable();
            } else {
                newUser = new com.codexmaker.services.rest.model.entity.Employe();
            }
            
            newUser.setId(userId);
            newUser.setUsername(userId);
            newUser.setNom(userId);
            newUser.setEmail(userId + "@kozao.africa");
            newUser.setRole(appRole);
            newUser.setSoldeConges(Constants.SOLDE_INITIAL_PAR_DEFAUT);
            
            return newUser;
        } else {
            // Mise à jour du rôle si nécessaire
            if (existing.getRole() != appRole) {
                System.out.println("[DemandeConge] Updating role for " + userId + " to " + appRole);
                existing.setRole(appRole);
                return utilisateurService.synchroniserUtilisateur(existing);
            }
            return existing;
        }
    }

    /**
     * Récupère la liste des demandes de l'utilisateur connecté.
     * 
     * @return Réponse HTTP contenant la liste des demandes.
     */
    @GET
    @Path(Constants.API_DEMANDES_ME)
    @RolesAllowed("users")
    public Response getMyDemandes() {
        syncUserWithExo(); // Sync before fetch
        List<DemandeConge> demandes = demandeCongeService.getDemandesParUtilisateur(getAuthenticatedUserId());
        return Response.ok(demandes.stream()
                .map(DemandeCongeMapper::toResponseDTO)
                .toList()).build();
    }

    /**
     * Soumet une nouvelle demande de congé.
     * 
     * @param dto Les informations de la demande.
     * @return Réponse HTTP avec l'objet créé.
     */
    @POST
    @Path(Constants.API_DEMANDES)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public Response submitDemande(DemandeCongeDTO dto) {
        DemandeConge demande = DemandeCongeMapper.toEntity(dto);
        DemandeConge saved = demandeCongeService.soumettreDemande(demande, getAuthenticatedUserId());

        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    /**
     * Récupère une demande par son identifiant unique.
     * 
     * @param id L'identifiant de la demande.
     * @return Réponse HTTP contenant la demande ou 404 si non trouvée.
     */
    @GET
    @Path(Constants.API_DEMANDES_BY_ID)
    @RolesAllowed("users")
    public Response getDemandeById(@PathParam("id") String id) {
        DemandeConge demande = demandeCongeService.getDemande(id);
        if (demande == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(DemandeCongeMapper.toResponseDTO(demande)).build();
    }

    /**
     * Modifie une demande existante (uniquement si elle est en attente).
     * 
     * @param id  L'identifiant de la demande à modifier.
     * @param dto Les nouvelles informations.
     * @return Réponse HTTP de succès.
     */
    @PUT
    @Path(Constants.API_DEMANDES_BY_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public Response updateDemande(@PathParam("id") String id, DemandeCongeDTO dto) {
        DemandeConge demande = DemandeCongeMapper.toEntity(dto);
        demande.setId(id);
        demandeCongeService.modifierDemandeEnAttente(demande, getAuthenticatedUserId());
        return Response.ok().build();
    }

    /**
     * Annule une demande de congé.
     * 
     * @param id L'identifiant de la demande.
     * @return Réponse HTTP de succès.
     */
    @DELETE
    @Path(Constants.API_DEMANDES_BY_ID)
    @RolesAllowed("users")
    public Response annulerDemande(@PathParam("id") String id) {
        demandeCongeService.annulerDemande(id);
        return Response.ok().build();
    }

    /**
     * Récupère l'historique des changements d'état d'une demande.
     * 
     * @param id L'identifiant de la demande.
     * @return Réponse HTTP contenant l'historique.
     */
    @GET
    @Path(Constants.API_DEMANDES_HISTORIQUE)
    @RolesAllowed("users")
    public Response getHistoriqueDemande(@PathParam("id") String id) {
        DemandeConge demande = demandeCongeService.getDemande(id);
        if (demande == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(demande.getHistorique()).build();
    }

    /**
     * Récupère la liste des demandes à traiter par le manager connecté.
     * 
     * @return Réponse HTTP contenant les demandes en attente.
     */
    @GET
    @Path(Constants.API_DEMANDES_A_TRAITER)
    @RolesAllowed("users")
    public Response getDemandesATraiter() {
        List<DemandeConge> demandes = demandeCongeService.getDemandesATraiter(getAuthenticatedUserId());
        return Response.ok(demandes.stream()
                .map(DemandeCongeMapper::toResponseDTO)
                .toList()).build();
    }

    /**
     * Valide une demande de congé.
     * 
     * @param id          L'identifiant de la demande.
     * @param commentaire Un commentaire optionnel du valideur.
     * @return Réponse HTTP de succès.
     */
    @POST
    @Path(Constants.API_DEMANDES_VALIDER)
    @RolesAllowed("users")
    public Response validerDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.validerDemande(id, commentaire);
        return Response.ok().build();
    }

    /**
     * Refuse une demande de congé.
     * 
     * @param id          L'identifiant de la demande.
     * @param commentaire Le motif du refus.
     * @return Réponse HTTP de succès.
     */
    @POST
    @Path(Constants.API_DEMANDES_REFUSER)
    @RolesAllowed("users")
    public Response refuserDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.refuserDemande(id, commentaire);
        return Response.ok().build();
    }

    /**
     * Récupère l'intégralité des demandes de congés (réservé aux administrateurs).
     * 
     * @return Réponse HTTP contenant toutes les demandes.
     */
    @GET
    @Path(Constants.API_DEMANDES_TOUTES)
    @RolesAllowed("administrators")
    public Response getToutesLesDemandes() {
        List<DemandeConge> toutes = demandeCongeService.getToutesLesDemandes();
        return Response.ok(toutes.stream()
                .map(DemandeCongeMapper::toResponseDTO)
                .toList()).build();
    }

    /**
     * Exporte un rapport des congés sous format CSV.
     * 
     * @param format Le format d'exportation (ex: 'CSV').
     * @param debut  Date de début de la période au format ISO.
     * @param fin    Date de fin de la période au format ISO.
     * @return Réponse HTTP contenant le fichier généré.
     */
    @GET
    @Path(Constants.API_DEMANDES_EXPORTER)
    @RolesAllowed("administrators")
    public Response exportRapports(@QueryParam("format") String format,
            @QueryParam("debut") String debut,
            @QueryParam("fin") String fin) {
        LocalDate start = (debut != null) ? LocalDate.parse(debut) : null;
        LocalDate end = (fin != null) ? LocalDate.parse(fin) : null;
        String csv = demandeCongeService.exporterRapports(format, start, end);
        return Response.ok(csv, "text/csv")
                .header("Content-Disposition", "attachment; filename=\"rapport_conges.csv\"")
                .build();
    }

    /**
     * Récupère les informations de profil de l'utilisateur connecté.
     * 
     * @return Réponse HTTP contenant l'utilisateur.
     */
    @GET
    @Path(Constants.API_UTILISATEUR_ME)
    @RolesAllowed("users")
    public Response getMe() {
        Utilisateur u = syncUserWithExo();
        if (u == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(u).build();
    }

    /**
     * Récupère le solde de congés actuel de l'utilisateur connecté.
     * Gère gracieusement le cas où l'utilisateur n'a pas encore fait de demande (et n'est pas en BDD).
     * 
     * @return Réponse HTTP contenant le solde.
     */
    @GET
    @Path(Constants.API_UTILISATEUR_ME_SOLDE)
    @RolesAllowed("users")
    public Response getMySolde() {
        try {
            double solde = utilisateurService.consulterSolde(getAuthenticatedUserId());
            return Response.ok(new SoldeResponseDTO(solde)).build();
        } catch (Exception e) {
            // L'utilisateur n'est pas encore en BDD (transitoire), il a le solde max
            System.out.println("[DemandeConge] Solde introuvable (utilisateur transitoire), renvoi du defaut.");
            return Response.ok(new SoldeResponseDTO((double) Constants.SOLDE_INITIAL_PAR_DEFAUT)).build();
        }
    }

    /**
     * Liste tous les utilisateurs ayant le rôle responsable (pour assigner un
     * valideur).
     * 
     * @return Réponse HTTP contenant la liste des responsables.
     */
    @GET
    @Path(Constants.API_UTILISATEURS_RESPONSABLES)
    @RolesAllowed("users")
    public Response getResponsables() {
        return Response.ok(utilisateurService.getTousLesResponsables()).build();
    }

    /**
     * Liste tous les types de congés paramétrés.
     * 
     * @return Réponse HTTP contenant la liste des types de congés.
     */
    @GET
    @Path(Constants.API_TYPES_CONGES)
    public Response getTypesConges() {
        List<TypeConge> types = typeCongeService.getTousLesTypesConges();
        return Response.ok(types).build();
    }

    /**
     * Crée un nouveau type de congé.
     * 
     * @param type Les informations du nouveau type.
     * @return Réponse HTTP avec l'objet créé.
     */
    @POST
    @Path(Constants.API_TYPES_CONGES)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrators")
    public Response createTypeConge(TypeConge type) {
        TypeConge created = typeCongeService.creerTypeConge(type);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    /**
     * Modifie un type de congé existant.
     * 
     * @param id   L'identifiant du type à modifier.
     * @param type Les nouvelles informations.
     * @return Réponse HTTP de succès.
     */
    @PUT
    @Path(Constants.API_TYPES_CONGES_BY_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrators")
    public Response updateTypeConge(@PathParam("id") String id, TypeConge type) {
        type.setId(id);
        typeCongeService.modifierTypeConge(type);
        return Response.ok().build();
    }

    /**
     * Supprime un type de congé par son identifiant.
     * 
     * @param id L'identifiant à supprimer.
     * @return Réponse HTTP de succès.
     */
    @DELETE
    @Path(Constants.API_TYPES_CONGES_BY_ID)
    @RolesAllowed("administrators")
    public Response deleteTypeConge(@PathParam("id") String id) {
        typeCongeService.supprimerTypeConge(id);
        return Response.ok().build();
    }
}