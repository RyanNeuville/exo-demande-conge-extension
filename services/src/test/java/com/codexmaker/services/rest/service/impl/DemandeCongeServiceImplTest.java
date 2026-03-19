package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.DemandeConge;
import com.codexmaker.services.rest.model.enums.StatutDemande;
import com.codexmaker.services.rest.repository.DemandeCongeRepository;
import com.codexmaker.services.rest.repository.HistoriqueEtatRepository;
import com.codexmaker.services.rest.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour {@link DemandeCongeServiceImpl}.
 * Utilise Mockito pour simuler les dépendances du dépôt.
 */
@ExtendWith(MockitoExtension.class)
public class DemandeCongeServiceImplTest {

    @Mock
    private DemandeCongeRepository demandeCongeRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private HistoriqueEtatRepository historiqueEtatRepository;

    private DemandeCongeServiceImpl demandeCongeService;

    @BeforeEach
    void setUp() {
        demandeCongeService = new DemandeCongeServiceImpl(demandeCongeRepository, utilisateurRepository, historiqueEtatRepository);
    }

    /**
     * Teste la soumission réussie d'une demande.
     */
    @Test
    void soumettreDemande_Success() {
        // Given
        String userId = "user123";
        DemandeConge demande = new DemandeConge();
        demande.setDateDebut(LocalDate.now().plusDays(1));
        demande.setDateFin(LocalDate.now().plusDays(3));
        demande.setDureeJoursOuvres(2);

        when(demandeCongeRepository.hasChevauchement(eq(userId), isNull(), any(), any())).thenReturn(false);
        when(utilisateurRepository.getSoldeById(userId)).thenReturn(10);
        when(demandeCongeRepository.save(any(DemandeConge.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        DemandeConge result = demandeCongeService.soumettreDemande(demande, userId);

        // Then
        assertNotNull(result);
        assertEquals(StatutDemande.EN_ATTENTE, result.getStatut());
        assertEquals(userId, result.getUserId());
        verify(utilisateurRepository).updateSolde(userId, 8);
        verify(demandeCongeRepository).save(demande);
        verify(historiqueEtatRepository).save(any());
    }

    @Test
    void soumettreDemande_Fail_Overlap() {
        // Given
        String userId = "user123";
        DemandeConge demande = new DemandeConge();
        demande.setDateDebut(LocalDate.now().plusDays(1));
        demande.setDateFin(LocalDate.now().plusDays(3));

        when(demandeCongeRepository.hasChevauchement(eq(userId), isNull(), any(), any())).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            demandeCongeService.soumettreDemande(demande, userId);
        });

        assertEquals("Une demande existe déjà pour cette période.", exception.getMessage());
        verify(demandeCongeRepository, never()).save(any());
    }

    @Test
    void soumettreDemande_Fail_InsufficientBalance() {
        // Given
        String userId = "user123";
        DemandeConge demande = new DemandeConge();
        demande.setDureeJoursOuvres(5);

        when(demandeCongeRepository.hasChevauchement(anyString(), isNull(), any(), any())).thenReturn(false);
        when(utilisateurRepository.getSoldeById(userId)).thenReturn(3);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            demandeCongeService.soumettreDemande(demande, userId);
        });

        assertEquals("Solde insuffisant pour cette demande.", exception.getMessage());
    }

    @Test
    void validerDemande_Success() {
        // Given
        String demandeId = "req1";
        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);
        demande.setStatut(StatutDemande.EN_ATTENTE);

        when(demandeCongeRepository.findById(demandeId)).thenReturn(demande);

        // When
        demandeCongeService.validerDemande(demandeId, "Approuvé");

        // Then
        verify(demandeCongeRepository).updateStatus(eq(demandeId), eq(StatutDemande.VALIDEE), eq("Approuvé"), any(), any());
        verify(historiqueEtatRepository).save(any());
    }

    /**
     * Teste le refus d'une demande avec recrédit du solde.
     */
    @Test
    void refuserDemande_Success() {
        // Given
        String demandeId = "req1";
        String userId = "user123";
        DemandeConge demande = new DemandeConge();
        demande.setId(demandeId);
        demande.setUserId(userId);
        demande.setStatut(StatutDemande.EN_ATTENTE);
        demande.setDureeJoursOuvres(3);

        when(demandeCongeRepository.findById(demandeId)).thenReturn(demande);
        when(utilisateurRepository.getSoldeById(userId)).thenReturn(10);

        // When
        demandeCongeService.refuserDemande(demandeId, "Budget insuffisant");

        // Then
        verify(utilisateurRepository).updateSolde(userId, 13); // Restored
        verify(demandeCongeRepository).updateStatus(eq(demandeId), eq(StatutDemande.REFUSEE), eq("Budget insuffisant"), any(), any());
        verify(historiqueEtatRepository).save(any());
    }
}
