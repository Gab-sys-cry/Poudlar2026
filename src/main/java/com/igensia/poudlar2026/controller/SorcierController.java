package com.igensia.poudlar2026.controller;

import com.igensia.poudlar2026.entities.SortilegeEntity;
import com.igensia.poudlar2026.service.ISorcierServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sorciers")
public class SorcierController {

    private final ISorcierServices sorcierServices;

    public SorcierController(ISorcierServices sorcierServices) {
        this.sorcierServices = sorcierServices;
    }

    @GetMapping("/{sorcierId}/sortileges")
    public ResponseEntity<List<SortilegeEntity>> getSortileges(@PathVariable Integer sorcierId) {
        return sorcierServices.findSortilegesBySorcierId(sorcierId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
