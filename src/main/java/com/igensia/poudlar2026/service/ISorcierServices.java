package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.entities.SorcierEntity;
import com.igensia.poudlar2026.entities.SortilegeEntity;

import java.util.List;
import java.util.Optional;

public interface ISorcierServices {

    List<SorcierEntity> findAll();

    Optional<SorcierEntity> findById(Integer id);

    Optional<List<SortilegeEntity>> findSortilegesBySorcierId(Integer sorcierId);

    SorcierEntity save(SorcierEntity sorcier);

    void deleteById(Integer id);
}
