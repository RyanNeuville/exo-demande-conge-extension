package com.codexmaker.services.service.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Cette classe est un test de connexion à la base de données.
 * Elle est utilisée pour vérifier que la connexion à la base de données fonctionne correctement.
 */
public class DataBaseConnectionTest {
    private Connection connection;

    @BeforeEach
    void establishConnection() throws ClassNotFoundException, SQLException {
        connection = DataBaseConnection.getConnection();
    }

    @Test
    void testConnectionEstablished(){
        assertNotNull(connection, "La connection MySQL doit etre etablie");
    }

    @AfterEach
    void closeConnection() throws SQLException{
        if (connection != null && !connection.isClosed()){
            DataBaseConnection.closeConnection();
        }
    }
}