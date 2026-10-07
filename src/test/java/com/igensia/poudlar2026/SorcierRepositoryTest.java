package com.igensia.poudlar2026;

import com.igensia.poudlar2026.entities.SorcierEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class SorcierRepositoryTest {

    @Autowired
    private SorcierRepository sorcierRepository;

    @Test
    void shouldSaveAndFindSorcier() {
        SorcierEntity sorcier = new SorcierEntity();
        sorcier.setNom("Potter");
        sorcier.setPrenom("Harry");
        sorcier.setDateNaissance(LocalDate.of(1980, 7, 31));

        SorcierEntity savedSorcier = sorcierRepository.save(sorcier);

        assertNotNull(savedSorcier.getId());
        SorcierEntity foundSorcier = sorcierRepository.findById(savedSorcier.getId()).orElseThrow();
        assertEquals("Potter", foundSorcier.getNom());
        assertEquals("Harry", foundSorcier.getPrenom());
        assertEquals(LocalDate.of(1980, 7, 31), foundSorcier.getDateNaissance());
    }

    @Test
    void shouldLoadTwoSeededSorciers() {
        assertEquals(2, sorcierRepository.count());
        assertTrue(sorcierRepository.findAll().stream()
                .anyMatch(sorcier -> "Harry".equals(sorcier.getPrenom())));
        assertTrue(sorcierRepository.findAll().stream()
                .anyMatch(sorcier -> "Hermione".equals(sorcier.getPrenom())));
    }
}
