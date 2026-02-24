// package com.codexmaker.services.rest.api;

// import com.codexmaker.services.rest.model.DemandeConge;
// import com.codexmaker.services.rest.model.UserDemandes;
// import com.codexmaker.services.rest.utils.Constants;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.exoplatform.commons.utils.ListAccess;
// import org.exoplatform.container.ExoContainerContext;
// import org.exoplatform.services.log.ExoLogger;
// import org.exoplatform.services.log.Log;
// import org.exoplatform.services.organization.OrganizationService;
// import org.exoplatform.services.organization.User;
// import org.exoplatform.services.rest.resource.ResourceContainer;
// import org.exoplatform.services.security.ConversationState;
// import org.exoplatform.services.security.Identity;
// import org.exoplatform.social.core.manager.IdentityManager;
// import org.exoplatform.social.core.manager.RelationshipManager;

// import javax.annotation.security.RolesAllowed;
// import javax.ws.rs.*;
// import javax.ws.rs.core.MediaType;
// import javax.ws.rs.core.Response;
// import java.io.File;
// import java.text.SimpleDateFormat;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.List;
// import java.util.Objects;
// import java.util.concurrent.atomic.AtomicLong;
// import java.util.stream.Collectors;

// @Path(Constants.MAIN_END_POINT)
// @Produces(MediaType.APPLICATION_JSON)
// public class DemandeCongeRestService implements ResourceContainer {

// private static final Log LOG =
// ExoLogger.getLogger(DemandeCongeRestService.class);
// private static final String DEMANDES_FILE =
// Objects.requireNonNull(DemandeCongeRestService.class.getResource(Constants.DEMANDES_FILE_PATH)).getPath();
// private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
// private static final AtomicLong ID_GENERATOR = new AtomicLong(1);
// private static final SimpleDateFormat DATE_FORMAT = new
// SimpleDateFormat(Constants.DATE_FORMAT_PATERN);

// private final IdentityManager identityManager;
// private final RelationshipManager relationshipManager;
// private final OrganizationService organizationService;

// public DemandeCongeRestService() {
// this.identityManager = ExoContainerContext.getService(IdentityManager.class);
// this.relationshipManager =
// ExoContainerContext.getService(RelationshipManager.class);
// this.organizationService =
// ExoContainerContext.getService(OrganizationService.class);
// DATE_FORMAT.setLenient(false);
// }

// private String getAuthenticatedUserName() {
// ConversationState state = ConversationState.getCurrent();
// return state != null && state.getIdentity() != null ?
// state.getIdentity().getUserId() : null;
// }

// private boolean isAdmin() {
// try {
// Identity identity = ConversationState.getCurrent().getIdentity();
// return
// organizationService.getMembershipHandler().findMembershipsByUserAndGroup(getAuthenticatedUserName(),
// "/platform/administrators").isEmpty();
// } catch (Exception e) {
// LOG.error(Constants.CHECKING_ROLE_ERROR, e);
// return true;
// }
// }

// private synchronized List<UserDemandes> readDemandesFile() {
// try {
// File file = new File(DEMANDES_FILE);
// if (!file.exists()) {
// return new ArrayList<>();
// }
// return OBJECT_MAPPER.readValue(file,
// OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class,
// UserDemandes.class));
// } catch (Exception e) {
// LOG.error(Constants.LOG_ERROR_READING_FILE, e);
// return new ArrayList<>();
// }
// }

// private synchronized void writeDemandesFile(List<UserDemandes> userDemandes)
// {
// try {
// File file = new File(DEMANDES_FILE);
// file.getParentFile().mkdirs();
// if (!file.exists()) {
// file.createNewFile();
// OBJECT_MAPPER.writeValue(file, new ArrayList<>());
// LOG.info(Constants.LOG_INFO_DEMANDE_FILE_CREATED, DEMANDES_FILE);
// }
// OBJECT_MAPPER.writeValue(file, userDemandes);
// LOG.info(Constants.LOG_INFO_DEMANDE_FILE_CREATED_SUCCESS);
// } catch (Exception e) {
// LOG.error(Constants.LOG_ERROR_CREATED_DEMANDE, DEMANDES_FILE, e);
// }
// }

// @POST
// @Path(Constants.SUBMIT_END_POINT)
// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// @RolesAllowed("users")
// public Response submitDemande(@FormParam("dateDebut") String dateDebut,
// @FormParam("dateFin") String dateFin,
// @FormParam("typeConge") String typeConge,
// @FormParam("motif") String motif) {
// try {
// String userName = getAuthenticatedUserName();
// if (userName == null) {
// return Response.status(Response.Status.UNAUTHORIZED).build();
// }

// if (dateDebut == null || dateFin == null || typeConge == null ||
// !typeConge.matches("VACANCES|MALADIE|AUTRE")) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Invalid
// input").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElseGet(() -> {
// User user = null;
// try {
// user = organizationService.getUserHandler().findUserByName(userName);
// } catch (Exception e) {
// throw new RuntimeException(e);
// }
// String fullName = user != null ? user.getFirstName() + " " +
// user.getLastName() : userName;
// UserDemandes ud = new UserDemandes(userName, fullName, new ArrayList<>());
// allDemandes.add(ud);
// return ud;
// });

// DemandeConge demande = new DemandeConge(
// ID_GENERATOR.getAndIncrement(),
// dateDebut,
// dateFin,
// typeConge,
// motif,
// Constants.STATUT_EN_ATTENTE,
// new SimpleDateFormat("yyyy-MM-dd").format(new Date())
// );

// userDemandes.getDemandes().add(demande);
// writeDemandesFile(allDemandes);

// LOG.info("Demande submitted by {}: {}", userName, demande);
// return Response.ok().build();
// } catch (Exception e) {
// LOG.error("Error submitting demande", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// submitting demande").build();
// }
// }

// @GET
// @Path(Constants.ALL_DEMANDES_END_POINT)
// @RolesAllowed("administrators")
// public Response getAllDemandes() {
// try {
// if (isAdmin()) {
// LOG.warn("Non-admin user {} attempted to access all demandes",
// getAuthenticatedUserName());
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// LOG.info("Fetched all demandes for {} users", allDemandes.size());
// return Response.ok(allDemandes).build();
// } catch (Exception e) {
// LOG.error("Error fetching all demandes", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// fetching all demandes").build();
// }
// }

// @GET
// @Path(Constants.MY_DEMANDE_END_POINT)
// @RolesAllowed("users")
// public Response getMyDemandes() {
// try {
// String userName = getAuthenticatedUserName();
// if (userName == null) {
// return Response.status(Response.Status.UNAUTHORIZED).build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElseGet(() -> new UserDemandes(userName, userName, new ArrayList<>()));

// return Response.ok(userDemandes).build();
// } catch (Exception e) {
// LOG.error("Error fetching my demandes", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// fetching my demandes").build();
// }
// }

// @GET
// @Path(Constants.MY_RELATIONS_DEMANDE_END_POINT)
// @RolesAllowed("users")
// public Response getRelationsDemandes() {
// String userName = null;
// try {
// userName = getAuthenticatedUserName();
// if (userName == null) {
// LOG.warn("Unauthorized access attempt to get relations demandes");
// return Response.status(Response.Status.UNAUTHORIZED).entity("User not
// authenticated").build();
// }

// org.exoplatform.social.core.identity.model.Identity userIdentity =
// identityManager.getOrCreateUserIdentity(userName);
// if (userIdentity == null) {
// LOG.warn("Identity not found for user {}", userName);
// return Response.status(Response.Status.NOT_FOUND).entity("User identity not
// found").build();
// }

// ListAccess<org.exoplatform.social.core.identity.model.Identity> connections =
// relationshipManager.getConnections(userIdentity);
// org.exoplatform.social.core.identity.model.Identity[] connectionsIdentities =
// connections.load(0, connections.getSize());

// List<String> relationUserNames = new ArrayList<>();
// for (org.exoplatform.social.core.identity.model.Identity connection :
// connectionsIdentities) {
// if (!userName.equals(connection.getRemoteId())) {
// relationUserNames.add(connection.getRemoteId());
// }
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// List<UserDemandes> relationsDemandes = allDemandes.stream()
// .filter(ud -> relationUserNames.contains(ud.getUserName()))
// .collect(Collectors.toList());

// LOG.info("Fetched {} relations with {} demandes for user {}",
// relationUserNames.size(), relationsDemandes.size(), userName);
// return Response.ok(relationsDemandes).build();
// } catch (Exception e) {
// LOG.error("Error fetching relations demandes for user {}", userName, e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// fetching relations demandes").build();
// }
// }

// @GET
// @Path(Constants.DEMANDE_EN_ATTANTE_END_POINT)
// @RolesAllowed("administrators")
// public Response getEnAttenteDemandes() {
// try {
// if (isAdmin()) {
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// List<UserDemandes> enAttenteDemandes = allDemandes.stream()
// .map(ud -> {
// List<DemandeConge> enAttente = ud.getDemandes().stream()
// .filter(d -> Constants.STATUT_EN_ATTENTE.equals(d.getStatus()))
// .collect(Collectors.toList());
// return new UserDemandes(ud.getUserName(), ud.getFullName(), enAttente);
// })
// .filter(ud -> !ud.getDemandes().isEmpty())
// .collect(Collectors.toList());

// return Response.ok(enAttenteDemandes).build();
// } catch (Exception e) {
// LOG.error("Error fetching en attente demandes", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// fetching en attente demandes").build();
// }
// }

// @PUT
// @Path(Constants.UPDATE_DEMANDE_END_POINT)
// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// @RolesAllowed("administrators")
// public Response updateDemande(@FormParam("userName") String userName,
// @FormParam("index") int index,
// @FormParam("dateDebut") String dateDebut,
// @FormParam("dateFin") String dateFin,
// @FormParam("typeConge") String typeConge,
// @FormParam("motif") String motif) {
// try {
// if (isAdmin()) {
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// if (dateDebut == null || dateFin == null || typeConge == null ||
// !typeConge.matches("VACANCES|MALADIE|AUTRE")) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Invalid
// input").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElse(null);

// if (userDemandes == null || index < 0 || index >=
// userDemandes.getDemandes().size()) {
// return Response.status(Response.Status.NOT_FOUND).entity("Demande not
// found").build();
// }

// DemandeConge demande = userDemandes.getDemandes().get(index);
// if (!Constants.STATUT_EN_ATTENTE.equals(demande.getStatus())) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Only EN_ATTENTE
// demandes can be updated").build();
// }

// demande.setDateDebut(dateDebut);
// demande.setDateFin(dateFin);
// demande.setTypeConge(typeConge);
// demande.setMotif(motif);

// writeDemandesFile(allDemandes);
// LOG.info("Demande updated for user {} at index {}", userName, index);
// return Response.ok().build();
// } catch (Exception e) {
// LOG.error("Error updating demande", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// updating demande").build();
// }
// }

// @POST
// @Path(Constants.APPROUVER_DEMANDE_END_POINT )
// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// @RolesAllowed("administrators")
// public Response approveDemande(@FormParam("userName") String userName,
// @FormParam("index") int index) {
// try {
// if (isAdmin()) {
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElse(null);

// if (userDemandes == null || index < 0 || index >=
// userDemandes.getDemandes().size()) {
// return Response.status(Response.Status.NOT_FOUND).entity("Demande not
// found").build();
// }

// DemandeConge demande = userDemandes.getDemandes().get(index);
// if (!Constants.STATUT_EN_ATTENTE.equals(demande.getStatus())) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Only EN_ATTENTE
// demandes can be approved").build();
// }

// demande.setStatus(Constants.STATUT_APPROUVEE);
// writeDemandesFile(allDemandes);
// LOG.info("Demande approved for user {} at index {}", userName, index);
// return Response.ok().build();
// } catch (Exception e) {
// LOG.error("Error approving demande", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// approving demande").build();
// }
// }

// @POST
// @Path(Constants.REJETER_DEMANDE_END_POINT)
// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// @RolesAllowed("administrators")
// public Response rejectDemande(@FormParam("userName") String userName,
// @FormParam("index") int index) {
// try {
// if (isAdmin()) {
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElse(null);

// if (userDemandes == null || index < 0 || index >=
// userDemandes.getDemandes().size()) {
// return Response.status(Response.Status.NOT_FOUND).entity("Demande not
// found").build();
// }

// DemandeConge demande = userDemandes.getDemandes().get(index);
// if (!Constants.STATUT_EN_ATTENTE.equals(demande.getStatus())) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Only EN_ATTENTE
// demandes can be rejected").build();
// }

// demande.setStatus(Constants.STATUT_REJETEE);
// writeDemandesFile(allDemandes);
// LOG.info("Demande rejected for user {} at index {}", userName, index);
// return Response.ok().build();
// } catch (Exception e) {
// LOG.error("Error rejecting demande", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// rejecting demande").build();
// }
// }

// @POST
// @Path(Constants.CANCEL_DEMANDE_END_POINT)
// @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
// @RolesAllowed("administrators")
// public Response cancelDemande(@FormParam("userName") String userName,
// @FormParam("index") int index) {
// try {
// if (isAdmin()) {
// return Response.status(Response.Status.FORBIDDEN).entity("Admin access
// required").build();
// }

// List<UserDemandes> allDemandes = readDemandesFile();
// UserDemandes userDemandes = allDemandes.stream()
// .filter(ud -> userName.equals(ud.getUserName()))
// .findFirst()
// .orElse(null);

// if (userDemandes == null || index < 0 || index >=
// userDemandes.getDemandes().size()) {
// return Response.status(Response.Status.NOT_FOUND).entity("Demande not
// found").build();
// }

// DemandeConge demande = userDemandes.getDemandes().get(index);
// if (!Constants.STATUT_EN_ATTENTE.equals(demande.getStatus())) {
// return Response.status(Response.Status.BAD_REQUEST).entity("Only EN_ATTENTE
// demandes can be canceled").build();
// }

// userDemandes.getDemandes().remove(index);
// writeDemandesFile(allDemandes);
// LOG.info("Demande canceled for user {} at index {}", userName, index);
// return Response.ok().build();
// } catch (Exception e) {
// LOG.error("Error canceling demande", e);
// return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error
// canceling demande").build();
// }
// }
// }