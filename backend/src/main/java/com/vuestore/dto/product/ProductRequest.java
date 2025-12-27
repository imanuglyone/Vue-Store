package com.vuestore.dto.product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProductRequest(
  @NotBlank String name,
  @NotBlank String description,
  @NotNull @DecimalMin("0.0") BigDecimal price,
  @NotBlank String imageUrl,
  @NotBlank String category,
  @Min(0) int stock
) {
}
