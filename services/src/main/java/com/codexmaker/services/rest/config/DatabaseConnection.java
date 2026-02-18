package com.codexmaker.services.rest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DB_URL = "jdbc:sqlite:./demande_conge.db";

    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            LOGGER.info("Driver SQLite chargé");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver SQLite introuvable", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
            try (var stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode = WAL;");
                stmt.execute("PRAGMA synchronous = NORMAL;");
                stmt.execute("PRAGMA cache_size = -20000;");
                stmt.execute("PRAGMA temp_store = MEMORY;");
                stmt.execute("PRAGMA mmap_size = 30000000;");
            }
            LOGGER.info("Connexion SQLite ouverte (optimisée)");
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                LOGGER.info("Connexion SQLite fermée");
            } catch (SQLException e) {
                LOGGER.warning("Erreur fermeture connexion : " + e.getMessage());
            }
        }
    }
}