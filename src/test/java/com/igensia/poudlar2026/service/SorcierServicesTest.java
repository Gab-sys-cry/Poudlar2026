package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.SorcierRepository;
import com.igensia.poudlar2026.entities.SorcierEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SorcierServicesTest {

    @Mock
    private SorcierRepository sorcierRepository;

    @InjectMocks
    private SorcierService sorcierService;

    @Test
    void shouldReturnAllSorciers() {
        List<SorcierEntity> sorciers = List.of(createSorcier("Potter", "Harry"));
        when(sorcierRepository.findAll()).thenReturn(sorciers);

        assertSame(sorciers, sorcierService.findAll());
    }

    @Test
    void shouldFindSorcierById() {
        SorcierEntity sorcier = createSorcier("Granger", "Hermione");
        when(sorcierRepository.findById(1)).thenReturn(Optional.of(sorcier));

        assertEquals(Optional.of(sorcier), sorcierService.findById(1));
    }

    @Test
    void shouldReturnEmptyWhenSorcierDoesNotExist() {
        when(sorcierRepository.findById(42)).thenReturn(Optional.empty());

        assertTrue(sorcierService.findById(42).isEmpty());
    }

    @Test
    void shouldSaveSorcier() {
        SorcierEntity sorcier = createSorcier("Potter", "Harry");
        when(sorcierRepository.save(sorcier)).thenReturn(sorcier);

        assertSame(sorcier, sorcierService.save(sorcier));
    }

    @Test
    void shouldDeleteSorcierById() {
        sorcierService.deleteById(1);

        verify(sorcierRepository).deleteById(1);
    }

    private SorcierEntity createSorcier(String nom, String prenom) {
        SorcierEntity sorcier = new SorcierEntity();
        sorcier.setNom(nom);
        sorcier.setPrenom(prenom);
        sorcier.setDateNaissance(LocalDate.of(1980, 7, 31));
        return sorcier;
    }
}
