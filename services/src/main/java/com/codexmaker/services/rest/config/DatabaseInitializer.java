package com.codexmaker.services.rest.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            /** Lecture du fichier init_db.sql qui ce trouve dans exo-demande-conge-extension/services/src/main/resources/init_db.sql */
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            DatabaseInitializer.class.getClassLoader().getResourceAsStream("init_db.sql")))) {

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sql.append(line).append("\n");
                }

                /** Exécution des statements pour rendre les requetes plus performates */ 
                for (String statement : sql.toString().split(";")) {
                    statement = statement.trim();
                    if (!statement.isEmpty() && !statement.startsWith("--")) {
                        stmt.execute(statement);
                    }
                }

                LOGGER.info("Base SQLite initialisée avec succès");
            }
        } catch (Exception e) {
            LOGGER.severe("Échec initialisation DB : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Initialisation DB échouée" + e.getMessage());
        }
    }
}