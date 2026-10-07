package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.SorcierRepository;
import com.igensia.poudlar2026.SortilegeRepository;
import com.igensia.poudlar2026.entities.SorcierEntity;
import com.igensia.poudlar2026.entities.SortilegeEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SorcierServicesH2Test {

    @Autowired
    private ISorcierServices sorcierServices;

    @Autowired
    private SorcierRepository sorcierRepository;

    @Autowired
    private SortilegeRepository sortilegeRepository;

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

    @Test
    void shouldFindSortilegesForSorcierFromH2() {
        SorcierEntity sorcier = sorcierRepository.findAll().getFirst();

        SortilegeEntity sortilege = new SortilegeEntity();
        sortilege.setNom("Expecto Patronum");
        sortilege.setType("defence");
        sortilege.setSorcier(sorcier);
        sortilegeRepository.save(sortilege);

        SortilegeEntity secondSortilege = new SortilegeEntity();
        secondSortilege.setNom("Lumos");
        secondSortilege.setType("lumiere");
        secondSortilege.setSorcier(sorcier);
        sortilegeRepository.save(secondSortilege);

        var sortileges = sorcierServices.findSortilegesBySorcierId(sorcier.getId()).orElseThrow();

        assertEquals(2, sortileges.size());
        assertTrue(sortileges.stream().anyMatch(
                item -> "Expecto Patronum".equals(item.getNom()) && "defence".equals(item.getType())));
        assertTrue(sortileges.stream().anyMatch(
                item -> "Lumos".equals(item.getNom()) && "lumiere".equals(item.getType())));
    }

    @Test
    void shouldDistinguishUnknownSorcierFromSorcierWithoutSortileges() {
        Integer existingSorcierId = sorcierRepository.findAll().getFirst().getId();

        assertTrue(sorcierServices.findSortilegesBySorcierId(existingSorcierId).orElseThrow().isEmpty());
        assertTrue(sorcierServices.findSortilegesBySorcierId(-1).isEmpty());
    }
}
