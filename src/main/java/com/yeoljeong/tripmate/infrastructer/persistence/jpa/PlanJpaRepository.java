package com.yeoljeong.tripmate.infrastructer.persistence.jpa;

import com.yeoljeong.tripmate.domain.enums.RecruitStatus;
import java.time.LocalDate;
import com.yeoljeong.tripmate.domain.model.Plan;
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

  @Query("""
    select distinct p
      from PlanParticipation pp
        join pp.planUnit pu
        join pu.plan p
          where pp.userId = :userId
            and pp.participationRole = com.yeoljeong.tripmate.domain.enums.ParticipationRole.GUEST
            and pp.isDeleted = false
          order by p.createdAt desc
  """)
  Slice<Plan> findMyParticipatedPlans(@Param("userId") UUID userId, Pageable pageable);


  @Query("""
    select distinct p
      from PlanParticipation host
        join host.planUnit pu
        join pu.plan p
          where host.userId = :userId
            and host.participationRole = com.yeoljeong.tripmate.domain.enums.ParticipationRole.HOST
            and host.isDeleted = false
          order by p.createdAt desc
  """)
  Slice<Plan> findHostPlans(@Param("userId") UUID userId, Pageable pageable);
//  @Query("""
//    select guest
//      from PlanParticipation guest
//        join guest.planUnit pu
//        join pu.plan p
//        join PlanParticipation host
//          on host.planUnit = pu
//          where host.userId = :userId
//            and host.participationRole = com.yeoljeong.tripmate.domain.enums.ParticipationRole.HOST
//            and guest.participationRole = com.yeoljeong.tripmate.domain.enums.ParticipationRole.GUEST
//          order by p.createdAt desc
//  """)
}
