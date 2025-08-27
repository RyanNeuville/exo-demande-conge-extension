package com.codexmaker.services.service.dao;

import io.qameta.allure.Step;
import io.qameta.allure.junit5.AllureJunit5;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Cette classe est un test de connexion à la base de données.
 * Elle est utilisée pour vérifier que la connexion à la base de données fonctionne correctement.
 */
@ExtendWith(AllureJunit5.class)
public class DataBaseConnectionTest {
    private Connection connection;

    @BeforeEach
    void establishConnection() throws ClassNotFoundException, SQLException {
        connection = DataBaseConnection.getConnection();
    }

    @Test
    @Step("Test de connexion a la base de donnees - succes")
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