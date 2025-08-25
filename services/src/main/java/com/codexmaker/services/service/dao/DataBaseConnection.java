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

    private static Connection connection;

    DataBaseConnection() {
    }

    /** Méthode pour obtenir la connexion à la base de données */
    public static synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()){
            try {
                Class.forName(Constants.DB_DRIVER);
                connection = DriverManager.getConnection(Constants.DB_URL, Constants.DB_USER, Constants.DB_PASSWORD);
                if (connection != null) {
                    logger.info(Constants.SUCCESS_CONNECTION);
                }
            } catch (SQLException e) {
                logger.severe(Constants.ERROR_DATABASE_CONNECTION + e.getMessage());
                throw new RuntimeException(Constants.FAILED_TO_CONNECT_TO_DATABASE, e);
            } catch (ClassNotFoundException e) {
                logger.severe(Constants.ERROR_DRIVER_NOT_FOUND + e.getMessage());
                throw new RuntimeException(e);
            }
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
