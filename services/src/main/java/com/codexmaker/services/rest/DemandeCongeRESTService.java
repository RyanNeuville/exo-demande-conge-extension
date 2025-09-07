package com.codexmaker.services.rest;

import org.exoplatform.commons.utils.ListAccess;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.rest.resource.ResourceContainer;
import org.exoplatform.social.core.identity.model.Identity;
import org.exoplatform.social.core.identity.model.Profile;
import org.exoplatform.social.core.manager.IdentityManager;
import org.exoplatform.social.core.manager.RelationshipManager;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DemandeCongeRESTService implements ResourceContainer {
    // Logger for our class
    private static final Log LOG = ExoLogger.getLogger(DemandeCongeRESTService.class);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // Service used to load user identities
    private final IdentityManager identityManager;

    //  Service used to manage relationships with other users
    private final RelationshipManager relationshipManager;

    private static final String BIRTHDAY_PROPERTY = "birthday";

    public DemandeCongeRESTService(IdentityManager identityManager, RelationshipManager relationshipManager) {
        this.identityManager = identityManager;
        this.relationshipManager = relationshipManager;
    }

    @GET
    @RolesAllowed("users")
    public Response getMyConnectionsBirthday(@Context SecurityContext securityContext) {

        // Load current authenticated user
        String authenticatedUserName = securityContext.getUserPrincipal().getName();
        try {
            // Load the identity of the current user
            Identity authenticatedUser = identityManager.getOrCreateUserIdentity(authenticatedUserName);
            // Load list of people connected with him
            ListAccess<Identity> connections = relationshipManager.getConnections(authenticatedUser);
            Identity[] connectionsIdentities = connections.load(0, connections.getSize());

            // Create a JSON array that will be returned with the response
            JSONArray jsonArray = new JSONArray();
            for (Identity connection : connectionsIdentities) {
                JSONObject connectionJSON = new JSONObject();
                connectionJSON.put("userName", connection.getRemoteId());
                connectionJSON.put("fullName", connection.getProfile().getFullName());
                connectionJSON.put("avatar", connection.getProfile().getAvatarUrl());
                connectionJSON.put(BIRTHDAY_PROPERTY, connection.getProfile().getProperty(BIRTHDAY_PROPERTY));
                jsonArray.put(connectionJSON);
            }

            // If everything is OK, we return a HTTP 200 response with the Json array
            return Response.ok(jsonArray.toString()).build();
        } catch (Exception e) {
            // Something went wrong, an error is logged and a HTTP 500 error is returned
            LOG.error("Error getting user connections for {}", authenticatedUserName, e);
            return Response.serverError().build();
        }
    }

    // We added POST verb since this function will update data on the server
    @POST
    @RolesAllowed("users")
    public Response updateBirthday(@Context SecurityContext securityContext, @FormParam("birthday") String birthday, @FormParam("userName") String userName) {
        String authenticatedUserName = securityContext.getUserPrincipal().getName();
        if(!authenticatedUserName.equalsIgnoreCase(userName)) {
            // Only the user himself is able to update his birthday date
            LOG.error("{} could not update the birthday of {}", authenticatedUserName, userName);
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        try {
            // Do a parsing to the received date and throw an exception if it is not valid
            LocalDate.parse(birthday, DATE_FORMATTER);
            // get the user identity and upda te the profile
            Identity authenticatedUser = identityManager.getOrCreateUserIdentity(authenticatedUserName);
            Profile authenticatedUserProfile = authenticatedUser.getProfile();
            // set the received birthday in the user profile
            authenticatedUserProfile.setProperty(BIRTHDAY_PROPERTY, birthday);
            identityManager.updateProfile(authenticatedUserProfile);
            // Prepare a response with the updated profile of the user
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userName", authenticatedUser.getRemoteId());
            jsonObject.put("fullName", authenticatedUserProfile.getFullName());
            jsonObject.put("avatar", authenticatedUserProfile.getAvatarUrl());
            jsonObject.put(BIRTHDAY_PROPERTY, authenticatedUserProfile.getProperty(BIRTHDAY_PROPERTY));
            return Response.ok(jsonObject.toString()).build();
        } catch (DateTimeParseException ex) {
            LOG.error("Could not parse date {}", birthday, ex);
            return Response.serverError().build();
        } catch (Exception e) {
            LOG.error("Could not update profile of user {}", authenticatedUserName, e);
            return Response.serverError().build();
        }
    }
}
