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
 * Mapper global pour transformer les exceptions métier en réponses JSON propres.
 */
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Log LOG = ExoLogger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        int status = Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        String errorCode = "SERVER_ERROR";
        String message = exception.getMessage();

        if (exception instanceof BusinessException) {
            status = Response.Status.BAD_REQUEST.getStatusCode();
            errorCode = "BUSINESS_ERROR";
        } else if (exception instanceof InsufficientLeaveBalanceException) {
            status = 409; // Conflict or 400
            errorCode = "INSUFFICIENT_BALANCE";
        } else if (exception instanceof UnauthorizedActionException) {
            status = Response.Status.FORBIDDEN.getStatusCode();
            errorCode = "UNAUTHORIZED_ACTION";
        } else {
            LOG.error("Erreur non gérée interceptée par le Mapper: " + exception.getMessage(), exception);
            message = "Une erreur serveur interne est survenue.";
        }

        ErrorResponse error = new ErrorResponse(status, message, errorCode);
        return Response.status(status)
                .entity(error)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
