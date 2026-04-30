package com.yeoljeong.tripmate.infrastructer.external.client;

import com.yeoljeong.tripmate.domain.provider.ProductData;
import jakarta.ws.rs.InternalServerErrorException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductFeignClientFallback implements FallbackFactory<ProductFeignClient> {

  @Override
  public ProductFeignClient create(Throwable cause) {

    return new ProductFeignClient() {

      @Override
      public ProductData get(UUID productId) {

        log.info("[Product service Fallback] ID : {} 조회 중 장애 발, 사유 : {}",
            productId,
            cause.getMessage(),
            cause); // 에러 발생 위치 (파생 위치)

        throw new InternalServerErrorException("Product service API 요청 처리 실패");
      }
    };
  }
}
