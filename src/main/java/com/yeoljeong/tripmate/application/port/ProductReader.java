package com.yeoljeong.tripmate.application.port;

import com.yeoljeong.tripmate.application.dto.external.ProductData;
import java.util.UUID;

public interface ProductReader {
  ProductData getProduct(UUID productId);
}
