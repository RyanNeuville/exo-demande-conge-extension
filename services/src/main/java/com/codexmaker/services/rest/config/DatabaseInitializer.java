package com.codexmaker.services.rest.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Statement;
import java.util.logging.Logger;

import com.codexmaker.services.rest.utils.Constants;

/**
 * Responsable de l'initialisation du schéma de base de données.
 * Lit et exécute le script 'init_db.sql' pour créer les tables et insérer
 * les données de référence (types de congés).
 */
public class DatabaseInitializer {

    private static final Logger LOGGER = Logger.getLogger(DatabaseInitializer.class.getName());

    /**
     * Exécute le script d'initialisation sur la connexion fournie.
     * Le script est nettoyé des commentaires et des lignes vides avant exécution.
     *
     * @param conn La connexion JDBC active.
     */
    public static void initialize(Connection conn) {
        if (conn == null) {
            LOGGER.severe("ÉCHEC : La connexion fournie est null.");
            return;
        }

        try (Statement stmt = conn.createStatement()) {

            /**
             * Chargement du fichier SQL depuis les ressources du classpath.
             */
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            DatabaseInitializer.class.getClassLoader().getResourceAsStream("init_db.sql")))) {

                if (reader == null) {
                    LOGGER.severe("ÉCHEC : Impossible de trouver init_db.sql dans les ressources.");
                    return;
                }

                StringBuilder sql = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    String trimmedLine = line.trim();

                    /**
                     * Exclusion des lignes vides et des commentaires SQL (--).
                     */
                    if (trimmedLine.isEmpty() || trimmedLine.startsWith("--")) {
                        continue;
                    }
                    sql.append(line).append(" ");
                }

                /**
                 * Découpage du script en instructions individuelles via le séparateur ';'.
                 */
                for (String statement : sql.toString().split(";")) {
                    statement = statement.trim();
                    if (!statement.isEmpty()) {
                        stmt.execute(statement);
                    }
                }

                LOGGER.info(Constants.SUCCES_DB_INITIALIZED);
            }
        } catch (Exception e) {
            LOGGER.severe(Constants.ERROR_DB_INITIALIZATION + " : " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(Constants.ERROR_DB_INITIALIZATION + " : " + e.getMessage());
        }
    }

    /**
     * Version simplifiée récupérant automatiquement la connexion via
     * DatabaseConnection.
     */
    public static void initialize() {
        try {
            initialize(DatabaseConnection.getConnection());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}