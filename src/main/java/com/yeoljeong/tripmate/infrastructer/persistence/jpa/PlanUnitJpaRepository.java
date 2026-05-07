package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.model.Plan;
import com.yeoljeong.tripmate.domain.model.PlanUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanUnitJpaRepository extends JpaRepository<PlanUnit, UUID> {

  Optional<PlanUnit> findByIdAndPlanId(UUID planUnitId, UUID planId);

  List<PlanUnit> findAllByPlanOrderByDayAscUnitTimeRange_StartTimeAsc(Plan plan);

  @Modifying
  @Query("""
   update PlanUnit u
     set u.participantCount.currentCount = u.participantCount.currentCount + :quantity
       where u.id = :planUnitId
         and u.participantCount.currentCount + :quantity <= u.participantCount.maxCount
  """)
  int addParticipantCount(@Param("planUnitId") UUID planUnitId, @Param("quantity") int quantity);
}
