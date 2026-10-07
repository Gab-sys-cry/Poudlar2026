package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.entities.SorcierEntity;

import java.util.List;
import java.util.Optional;

public interface ISorcierServices {

    List<SorcierEntity> findAll();

    Optional<SorcierEntity> findById(Integer id);

    SorcierEntity save(SorcierEntity sorcier);

    void deleteById(Integer id);
}
