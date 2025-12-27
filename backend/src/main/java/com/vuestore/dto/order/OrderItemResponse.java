package com.vuestore.dto.order;

import java.math.BigDecimal;

public record OrderItemResponse(
  String productName,
  BigDecimal productPrice,
  int quantity,
  BigDecimal lineTotal
) {
}
