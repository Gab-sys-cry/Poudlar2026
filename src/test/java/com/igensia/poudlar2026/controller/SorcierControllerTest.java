package com.igensia.poudlar2026.controller;

import com.igensia.poudlar2026.entities.SortilegeEntity;
import com.igensia.poudlar2026.service.ISorcierServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SorcierControllerTest {

    @Mock
    private ISorcierServices sorcierServices;

    @InjectMocks
    private SorcierController sorcierController;

    @Test
    void shouldReturnSortilegesForExistingSorcier() {
        List<SortilegeEntity> sortileges = List.of(new SortilegeEntity());
        when(sorcierServices.findSortilegesBySorcierId(1)).thenReturn(Optional.of(sortileges));

        var response = sorcierController.getSortileges(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(sortileges, response.getBody());
    }

    @Test
    void shouldReturnNotFoundForUnknownSorcier() {
        when(sorcierServices.findSortilegesBySorcierId(42)).thenReturn(Optional.empty());

        var response = sorcierController.getSortileges(42);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(sorcierServices).findSortilegesBySorcierId(42);
    }
}
