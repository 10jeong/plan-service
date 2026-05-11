package com.yeoljeong.tripmate.domain.enums;

import java.util.Set;

public enum ParticipationStatus {

  REQUESTED,
  APPROVED,
  RESERVED,
  CONFIRMED,
  REJECTED,
  RESERVED_CANCELLED,
  PAYMENT_CANCELLED;

  private Set<ParticipationStatus> changeableStatuses;

  static {
    REQUESTED.changeableStatuses =
        Set.of(APPROVED, REJECTED);

    APPROVED.changeableStatuses =
        Set.of(RESERVED);

    RESERVED.changeableStatuses =
        Set.of(CONFIRMED, RESERVED_CANCELLED);

    CONFIRMED.changeableStatuses =
        Set.of(PAYMENT_CANCELLED);

    REJECTED.changeableStatuses =
        Set.of();

    RESERVED_CANCELLED.changeableStatuses =
        Set.of();

    PAYMENT_CANCELLED.changeableStatuses =
        Set.of();
  }

  public boolean canChangeTo(ParticipationStatus target) {
    return changeableStatuses.contains(target);
  }
}
