package com.igensia.poudlar2026;

import com.igensia.poudlar2026.entities.SortilegeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SortilegeRepository extends JpaRepository<SortilegeEntity, Integer> {

    List<SortilegeEntity> findBySorcier_Id(Integer sorcierId);
}
