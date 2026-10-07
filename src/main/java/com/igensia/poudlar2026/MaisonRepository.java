package com.igensia.poudlar2026;

import com.igensia.poudlar2026.entities.MaisonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaisonRepository extends JpaRepository<MaisonEntity, Integer> {
}
