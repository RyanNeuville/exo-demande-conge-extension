package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.Employe;
import com.codexmaker.services.rest.model.entity.Utilisateur;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link UtilisateurServiceImpl}.
 * Utilise Mockito pour simuler le dépôt des utilisateurs.
 */
@ExtendWith(MockitoExtension.class)
public class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    private UtilisateurServiceImpl utilisateurService;

    @BeforeEach
    void setUp() {
        utilisateurService = new UtilisateurServiceImpl(utilisateurRepository);
    }

    /**
     * Teste la consultation du solde d'un utilisateur.
     */
    @Test
    void consulterSolde_Success() {
        /** Given */
        String userId = "user1";
        when(utilisateurRepository.getSoldeById(userId)).thenReturn(25.0);

        /** When */
        double solde = utilisateurService.consulterSolde(userId);

        /** Then */
        assertEquals(25.0, solde);
        verify(utilisateurRepository).getSoldeById(userId);
    }

    @Test
    void getTousLesResponsables_Success() {
        /** Given */
        Employe r1 = new Employe();
        r1.setId("r1");
        List<Utilisateur> list = Arrays.asList(r1);
        when(utilisateurRepository.findAllResponsables()).thenReturn(list);

        /** When */
        List<Utilisateur> result = utilisateurService.getTousLesResponsables();

        /** Then */
        assertEquals(1, result.size());
        assertEquals("r1", result.get(0).getId());
    }
}
