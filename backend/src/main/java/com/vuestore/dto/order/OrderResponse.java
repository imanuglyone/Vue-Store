package com.vuestore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
  Long id,
  String status,
  BigDecimal totalAmount,
  LocalDateTime createdAt,
  List<OrderItemResponse> items,
  String fullName,
  String phone,
  String address,
  String paymentMethod,
  String paymentLast4,
  String paymentStatus
) {
}
