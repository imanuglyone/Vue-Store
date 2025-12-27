package com.vuestore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record OrderCreateRequest(
  @NotBlank @Size(min = 2, max = 120) String fullName,
  @NotBlank @Size(min = 6, max = 30) String phone,
  @NotBlank @Size(min = 5, max = 200) String address,
  @NotBlank String paymentMethod,
  @Size(max = 4) String paymentLast4
) {
}
