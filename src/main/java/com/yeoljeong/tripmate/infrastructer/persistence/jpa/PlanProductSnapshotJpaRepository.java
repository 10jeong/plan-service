package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.plan.PlanProductSnapshot;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanProductSnapshotJpaRepository extends JpaRepository<PlanProductSnapshot, UUID> {

}
