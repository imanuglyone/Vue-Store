package com.vuestore.dto.auth;

import com.vuestore.dto.user.UserResponse;

public record AuthResponse(
  String token,
  UserResponse user
) {
}
