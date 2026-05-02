package com.yeoljeong.tripmate.domain.provider;

import java.util.UUID;

public interface ProductProvider {
  ProductData getProduct(UUID productId);
}
