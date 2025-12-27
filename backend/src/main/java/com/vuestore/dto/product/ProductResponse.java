package com.vuestore.dto.product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponse(
  Long id,
  String name,
  String description,
  BigDecimal price,
  String imageUrl,
  String category,
  int stock,
  LocalDateTime createdAt
) {
}
