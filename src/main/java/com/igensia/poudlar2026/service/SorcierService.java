package com.igensia.poudlar2026.service;

import com.igensia.poudlar2026.SorcierRepository;
import com.igensia.poudlar2026.SortilegeRepository;
import com.igensia.poudlar2026.entities.SorcierEntity;
import com.igensia.poudlar2026.entities.SortilegeEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SorcierService implements ISorcierServices {

    private final SorcierRepository sorcierRepository;
    private final SortilegeRepository sortilegeRepository;

    public SorcierService(SorcierRepository sorcierRepository, SortilegeRepository sortilegeRepository) {
        this.sorcierRepository = sorcierRepository;
        this.sortilegeRepository = sortilegeRepository;
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
    public Optional<List<SortilegeEntity>> findSortilegesBySorcierId(Integer sorcierId) {
        if (!sorcierRepository.existsById(sorcierId)) {
            return Optional.empty();
        }
        return Optional.of(sortilegeRepository.findBySorcier_Id(sorcierId));
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
