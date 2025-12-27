package com.vuestore.dto.cart;

import java.math.BigDecimal;

public record CartItemResponse(
  Long productId,
  String name,
  String imageUrl,
  BigDecimal price,
  int quantity,
  BigDecimal lineTotal
) {
}
