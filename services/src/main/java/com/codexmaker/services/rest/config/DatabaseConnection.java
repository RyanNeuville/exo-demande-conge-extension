package com.codexmaker.services.rest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public final class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DEFAULT_DB_PATH = "/data/demande_conge.db";
    private static final String LOCAL_FALLBACK_PATH = "demande_conge.db";

    private static String getEffectiveDbPath() {
        String path = System.getProperty("db.path", System.getenv().getOrDefault("DB_PATH", DEFAULT_DB_PATH));
        java.io.File file = new java.io.File(path);
        java.io.File parent = file.getParentFile();

        // Si on est en local et que /data n'est pas accessible, on bascule sur un fichier local
        if (DEFAULT_DB_PATH.equals(path) && (parent != null && !parent.exists() && !parent.mkdirs())) {
            LOGGER.warning("Le chemin par défaut /data n'est pas accessible. Utilisation du fallback local : " + LOCAL_FALLBACK_PATH);
            return LOCAL_FALLBACK_PATH;
        }
        return path;
    }


    private static Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            LOGGER.info("Driver SQLite chargé avec succès.");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("CRITIQUE : Driver SQLite introuvable ! " + e.getMessage());
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbPath = getEffectiveDbPath();
            java.io.File dbFile = new java.io.File(dbPath);
            
            // S'assurer que le dossier parent existe
            java.io.File parentDir = dbFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                if (parentDir.mkdirs()) {
                    LOGGER.info("Dossier parent créé : " + parentDir.getAbsolutePath());
                } else {
                    LOGGER.warning("Impossible de créer le dossier parent : " + parentDir.getAbsolutePath());
                }
            }

            LOGGER.info("Tentative de connexion à : jdbc:sqlite:" + dbPath);
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
            
            try (var stmt = connection.createStatement()) {
                stmt.execute("PRAGMA journal_mode = WAL;");
                stmt.execute("PRAGMA synchronous = NORMAL;");
                stmt.execute("PRAGMA cache_size = -20000;");
                stmt.execute("PRAGMA temp_store = MEMORY;");
                stmt.execute("PRAGMA mmap_size = 30000000;");
            }
            LOGGER.info("✅ Connexion SQLite ouverte sur : " + dbPath);
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

    /**
     * Méthode principale pour tester la connectivité et l'initialisation du schéma.
     */
    public static void main(String[] args) {
        LOGGER.info("Démarrage du test de connectivité SQLite...");
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                LOGGER.info("SUCCÈS : Connexion établie avec succès !");

                LOGGER.info("Initialisation du schéma de la base de données...");
                DatabaseInitializer.initialize();
                LOGGER.info("SUCCÈS : Schéma initialisé avec succès !");

                closeConnection();
                LOGGER.info("Test terminé avec succès.");
            }
        } catch (Exception e) {
            LOGGER.severe("ÉCHEC : Une erreur est survenue lors du test : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}