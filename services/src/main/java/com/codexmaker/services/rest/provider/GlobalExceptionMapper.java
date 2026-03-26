package com.codexmaker.services.rest.provider;

import com.codexmaker.services.rest.exception.BusinessException;
import com.codexmaker.services.rest.exception.InsufficientLeaveBalanceException;
import com.codexmaker.services.rest.exception.UnauthorizedActionException;
import com.codexmaker.services.rest.model.dto.ErrorResponse;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Intercepteur global d'exceptions pour l'API REST.
 * Transforme les exceptions Java (checked ou unchecked) en réponses HTTP
 * standardisées au format JSON, consommables par le frontend.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Log LOG = ExoLogger.getLogger(GlobalExceptionMapper.class);

    /**
     * Convertit une exception en une réponse HTTP.
     * 
     * @param exception L'erreur interceptée.
     * @return Une instance de Response avec le code statut et l'entité
     *         ErrorResponse.
     */
    @Override
    public Response toResponse(Throwable exception) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String errorCode = "SERVER_ERROR";
        String message = exception.getMessage();

        /**
         * Dispatching selon le type d'exception métier.
         */
        if (exception instanceof BusinessException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            errorCode = "BUSINESS_ERROR";
        } else if (exception instanceof InsufficientLeaveBalanceException) {
            status = 409;
            errorCode = "INSUFFICIENT_BALANCE";
        } else if (exception instanceof UnauthorizedActionException) {
            status = Response.Status.FORBIDDEN.getStatusCode();
            errorCode = "UNAUTHORIZED_ACTION";
        } else {
            LOG.error("Erreur critique non gérée par l'application : " + exception.getMessage(), exception);
            message = "Une erreur serveur interne est survenue.";
        }

        ErrorResponse error = new ErrorResponse(status, message, errorCode);
        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
