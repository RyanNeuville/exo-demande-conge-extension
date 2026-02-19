package com.codexmaker.services.rest.config;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class DatabaseInitializerTest {

    @BeforeAll
    @Test
    static void initDb() {
        DatabaseInitializer.initialize();
    }

    public static void main(String[] args) {
        DatabaseInitializer.initialize();
    }
}
