package com.vuestore.service;

import com.vuestore.entity.User;
import com.vuestore.repository.UserRepository;
import com.vuestore.security.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User getCurrentUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails details)) {
      throw new IllegalArgumentException("Unauthorized");
    }
    return userRepository.findByEmail(details.getUsername())
      .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}
