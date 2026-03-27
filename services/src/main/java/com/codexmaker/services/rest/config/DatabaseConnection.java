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

    private static volatile boolean isInitialized = false;

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
     */
    private static String getEffectiveDbPath() {
        String path = System.getProperty("db.path", System.getenv().getOrDefault("DB_PATH", DEFAULT_DB_PATH));
        java.io.File file = new java.io.File(path);
        java.io.File parent = file.getParentFile();

        if (DEFAULT_DB_PATH.equals(path) && (parent != null && !parent.exists() && !parent.mkdirs())) {
            LOGGER.warning("Le chemin par défaut /data n'est pas accessible. Utilisation du fallback local : " + LOCAL_FALLBACK_PATH);
            return LOCAL_FALLBACK_PATH;
        }
        return path;
    }

    /**
     * Récupère une NOUVELLE connexion active.
     */
    public static Connection getConnection() throws SQLException {
        String dbPath = getEffectiveDbPath();
        java.io.File dbFile = new java.io.File(dbPath);

        java.io.File parentDir = dbFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            if (parentDir.mkdirs()) {
                LOGGER.info("Dossier parent créé : " + parentDir.getAbsolutePath());
            }
        }

        Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

        // Initialiser le schéma une seule fois (Singleton pattern sur le flag)
        if (!isInitialized) {
            synchronized (DatabaseConnection.class) {
                if (!isInitialized) {
                    try {
                        DatabaseInitializer.initialize(conn);
                        isInitialized = true;
                    } catch (Exception e) {
                        LOGGER.warning("Erreur lors de l'initialisation auto : " + e.getMessage());
                    }
                }
            }
        }

        // Configurer cette connexion spécifique
        try (var stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode = WAL;");
            stmt.execute("PRAGMA synchronous = NORMAL;");
            stmt.execute("PRAGMA busy_timeout = 5000;");
        }
        return conn;
    }

    /**
     * Obsolète : Les connexions sont désormais gérées par requête et fermées par le bloc try-with-resources.
     */
    public static void closeConnection() {
        // Obsolete
    }

    public static void main(String[] args) {
        LOGGER.info("Démarrage du test de connectivité SQLite...");
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                LOGGER.info("SUCCÈS : Connexion établie avec succès !");
            }
        } catch (Exception e) {
            LOGGER.severe("ÉCHEC : Une erreur est survenue lors du test : " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}