package com.codexmaker.services.rest.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

/**
 * Configuration personnalisée de Jackson pour supporter les types Java 8 (LocalDate, etc.).
 * Ce provider permet à eXo Platform de désérialiser correctement les dates envoyées par le frontend.
 */
@Provider
public class JacksonContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper mapper;

    public JacksonContextResolver() {
        this.mapper = new ObjectMapper();
        /** Enregistre le module pour LocalDate, LocalDateTime, etc. */
        this.mapper.registerModule(new JavaTimeModule());
        /** Désactive l'écriture des dates sous forme de timestamps numériques pour préférer ISO-8601 */
        this.mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
}
