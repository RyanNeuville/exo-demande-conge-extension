package com.codexmaker.services.controller;

import com.codexmaker.services.model.entity.DemandeConge;
import com.codexmaker.services.model.entity.DemandeCongeResponse;
import com.codexmaker.services.service.DemandeCongeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.inject.Inject;
import javax.enterprise.context.RequestScoped;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;

import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 * Ressource REST (Contrôleur JAX-RS) pour la gestion des demandes de congé.
 * Expose les fonctionnalités du service via des endpoints HTTP.
 */
/** Indique que ce bean est géré par CDI et a une portée de requête */
@RequestScoped
@Path("/conges") // Chemin de base pour toutes les opérations de ce contrôleur
public class DemandeCongeController implements ResourceContainer {

    private static final Log LOG = ExoLogger.getLogger(DemandeCongeController.class);

    // Injection du service via CDI
    @Inject
    private DemandeCongeService demandeCongeService;

    private static final String USER_ID_SESSION = "exoUserId"; // Nom de l'attribut de session pour l'ID utilisateur
    private static final String UNAUTHORIZED_MSG = "Utilisateur non authentifié ou session expirée.";
    private static final String FAILED_TITLE = "title";
    private static final String FAILED_RESPONSE_MESSAGE = "message";
    private static final String FAILED_RESPONSE_CODE = "statusCode";

    // Constructor injection for testing or explicit CDI use (though @Inject on fields is also common for CDI)
    // If you remove @Inject from the field, you'd need this constructor if you want CDI to inject it here:
    // public DemandeCongeRestService(DemandeCongeService demandeCongeService) {
    //     this.demandeCongeService = demandeCongeService;
    // }

    /**
     * Récupère l'ID de l'utilisateur connecté depuis la session HTTP.
     * En environnement eXo, l'utilisateur est déjà authentifié par le conteneur.
     * @param request L'objet HttpServletRequest.
     * @return L'ID de l'utilisateur ou null si non authentifié.
     */
    private String getUserId(HttpServletRequest request) {
        // Option 1: Via Principal (méthode préférée si l'authentification eXo remplit Principal)
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return principal.getName();
        }

        // Option 2: Via la session (pour la simulation de login ou si Principal n'est pas rempli ainsi)
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(USER_ID_SESSION) != null) {
            return session.getAttribute(USER_ID_SESSION).toString();
        }
        return null;
    }

    /**
     * Méthode utilitaire pour créer une réponse d'erreur JSON standardisée.
     */
    private String createErrorResponse(String title, String message, int statusCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(FAILED_TITLE, title);
        jsonObject.put(FAILED_RESPONSE_MESSAGE, message);
        jsonObject.put(FAILED_RESPONSE_CODE, statusCode);
        return jsonObject.toString();
    }

    /**
     * Méthode utilitaire pour convertir une DemandeCongeResponse du service en une Response JAX-RS.
     * Suit le pattern de l'exemple du professeur.
     */
    private Response handleServiceResponse(DemandeCongeResponse serviceResponse) {
        if (serviceResponse.getResponse() != null) {
            return Response.status(serviceResponse.getStatus())
                    .entity(serviceResponse.getResponse())
                    .type(MediaType.APPLICATION_JSON) // Assurez-vous que le type est JSON
                    .build();
        } else {
            // Si la réponse est null, le message d'erreur est déjà formaté en JSON par le service
            return Response.status(serviceResponse.getStatus())
                    .entity(serviceResponse.getMessage())
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    // --- Endpoints d'Authentification (Simulation, comme dans l'exemple du professeur) ---

    @POST
    @Path("/login")
    @PermitAll // Permet l'accès sans authentification préalable
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@Context HttpServletRequest request,
                          @FormParam("username") String username,
                          @FormParam("password") String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(createErrorResponse("Erreur d'authentification", "Nom d'utilisateur ou mot de passe manquant.", 400))
                    .build();
        }
        // Simulation très simple: tout mot de passe avec un username est "authentifié"
        // En prod, ceci ferait appel à un service d'authentification eXo
        request.getSession(true).setAttribute(USER_ID_SESSION, username);
        LOG.info("Utilisateur '{}' authentifié (simulation) et session créée.", username);
        return Response.ok(Collections.singletonMap("status", "authentifié")).build();
    }

    @POST
    @Path("/logout")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response logout(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USER_ID_SESSION);
            session.invalidate(); // Invalide toute la session
        }
        LOG.info("Utilisateur déconnecté et session invalidée.");
        return Response.ok(Collections.singletonMap("status", "déconnecté")).build();
    }

    @GET
    @Path("/check-session")
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkSession(@Context HttpServletRequest request) {
        String userId = getUserId(request);
        boolean isAuthenticated = userId != null;
        LOG.info("Vérification de session: isAuthenticated={}", isAuthenticated);
        Map<String, Object> sessionInfo = new HashMap<>();
        sessionInfo.put("authenticated", isAuthenticated);
        if (isAuthenticated) {
            sessionInfo.put("userId", userId);
        }
        return Response.ok(sessionInfo).build();
    }

    // --- Endpoints pour les Demandes de Congé ---

    @POST
    @Path("/demandes")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response soumettreDemande(@Context HttpServletRequest request, DemandeConge demande) {
        String userId = getUserId(request);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        DemandeCongeResponse response = demandeCongeService.soumettreDemande(demande, userId);
        return handleServiceResponse(response);
    }

    @PUT
    @Path("/demandes/{demandeId}/approuver")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // ou MediaType.APPLICATION_JSON si commentaires sont dans un JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response approuverDemande(@Context HttpServletRequest request,
                                     @PathParam("demandeId") int demandeId,
                                     @FormParam("commentaires") String commentaires) {
        String approverId = getUserId(request);
        if (approverId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        // La vérification du rôle du manager/admin se fait dans le service
        DemandeCongeResponse response = demandeCongeService.approuverDemande(demandeId, commentaires);
        return handleServiceResponse(response);
    }

    @PUT
    @Path("/demandes/{demandeId}/refuser")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED) // ou MediaType.APPLICATION_JSON
    @Produces(MediaType.APPLICATION_JSON)
    public Response refuserDemande(@Context HttpServletRequest request,
                                   @PathParam("demandeId") int demandeId,
                                   @FormParam("commentaires") String commentaires) {
        String managerId = getUserId(request);
        if (managerId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        DemandeCongeResponse response = demandeCongeService.refuserDemande(demandeId, commentaires);
        return handleServiceResponse(response);
    }

    @PUT
    @Path("/demandes/{demandeId}/annuler")
    @Produces(MediaType.APPLICATION_JSON)
    public Response annulerDemande(@Context HttpServletRequest request,
                                   @PathParam("demandeId") int demandeId) {
        String userId = getUserId(request);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        DemandeCongeResponse response = demandeCongeService.annulerDemande(demandeId, userId);
        return handleServiceResponse(response);
    }

    @GET
    @Path("/demandes/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDemandesUtilisateur(@Context HttpServletRequest request) {
        String userId = getUserId(request);
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        DemandeCongeResponse response = demandeCongeService.getDemandesUtilisateur(userId);
        return handleServiceResponse(response);
    }

    @GET
    @Path("/demandes/{demandeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDemandeById(@Context HttpServletRequest request, @PathParam("demandeId") int demandeId) {
        String userId = getUserId(request); // Pourrait être utilisé pour une vérification d'accès si besoin
        if (userId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        DemandeCongeResponse response = demandeCongeService.getDemandeById(demandeId);
        return handleServiceResponse(response);
    }

    @GET
    @Path("/demandes/en-attente")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDemandesEnAttente(@Context HttpServletRequest request) {
        String approverId = getUserId(request);
        if (approverId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        // La vérification du rôle de l'approbateur se fait dans le service
        DemandeCongeResponse response = demandeCongeService.getDemandesEnAttente(approverId);
        return handleServiceResponse(response);
    }

    @GET
    @Path("/demandes/all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDemandes(@Context HttpServletRequest request) {
        String adminId = getUserId(request);
        if (adminId == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(createErrorResponse("Authentification requise", UNAUTHORIZED_MSG, 401))
                    .build();
        }
        // La vérification du rôle de l'admin se fait dans le service
        DemandeCongeResponse response = demandeCongeService.getAllDemandes(adminId);
        return handleServiceResponse(response);
    }
}
