package com.vuestore.dto.order;

import jakarta.validation.constraints.NotBlank;

public record UpdateOrderStatusRequest(
  @NotBlank String status
) {
}
