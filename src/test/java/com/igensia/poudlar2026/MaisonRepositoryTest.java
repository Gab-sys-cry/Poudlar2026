package com.igensia.poudlar2026;

import com.igensia.poudlar2026.entities.MaisonEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MaisonRepositoryTest {

    @Autowired
    private MaisonRepository maisonRepository;

    @Test
    void shouldSaveAndFindMaison() {
        MaisonEntity maison = new MaisonEntity();
        maison.setNom("Serpentard");
        maison.setPoints(25);

        MaisonEntity savedMaison = maisonRepository.save(maison);

        assertNotNull(savedMaison.getId());
        MaisonEntity foundMaison = maisonRepository.findById(savedMaison.getId()).orElseThrow();
        assertEquals("Serpentard", foundMaison.getNom());
        assertEquals(25, foundMaison.getPoints());
    }

    @Test
    void shouldFindSeededMaison() {
        assertTrue(maisonRepository.findAll().stream()
                .anyMatch(maison -> "Gryffondor".equals(maison.getNom())));
    }
}
