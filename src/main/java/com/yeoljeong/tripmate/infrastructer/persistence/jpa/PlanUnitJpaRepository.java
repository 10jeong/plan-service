package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.plan.PlanUnit;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanUnitJpaRepository extends JpaRepository<PlanUnit, UUID> {

}
