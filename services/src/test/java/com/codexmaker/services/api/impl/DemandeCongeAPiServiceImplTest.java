package com.codexmaker.services.api.impl;

import org.exoplatform.services.organization.OrganizationService;
import org.exoplatform.services.organization.User;
import org.exoplatform.services.organization.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Classe de test pour l'implémentation de l'API DemandeCongeAPiServiceImpl des tests
 * unitaires pour vérifier le comportement de l'implémentation de l'API.
 */

@ExtendWith(MockitoExtension.class)
public class DemandeCongeAPiServiceImplTest {

    @Mock
    private OrganizationService organizationService;

    @Mock
    private UserHandler userHandler;

    @InjectMocks
    private DemandeCongeAPiServiceImpl service;

    @BeforeEach
    void setUp(){
        /** Simulation du UserHandler pour chaque test */
        when(organizationService.getUserHandler()).thenReturn(userHandler);
    }

    @Test
    @DisplayName("Test findExoUserById - Succès : Retourne un utilisateur valide")
    void findExoUserById_Success() throws Exception {
        User mockUser = mock(User.class);
        when(userHandler.findUserByName("testUser")).thenReturn(mockUser);

        User result = service.findExoUserById("testUser");

        assertNotNull(result);
        verify(userHandler).findUserByName("testUser");
    }

    @Test
    @DisplayName("Test findExoUserById - Échec : Utilisateur non trouvé")
    void findExoUserById_UserNotFound() throws Exception {
        when(userHandler.findUserByName("unknown")).thenReturn(null);

        User result = service.findExoUserById("unknown");

        assertNull(result);
        verify(userHandler).findUserByName("unknown");
    }

    @Test
    @DisplayName("Test findExoUserById - Exception : Gère l'erreur et retourne null")
    void findExoUserById_Exception() throws Exception {
        when(userHandler.findUserByName("errorUser")).thenThrow(new RuntimeException("Erreur simulée"));

        User result = service.findExoUserById("errorUser");

        assertNull(result);
        verify(userHandler).findUserByName("errorUser");
    }

    @Test
    @DisplayName("Test getExoUserFullName - Succes : Retourne le nom complet")
    void getExoUserFullName_Success() throws Exception{
        User mockUser = mock(User.class);
        when(mockUser.getUserName()).thenReturn("ryan neuville");
        when(userHandler.findUserByName("testUser")).thenReturn(mockUser);

        String result = service.getExoUserFullName("testUser");

        assertEquals("ryan neuville", result);
    }

    @Test
    @DisplayName("Test getExoUserFullName - Échec : Utilisateur non trouvé")
    void getExoUserFullName_UserNotFound() throws Exception{
        when(userHandler.findUserByName("unknown")).thenReturn(null);

        String result = service.getExoUserFullName("unknown");

        assertNull(result);
    }
}