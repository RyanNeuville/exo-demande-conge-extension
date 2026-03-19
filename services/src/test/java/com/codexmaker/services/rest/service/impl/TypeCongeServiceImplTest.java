package com.codexmaker.services.rest.service.impl;

import com.codexmaker.services.rest.model.entity.TypeConge;
import com.codexmaker.services.rest.repository.TypeCongeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TypeCongeServiceImplTest {

    @Mock
    private TypeCongeRepository typeCongeRepository;

    private TypeCongeServiceImpl typeCongeService;

    @BeforeEach
    void setUp() {
        typeCongeService = new TypeCongeServiceImpl(typeCongeRepository);
    }

    @Test
    void creerTypeConge_Success() {
        // Given
        TypeConge tc = new TypeConge();
        tc.setCode("CP");
        tc.setLibelle("Congés Payés");

        when(typeCongeRepository.save(any(TypeConge.class))).thenAnswer(i -> i.getArgument(0));

        // When
        TypeConge result = typeCongeService.creerTypeConge(tc);

        // Then
        assertNotNull(result.getId());
        assertEquals("CP", result.getCode());
        verify(typeCongeRepository).save(tc);
    }

    @Test
    void supprimerTypeConge_Fail_Used() {
        // Given
        String typeId = UUID.randomUUID().toString();
        when(typeCongeRepository.isTypeUsed(typeId)).thenReturn(true);

        // When & Then
        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            typeCongeService.supprimerTypeConge(typeId);
        });

        assertTrue(ex.getMessage().contains("utilisé"));
        verify(typeCongeRepository, never()).deleteById(anyString());
    }

    @Test
    void supprimerTypeConge_Success() {
        // Given
        String typeId = UUID.randomUUID().toString();
        when(typeCongeRepository.isTypeUsed(typeId)).thenReturn(false);

        // When
        typeCongeService.supprimerTypeConge(typeId);

        // Then
        verify(typeCongeRepository).deleteById(typeId);
    }
}
