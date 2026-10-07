package com.igensia.poudlar2026;

import com.igensia.poudlar2026.entities.SorcierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SorcierRepository extends JpaRepository<SorcierEntity, Integer> {
}
