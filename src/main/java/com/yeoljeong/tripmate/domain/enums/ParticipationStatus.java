package com.yeoljeong.tripmate.domain.enums;

public enum ParticipationStatus {
  REQUESTED{
    @Override
    public boolean canChangeTo(ParticipationStatus target) {
      return target == APPROVED
          || target == REJECTED;
    }
  },
  APPROVED {
    @Override
    public boolean canChangeTo(ParticipationStatus target) {
      return target == RESERVED;
    }
  },
  RESERVED {
    @Override
    public boolean canChangeTo(ParticipationStatus target) {
      return target == CONFIRMED
          || target == RESERVED_CANCELLED;
    }
  },
  CONFIRMED {
    @Override
    public boolean canChangeTo(ParticipationStatus target) {
      return target == PAYMENT_CANCELLED;
    }
  },
  REJECTED , RESERVED_CANCELLED, PAYMENT_CANCELLED;

  public boolean canChangeTo(ParticipationStatus target) {
    return false;
  }

}
