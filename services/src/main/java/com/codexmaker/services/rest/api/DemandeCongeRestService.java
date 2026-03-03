package com.codexmaker.services.rest.api;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.service.DemandeCongeService;
import com.codexmaker.services.rest.service.TypeCongeService;
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
 * Service REST pour la gestion des demandes de congés.
 */
@Path(Constants.MAIN_END_POINT)
@Produces(MediaType.APPLICATION_JSON)
public class DemandeCongeRestService implements ResourceContainer {

    private final DemandeCongeService demandeCongeService;
    private final TypeCongeService typeCongeService;

    public DemandeCongeRestService() {
        this.demandeCongeService = ExoContainerContext.getService(DemandeCongeService.class);
        this.typeCongeService = ExoContainerContext.getService(TypeCongeService.class);
    }

    private String getAuthenticatedUserId() {
        ConversationState state = ConversationState.getCurrent();
        return (state != null && state.getIdentity() != null) ? state.getIdentity().getUserId() : "anonymous";
    }

    @GET
    @Path("/demandes/me")
    @RolesAllowed("users")
    public Response getMyDemandes() {
        List<DemandeConge> demandes = demandeCongeService.getDemandesParUtilisateur(getAuthenticatedUserId());
        return Response.ok(demandes).build();
    }

    @POST
    @Path("/demandes")
    @Consumes(MediaType.APPLICATION_JSON)
    @RolesAllowed("users")
    public Response submitDemande(DemandeConge demande) {
        try {
            DemandeConge saved = demandeCongeService.soumettreDemande(demande, getAuthenticatedUserId());
            return Response.status(Response.Status.CREATED).entity(saved).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/demandes/a-traiter")
    @RolesAllowed("users")
    public Response getDemandesATraiter() {
        List<DemandeConge> demandes = demandeCongeService.getDemandesATraiter(getAuthenticatedUserId());
        return Response.ok(demandes).build();
    }

    @POST
    @Path("/demandes/{id}/valider")
    @RolesAllowed("users")
    public Response validerDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.validerDemande(id, commentaire);
        return Response.ok().build();
    }

    @POST
    @Path("/demandes/{id}/refuser")
    @RolesAllowed("users")
    public Response refuserDemande(@PathParam("id") String id, @QueryParam("commentaire") String commentaire) {
        demandeCongeService.refuserDemande(id, commentaire);
        return Response.ok().build();
    }

    @DELETE
    @Path("/demandes/{id}")
    @RolesAllowed("users")
    public Response annulerDemande(@PathParam("id") String id) {
        demandeCongeService.annulerDemande(id);
        return Response.ok().build();
    }

    @GET
    @Path("/demandes/export")
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

    @GET
    @Path("/types-conges")
    public Response getTypesConges() {
        List<TypeConge> types = typeCongeService.getTousLesTypesConges();
        return Response.ok(types).build();
    }
}