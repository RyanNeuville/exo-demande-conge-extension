package com.codexmaker.services.rest.api;

import com.codexmaker.services.rest.dto.DemandeCongeDTO;
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
 */
@Path(Constants.API_BASE)
@Produces(MediaType.APPLICATION_JSON)
public class DemandeCongeRestService implements ResourceContainer {

    private final DemandeCongeService demandeCongeService;
    private final TypeCongeService typeCongeService;
    private final UtilisateurService utilisateurService;

    public DemandeCongeRestService() {
        this.demandeCongeService = ExoContainerContext.getService(DemandeCongeService.class);
        this.typeCongeService = ExoContainerContext.getService(TypeCongeService.class);
        /**
         * UtilisateurService might not be an eXo Service if not registered in
         * configuration.xml,
         * using impl directly if not available in context.
         */
        UtilisateurService us = ExoContainerContext.getService(UtilisateurService.class);
        this.utilisateurService = (us != null) ? us : new UtilisateurServiceImpl();
    }

    private String getAuthenticatedUserId() {
        ConversationState state = ConversationState.getCurrent();
        return (state != null && state.getIdentity() != null) ? state.getIdentity().getUserId() : "anonymous";
    }

    /**
     * GESTION DES DEMANDES (Base & Employé)
     */

    @GET
    @Path(Constants.API_DEMANDES_ME)
    @RolesAllowed("users")
    public Response getMyDemandes() {
        List<DemandeConge> demandes = demandeCongeService.getDemandesParUtilisateur(getAuthenticatedUserId());
        return Response.ok(demandes.stream()
                .map(DemandeCongeMapper::toResponseDTO)
                .toList()).build();
    }

    @POST
    @Path(Constants.API_DEMANDES)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public Response submitDemande(DemandeCongeDTO dto) {
        DemandeConge demande = DemandeCongeMapper.toEntity(dto);
        DemandeConge saved = demandeCongeService.soumettreDemande(demande, getAuthenticatedUserId());

        return Response.status(Response.Status.CREATED).entity(saved).build();
    }

    @GET
    @Path(Constants.API_DEMANDES_BY_ID)
    @RolesAllowed("users")
    public Response getDemandeById(@PathParam("id") String id) {
        DemandeConge demande = demandeCongeService.getDemande(id);
        if (demande == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(DemandeCongeMapper.toResponseDTO(demande)).build();
    }

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

    @DELETE
    @Path(Constants.API_DEMANDES_BY_ID)
    @RolesAllowed("users")
    public Response annulerDemande(@PathParam("id") String id) {
        demandeCongeService.annulerDemande(id);
        return Response.ok().build();
    }

    @GET
    @Path(Constants.API_DEMANDES_HISTORIQUE)
    @RolesAllowed("users")
    public Response getHistoriqueDemande(@PathParam("id") String id) {
        /**
         * Logique déléguée au service (qui récupère l'historique depuis le repo)
         */
        DemandeConge demande = demandeCongeService.getDemande(id);
        if (demande == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(demande.getHistorique()).build();
    }

    /**
     * GESTION MANAGÉRIALE (Responsable)
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

    @POST
    @Path(Constants.API_DEMANDES_VALIDER)
    @RolesAllowed("users")
    public Response validerDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.validerDemande(id, commentaire);
        return Response.ok().build();
    }

    @POST
    @Path(Constants.API_DEMANDES_REFUSER)
    @RolesAllowed("users")
    public Response refuserDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.refuserDemande(id, commentaire);
        return Response.ok().build();
    }

    /**
     * GESTION ADMINISTRATIVE (Admin)
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
     * GESTION DES UTILISATEURS / SOLDES
     */

    @GET
    @Path(Constants.API_UTILISATEUR_ME)
    @RolesAllowed("users")
    public Response getMe() {
        Utilisateur u = utilisateurService.getUtilisateur(getAuthenticatedUserId());
        if (u == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(u).build();
    }

    @GET
    @Path(Constants.API_UTILISATEUR_ME_SOLDE)
    @RolesAllowed("users")
    public Response getMySolde() {
        int solde = utilisateurService.consulterSolde(getAuthenticatedUserId());
        return Response.ok(solde).build();
    }

    @GET
    @Path(Constants.API_UTILISATEURS_RESPONSABLES)
    @RolesAllowed("users")
    public Response getResponsables() {
        return Response.ok(utilisateurService.getTousLesResponsables()).build();
    }

    /**
     * GESTION DES TYPES DE CONGÉS
     */

    @GET
    @Path(Constants.API_TYPES_CONGES)
    public Response getTypesConges() {
        List<TypeConge> types = typeCongeService.getTousLesTypesConges();
        return Response.ok(types).build();
    }

    @POST
    @Path(Constants.API_TYPES_CONGES)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrators")
    public Response createTypeConge(TypeConge type) {
        TypeConge created = typeCongeService.creerTypeConge(type);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path(Constants.API_TYPES_CONGES_BY_ID)
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("administrators")
    public Response updateTypeConge(@PathParam("id") String id, TypeConge type) {
        type.setId(id);
        typeCongeService.modifierTypeConge(type);
        return Response.ok().build();
    }

    @DELETE
    @Path(Constants.API_TYPES_CONGES_BY_ID)
    @RolesAllowed("administrators")
    public Response deleteTypeConge(@PathParam("id") String id) {
        typeCongeService.supprimerTypeConge(id);
        return Response.ok().build();
    }
}