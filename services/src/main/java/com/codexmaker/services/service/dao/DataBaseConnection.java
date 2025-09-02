package com.codexmaker.services.service.dao;

/**
 * Classe DataBaseConnection est responsable de la gestion de la connexion à la base de données.
 * Elle utilise les constantes définies dans la classe Constants pour établir la connexion.
 * elle utilise un singleton pour garantir qu'une seule connexion est utilisée tout au long de l'application.
 * En cas d'erreur lors de l'établissement de la connexion, une exception est levée.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import com.codexmaker.services.utils.Constants;


public class DataBaseConnection {

    /** Logger pour enregistrer les messages de connexion et d'erreur */
    private static final Logger logger = Logger.getLogger(DataBaseConnection.class.getName());

    /** Récupère les variables d'environnement définies dans le docker-compose */
    private static final String DB_HOST = "mysql"; // Nom du service MySQL dans le docker-compose
    private static final String DB_PORT = "3306";
    private static final String DB_NAME = System.getenv("MYSQL_DATABASE");
    private static final String DB_USER = System.getenv("MYSQL_USER");
    private static final String DB_PASSWORD = System.getenv("MYSQL_PASSWORD");

    /** L'URL de connexion JDBC, utilisant le nom du service Docker comme hôte */
    private static final String JDBC_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?useSSL=false&serverTimezone=UTC";

    private static Connection connection;

    DataBaseConnection() {
    }

    /** Méthode pour obtenir la connexion à la base de données */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            try {
                /** Charge le driver JDBC */
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                /** Gère l'erreur si le driver n'est pas trouvé. */
                throw new SQLException("MySQL JDBC Driver non trouvé.", e);
            }
            return DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        }
        return connection;
    }

    /** Méthode pour fermer la connexion à la base de données */
    public static void closeConnection(){
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    logger.info(Constants.SUCCESS_CLOSE_CONNECTION);
                }
            } catch (SQLException e) {
                logger.severe(Constants.ERROR_CLOSE_CONNECTION + e.getMessage());
            }
        }
    }
}
