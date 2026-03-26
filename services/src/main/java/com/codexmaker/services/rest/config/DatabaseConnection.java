package com.codexmaker.services.rest.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Gère la connexion unique à la base de données SQLite.
 * Implémente le pattern Singleton pour la connexion et configure les paramètres
 * de performance et de sécurité (mode WAL, synchronisation).
 */
public final class DatabaseConnection {

    private static final Logger LOGGER = Logger.getLogger(DatabaseConnection.class.getName());
    private static final String DEFAULT_DB_PATH = "/data/demande_conge.db";
    private static final String LOCAL_FALLBACK_PATH = "demande_conge.db";

    private static Connection connection;

    /**
     * Bloc d'initialisation statique pour charger le driver JDBC SQLite.
     */
    static {
        try {
            Class.forName("org.sqlite.JDBC");
            LOGGER.info("Driver SQLite chargé avec succès.");
        } catch (ClassNotFoundException e) {
            LOGGER.severe("CRITIQUE : Driver SQLite introuvable ! " + e.getMessage());
        }
    }

    /**
     * Détermine le chemin effectif du fichier de base de données.
     * Tente d'utiliser le volume Docker /data par défaut, sinon bascule sur un
     * fichier local.
     * 
     * @return Le chemin absolu ou relatif vers le fichier .db.
     */
    private static String getEffectiveDbPath() {
        String path = System.getProperty("db.path", System.getenv().getOrDefault("DB_PATH", DEFAULT_DB_PATH));
        java.io.File file = new java.io.File(path);
        java.io.File parent = file.getParentFile();

        /**
         * Fallback local si le répertoire /data n'est pas accessible (cas hors Docker).
         */
        if (DEFAULT_DB_PATH.equals(path) && (parent != null && !parent.exists() && !parent.mkdirs())) {
            LOGGER.warning("Le chemin par défaut /data n'est pas accessible. Utilisation du fallback local : "
                    + LOCAL_FALLBACK_PATH);
            return LOCAL_FALLBACK_PATH;
        }
        return path;
    }

    /**
     * Récupère la connexion active ou en crée une nouvelle si nécessaire.
     * Configure également les PRAGMA SQLite pour optimiser la concurrence et la
     * rapidité.
     * 
     * @return Une instance de Connection JDBC.
     * @throws SQLException En cas d'échec de l'ouverture du fichier.
     */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String dbPath = getEffectiveDbPath();
            java.io.File dbFile = new java.io.File(dbPath);

            /** Création récursive du dossier parent si manquant. */
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

            /**
             * Initialisation automatique du schéma (tables et données de base).
             */
            try {
                DatabaseInitializer.initialize(connection);
            } catch (Exception e) {
                LOGGER.warning("Erreur lors de l'initialisation auto : " + e.getMessage());
            }

            /**
             * Configuration des PRAGMA pour le mode Write-Ahead Logging (WAL)
             * permettant des lectures et écritures concurrentes.
             */
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

    /**
     * Ferme proprement la connexion à la base de données.
     */
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
     * Méthode principale utilitaire pour tester la connectivité manuellement.
     * 
     * @param args Arguments ligne de commande (non utilisés).
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