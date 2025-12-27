package com.vuestore.dto.support;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ContactRequest(
  @NotBlank @Size(min = 2, max = 120) String name,
  @Email @NotBlank String email,
  @NotBlank @Size(min = 10, max = 2000) String message
) {
}
