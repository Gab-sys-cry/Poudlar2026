package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.entities.SorcierEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SorcierServicesH2Test {

    @Autowired
    private ISorcierServices sorcierServices;

    @Test
    void shouldReadSeededSorciersFromH2() {
        assertEquals(2, sorcierServices.findAll().size());
        assertTrue(sorcierServices.findAll().stream()
                .anyMatch(sorcier -> "Harry".equals(sorcier.getPrenom())));
    }

    @Test
    void shouldCreateUpdateFindAndDeleteSorcierInH2() {
        SorcierEntity sorcier = new SorcierEntity();
        sorcier.setNom("Weasley");
        sorcier.setPrenom("Ron");
        sorcier.setDateNaissance(LocalDate.of(1980, 3, 1));

        SorcierEntity savedSorcier = sorcierServices.save(sorcier);

        assertNotNull(savedSorcier.getId());
        Optional<SorcierEntity> foundSorcier = sorcierServices.findById(savedSorcier.getId());
        assertTrue(foundSorcier.isPresent());
        assertEquals("Ron", foundSorcier.orElseThrow().getPrenom());

        foundSorcier.orElseThrow().setNom("Weasley-Potter");
        SorcierEntity updatedSorcier = sorcierServices.save(foundSorcier.orElseThrow());
        assertEquals("Weasley-Potter", updatedSorcier.getNom());

        sorcierServices.deleteById(savedSorcier.getId());
        assertTrue(sorcierServices.findById(savedSorcier.getId()).isEmpty());
    }
}
