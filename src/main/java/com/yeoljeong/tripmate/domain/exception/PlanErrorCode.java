package com.yeoljeong.tripmate.domain.exception;

import com.yeoljeong.tripmate.exception.constants.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PlanErrorCode implements ErrorCode {

  // plan
  PLAN_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "일정 제목은 필수입니다." ),
  PLAN_TITLE_EXCEED(HttpStatus.BAD_REQUEST, "일정 제목은 255자를 초과할 수 없습니다."),
  PLAN_DESCRIPTION_REQUIRED(HttpStatus.BAD_REQUEST, "일정 설명은 필수입니다."),
  PLAN_TRAVEL_PERIOD_REQUIRED(HttpStatus.BAD_REQUEST, "여행 기간은 필수입니다."),
  PLAN_TRAVEL_PERIOD_INVALID(HttpStatus.BAD_REQUEST, "여행 기간이 올바르지 않습니다." ),
  PLAN_RECRUIT_STATUS_REQUIRED(HttpStatus.BAD_REQUEST, "일정 모집 상태는 필수입니다."),
  PLAN_TYPE_REQUIRED(HttpStatus.BAD_REQUEST, "일정 생성 방식은 필수입니다."),

  // plan_unit
  PLAN_UNIT_TITLE_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정 제목은 필수입니다."),
  PLAN_UNIT_TITLE_EXCEED(HttpStatus.BAD_REQUEST, "단위 일정 제목은 255자를 초과할 수 없습니다."),
  PLAN_UNIT_DESCRIPTION_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정 설명은 필수입니다." ),
  PLAN_UNIT_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정의 가격은 필수입니다." ),
  PLAN_UNIT_PRICE_INVALID(HttpStatus.BAD_REQUEST, "단위 일정의 가격은 음수가 불가능합니다." ),
  PLAN_UNIT_PARTICIPANT_COUNT_NEGATIVE(HttpStatus.BAD_REQUEST, "단위 일정의 참여 최대 인원은 1명 이상입니다." ),
  PLAN_UNIT_PARTICIPANT_EXCEED(HttpStatus.BAD_REQUEST, "단위 일정의 현재 인원은 최대 인원을 초과할 수 없습니다." ),
  PLAN_UNIT_TIME_RANGE_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정의 예상 시간은 필수입니다." ),
  PLAN_UNIT_TIME_RANGE_INVALID(HttpStatus.BAD_REQUEST, "단위 일정의 예상 시작 시간은 예상 종료 시간보다 이전이어야 합니다."),
  PLAN_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정에 연결될 일정 정보가 필요합니다." ),
  PLAN_UNIT_PRODUCT_REQUIRED(HttpStatus.BAD_REQUEST, "단위 일정의 상품 정보는 필수입니다." ),
  PLAN_UNIT_DAY_INVALID(HttpStatus.BAD_REQUEST, "단위 일정의 일차는 1 이상이어야 합니다." ),
  PLAN_UNIT_ORDER_INVALID(HttpStatus.BAD_REQUEST, "단위 일정의 순서는 1 이상이어야 합니다." ),
  PLAN_UNIT_PRICE_SCALE_INVALID(HttpStatus.BAD_REQUEST, "단위 일정의 가격은 소수점 둘째 자리까지 가능합니다."),
  // plan_product_snapshot
  PLAN_PRODUCT_SNAPSHOT_COUNTRY_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 국가는 필수입니다."),
  PLAN_PRODUCT_SNAPSHOT_STATE_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 주소(도/주)는 필수입니다." ),
  PLAN_PRODUCT_SNAPSHOT_STATE_EXCEED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 주소(도/주)는 255자를 초과할 수 없습니다." ),
  PLAN_PRODUCT_SNAPSHOT_CITY_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 주소(시)는 필수입니다."),
  PLAN_PRODUCT_SNAPSHOT_CITY_EXCEED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 주소(시)는 255자를 초과할 수 없습니다."),
  PLAN_PRODUCT_SNAPSHOT_PLAN_UNIT_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷에 연결될 단위일정 정보가 필요합니다." ),
  PLAN_PRODUCT_SNAPSHOT_PRICE_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 가격은 필수입니다." ),
  PLAN_PRODUCT_SNAPSHOT_PRICE_NEGATIVE(HttpStatus.BAD_REQUEST, "상품 스냅샷의 가격은 음수가 불가능합니다." ),
  PLAN_PRODUCT_SNAPSHOT_NAME_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 상품명은 필수입니다." ),
  PLAN_PRODUCT_SNAPSHOT_NAME_EXCEED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 상품명은 100자를 초과할 수 없습니다." ),
  PLAN_PRODUCT_ID_REQUIRED(HttpStatus.BAD_REQUEST, "상품 스냅샷의 상품 ID는 필수입니다." ),
  PLAN_PRODUCT_SNAPSHOT_PRICE_SCALE_INVALID(HttpStatus.BAD_REQUEST, "상품 스냅샷의 상품 가격은 소수점 둘째 자리까지만 가능합니다." ),

  // plan_participant
  PLAN_PARTICIPANT_PLAN_UNIT_REQUIRED(HttpStatus.BAD_REQUEST, "일정 참가에 연결될 단위 일정 정보가 필요합니다."),
  PLAN_PARTICIPANT_USER_REQUIRED(HttpStatus.BAD_REQUEST, "일정 참여자의 사용자 정보는 필수입니다." ),
  PLAN_PARTICIPANT_ROLE_REQUIRED(HttpStatus.BAD_REQUEST, "일정 참여자의 역할 정보는 필수입니다." );



  private final HttpStatus status;
  private final String message;

  PlanErrorCode(HttpStatus status, String message) {
    this.status = status;
    this.message = message;
  }

  @Override
  public int getCode() {
    return status.value();
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
