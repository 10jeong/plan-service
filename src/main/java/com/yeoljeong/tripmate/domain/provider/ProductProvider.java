package com.yeoljeong.tripmate.domain.provider;

import java.util.UUID;

public interface ProductProvider {
  ProductData get(UUID productId);
}
