package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import com.yeoljeong.tripmate.domain.model.plan.Plan;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanJpaRepository extends JpaRepository<Plan, UUID> {

  @Query("""
  select p
    from Plan p
      where p.recruitStatus = :recruitStatus
        and (:title is null or p.title like :title)
        and (:startDate is null or p.travelPeriod.startDate >= :startDate)
        and (:endDate is null or p.travelPeriod.endDate <= :endDate)
  """)
  Slice<Plan> findByCondition(
      @Param("title") String title,
      @Param("startDate") LocalDate startDate,
      @Param("endDate") LocalDate endDate,
      @Param("recruitStatus") RecruitStatus recruitStatus,
      Pageable pageable);
}
