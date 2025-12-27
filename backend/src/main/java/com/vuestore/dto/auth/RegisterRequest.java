package com.vuestore.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
  @NotBlank @Size(min = 2, max = 60) String name,
  @Email @NotBlank String email,
  @NotBlank @Size(min = 6, max = 100) String password
) {
}
