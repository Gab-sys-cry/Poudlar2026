package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.SorcierRepository;
import com.igensia.poudlar2026.entities.SorcierEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SorcierService implements ISorcierServices {

    private final SorcierRepository sorcierRepository;

    public SorcierService(SorcierRepository sorcierRepository) {
        this.sorcierRepository = sorcierRepository;
    }

    @Override
    public List<SorcierEntity> findAll() {
        return sorcierRepository.findAll();
    }

    @Override
    public Optional<SorcierEntity> findById(Integer id) {
        return sorcierRepository.findById(id);
    }

    @Override
    public SorcierEntity save(SorcierEntity sorcier) {
        return sorcierRepository.save(sorcier);
    }

    @Override
    public void deleteById(Integer id) {
        sorcierRepository.deleteById(id);
    }
}
